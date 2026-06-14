package com.example.registrasisiswa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.Member
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.data.entity.level
import com.example.registrasisiswa.ui.theme.BackgroundCream
import com.example.registrasisiswa.ui.theme.DarkText
import com.example.registrasisiswa.ui.theme.MediumText
import com.example.registrasisiswa.ui.theme.RoseGold
import com.example.registrasisiswa.ui.theme.RoseGoldDark
import com.example.registrasisiswa.ui.theme.RoseGoldLight
import com.example.registrasisiswa.ui.theme.SurfaceWhite
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NasabahLoginScreen(
    viewModel: EcoBankViewModel,
    onLoginSuccess: (memberId: Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("🔍 Masuk", "✏️ Daftar Baru")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Portal Nasabah", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoseGold)
            )
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = RoseGold,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color.White
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = Color.White
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> NasabahLoginTab(viewModel = viewModel, onLoginSuccess = onLoginSuccess)
                1 -> NasabahRegisterTab(viewModel = viewModel, onRegisterSuccess = onLoginSuccess)
            }
        }
    }
}

@Composable
fun NasabahLoginTab(
    viewModel: EcoBankViewModel,
    onLoginSuccess: (memberId: Int) -> Unit
) {
    val allMembers by viewModel.allMembers.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val searchResults = remember(searchQuery, allMembers) {
        if (searchQuery.isBlank()) allMembers
        else allMembers.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.formattedId().contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search area
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = RoseGoldLight
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cari akun Anda", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = RoseGoldDark)
                Text("Ketik nama atau ID nasabah", fontSize = 12.sp, color = RoseGoldDark.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Nama atau NSB00001...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = RoseGold)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGold,
                        unfocusedBorderColor = RoseGold.copy(alpha = 0.4f),
                        focusedLabelColor = RoseGold,
                        cursorColor = RoseGold,
                        unfocusedContainerColor = SurfaceWhite,
                        focusedContainerColor = SurfaceWhite
                    ),
                    singleLine = true
                )
            }
        }

        if (allMembers.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                    Text("👤", fontSize = 52.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Belum ada nasabah terdaftar", fontWeight = FontWeight.Bold, color = DarkText)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Hubungi pengelola untuk mendaftarkan akun Anda, atau gunakan tab Daftar Baru.",
                        fontSize = 12.sp,
                        color = MediumText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else if (searchResults.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔍", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("\"$searchQuery\" tidak ditemukan", color = MediumText, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (searchQuery.isBlank()) {
                    item {
                        Text("Pilih akun Anda", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
                    }
                }
                items(searchResults) { member ->
                    NasabahLoginCard(member = member, onClick = {
                        viewModel.loginAsNasabah(member.id)
                        onLoginSuccess(member.id)
                    })
                }
            }
        }
    }
}

@Composable
fun NasabahLoginCard(member: Member, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = SurfaceWhite,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(RoseGoldLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    member.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoseGoldDark
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkText)
                Text(member.formattedId(), fontSize = 11.sp, color = MediumText)
                Text("${member.points} poin  •  ${member.level()}", fontSize = 11.sp, color = RoseGoldDark)
            }
            Surface(
                color = RoseGoldLight,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Masuk",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoseGoldDark,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun NasabahRegisterTab(
    viewModel: EcoBankViewModel,
    onRegisterSuccess: (memberId: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Info card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = RoseGoldLight
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("👤", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Daftar sebagai Nasabah Baru", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RoseGoldDark)
                    Text("Isi data diri Anda untuk bergabung", fontSize = 11.sp, color = RoseGoldDark.copy(alpha = 0.75f))
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Data Diri", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkText)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = "" },
                    label = { Text("Nama Lengkap *") },
                    placeholder = { Text("Contoh: Budi Santoso") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    isError = nameError.isNotEmpty(),
                    supportingText = { if (nameError.isNotEmpty()) Text(nameError, color = Color.Red) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGold,
                        focusedLabelColor = RoseGold,
                        cursorColor = RoseGold
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; emailError = "" },
                    label = { Text("Email *") },
                    placeholder = { Text("contoh@email.com") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    isError = emailError.isNotEmpty(),
                    supportingText = { if (emailError.isNotEmpty()) Text(emailError, color = Color.Red) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGold,
                        focusedLabelColor = RoseGold,
                        cursorColor = RoseGold
                    ),
                    singleLine = true
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { c -> c.isDigit() }; phoneError = "" },
                    label = { Text("Nomor HP *") },
                    placeholder = { Text("08xxxxxxxxxx") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = phoneError.isNotEmpty(),
                    supportingText = { if (phoneError.isNotEmpty()) Text(phoneError, color = Color.Red) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoseGold,
                        focusedLabelColor = RoseGold,
                        cursorColor = RoseGold
                    ),
                    singleLine = true
                )
            }
        }

        Button(
            onClick = {
                var valid = true
                if (name.isBlank()) { nameError = "Nama tidak boleh kosong"; valid = false }
                if (email.isBlank() || !email.contains("@")) { emailError = "Email tidak valid"; valid = false }
                if (phone.isBlank() || phone.length < 9) { phoneError = "Nomor HP tidak valid"; valid = false }
                if (valid) {
                    viewModel.registerAndLoginAsNasabah(name, email, phone) { newId ->
                        onRegisterSuccess(newId)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RoseGoldDark)
        ) {
            Text("✅  Daftar & Masuk", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
