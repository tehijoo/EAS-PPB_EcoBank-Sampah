package com.example.registrasisiswa.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.registrasisiswa.data.entity.Pengguna
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

enum class UserRole { ADMIN, PENGGUNA }

class EcoBankViewModel(private val repository: EcoBankRepository) : ViewModel() {

    val allPengguna = repository.getAllPengguna()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPengguna = repository.getTotalPengguna()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Auth state
    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole = _userRole.asStateFlow()

    private val _currentPenggunaId = MutableStateFlow(-1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentPengguna = _currentPenggunaId
        .flatMapLatest { id ->
            if (id == -1) flowOf(null) else repository.getPenggunaById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentTransactions = _currentPenggunaId
        .flatMapLatest { id ->
            if (id == -1) flowOf(emptyList()) else repository.getTransactionsByPenggunaId(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiMessage = MutableStateFlow<String?>(null)
    val uiMessage = _uiMessage.asStateFlow()

    fun loginAsAdmin() {
        _userRole.value = UserRole.ADMIN
    }

    fun loginAsPengguna(penggunaId: Int) {
        _userRole.value = UserRole.PENGGUNA
        selectPengguna(penggunaId)
    }

    fun logout() {
        _userRole.value = null
        _currentPenggunaId.value = -1
    }

    fun selectPengguna(id: Int) {
        _currentPenggunaId.value = id
    }

    fun addPengguna(name: String, email: String, phone: String) {
        viewModelScope.launch {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date())
            repository.insertPengguna(
                Pengguna(name = name.trim(), email = email.trim(), phone = phone.trim(), joinDate = date)
            )
            _uiMessage.value = "Pengguna berhasil didaftarkan!"
        }
    }

    // Digunakan oleh pengguna saat daftar baru — langsung login setelah daftar
    fun registerAndLoginAsPengguna(
        name: String,
        email: String,
        phone: String,
        onSuccess: (penggunaId: Int) -> Unit
    ) {
        viewModelScope.launch {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date())
            val id = repository.insertPenggunaAndGetId(
                Pengguna(name = name.trim(), email = email.trim(), phone = phone.trim(), joinDate = date)
            )
            loginAsPengguna(id)
            onSuccess(id)
        }
    }

    fun addTransaction(pengguna: Pengguna, wasteType: String, weightGrams: Double, pointsPerKg: Int) {
        viewModelScope.launch {
            repository.addWasteTransaction(pengguna, wasteType, weightGrams, pointsPerKg)
            val points = (weightGrams * pointsPerKg / 1000).toInt()
            _uiMessage.value = "Setor berhasil! +$points poin"
        }
    }

    fun redeemReward(pengguna: Pengguna, rewardName: String, pointCost: Int) {
        viewModelScope.launch {
            val success = repository.redeemReward(pengguna, pointCost, rewardName)
            _uiMessage.value = if (success)
                "Selamat! $rewardName berhasil ditukar!"
            else
                "Poin tidak cukup. Butuh $pointCost poin."
        }
    }

    fun deletePengguna(pengguna: Pengguna) {
        viewModelScope.launch {
            repository.deletePengguna(pengguna)
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
