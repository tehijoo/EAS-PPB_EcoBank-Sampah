package com.example.registrasisiswa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.registrasisiswa.data.entity.Member
import com.example.registrasisiswa.data.repository.EcoBankRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class UserRole { ADMIN, NASABAH }

class EcoBankViewModel(private val repository: EcoBankRepository) : ViewModel() {

    val allMembers = repository.getAllMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalMembers = repository.getTotalMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Auth state
    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole = _userRole.asStateFlow()

    private val _currentMemberId = MutableStateFlow(-1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentMember = _currentMemberId
        .flatMapLatest { id ->
            if (id == -1) flowOf(null) else repository.getMemberById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentTransactions = _currentMemberId
        .flatMapLatest { id ->
            if (id == -1) flowOf(emptyList()) else repository.getTransactionsByMemberId(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage = _uiMessage.asStateFlow()

    fun loginAsAdmin() {
        _userRole.value = UserRole.ADMIN
    }

    fun loginAsNasabah(memberId: Int) {
        _userRole.value = UserRole.NASABAH
        selectMember(memberId)
    }

    fun logout() {
        _userRole.value = null
        _currentMemberId.value = -1
    }

    fun selectMember(id: Int) {
        _currentMemberId.value = id
    }

    fun addMember(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date())
            repository.insertMember(
                Member(name = name.trim(), email = email.trim(), phone = phone.trim(), joinDate = date)
            )
            _uiMessage.value = "Nasabah berhasil didaftarkan!"
        }
    }

    // Digunakan oleh nasabah saat daftar baru — langsung login setelah daftar
    fun registerAndLoginAsNasabah(
        name: String,
        email: String,
        phone: String,
        onSuccess: (memberId: Int) -> Unit
    ) {
        viewModelScope.launch {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date())
            val id = repository.insertMemberAndGetId(
                Member(name = name.trim(), email = email.trim(), phone = phone.trim(), joinDate = date)
            )
            loginAsNasabah(id)
            onSuccess(id)
        }
    }

    fun addTransaction(member: Member, wasteType: String, weightGrams: Double, pointsPerKg: Int) {
        viewModelScope.launch {
            repository.addWasteTransaction(member, wasteType, weightGrams, pointsPerKg)
            val points = (weightGrams * pointsPerKg / 1000).toInt()
            _uiMessage.value = "Setor berhasil! +$points poin"
        }
    }

    fun redeemReward(member: Member, rewardName: String, pointCost: Int) {
        viewModelScope.launch {
            val success = repository.redeemReward(member, pointCost, rewardName)
            _uiMessage.value = if (success)
                "Selamat! $rewardName berhasil ditukar!"
            else
                "Poin tidak cukup. Butuh $pointCost poin."
        }
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch {
            repository.deleteMember(member)
        }
    }

    fun clearMessage() {
        _uiMessage.value = null
    }
}

class EcoBankViewModelFactory(private val repository: EcoBankRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EcoBankViewModel(repository) as T
    }
}
