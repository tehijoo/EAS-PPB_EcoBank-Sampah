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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.data.entity.level
import com.example.registrasisiswa.ui.theme.Black
import com.example.registrasisiswa.ui.theme.KatalogBg
import com.example.registrasisiswa.ui.theme.White
import com.example.registrasisiswa.viewmodel.EcoBankViewModel
import com.example.registrasisiswa.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenggunaDetailScreen(
    viewModel: EcoBankViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPenggunaCard: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactionHistory: () -> Unit,
    onNavigateToReward: () -> Unit,
    onNavigateToWasteCatalog: () -> Unit
) {
    val pengguna by viewModel.currentPengguna.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val isPengguna = userRole == UserRole.PENGGUNA
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Pengguna") },
            text = { Text("Hapus pengguna ${pengguna?.name}? Semua data riwayat setor akan hilang.") },
            confirmButton = {
                TextButton(onClick = {
                    pengguna?.let { viewModel.deletePengguna(it) }
                    showDeleteDialog = false
                    onNavigateBack()
                }) {
                    Text("Hapus", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pengguna", color = White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = White)
                    }
                },
                actions = {
                    if (isPengguna) {
                        IconButton(onClick = {
                            viewModel.logout()
                            onNavigateBack()
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Keluar", tint = White)
                        }
                    } else {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        pengguna?.let { p ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(24.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(White.copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = p.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(p.name, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = White)
                            Text(p.formattedId(), fontSize = 13.sp, color = White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("${p.points}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = White)
                                    Text("Total Poin", fontSize = 11.sp, color = White.copy(alpha = 0.75f))
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = when (p.level()) { "Champion" -> "🏆"; "Aktif" -> "⭐"; else -> "🌱" },
                                        fontSize = 28.sp
                                    )
                                    Text(p.level(), fontSize = 11.sp, color = White.copy(alpha = 0.75f))
                                }
                            }
                        }
                    }
                }

                item { Text("Menu Pengguna", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Black) }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ActionMenuCard("💳", "Kartu Pengguna", "Tampilkan kartu digital", Modifier.weight(1f)) {
                                onNavigateToPenggunaCard()
                            }
                            ActionMenuCard("🌱", "Setor Sampah", "Catat setoran sampah", Modifier.weight(1f)) {
                                onNavigateToAddTransaction()
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ActionMenuCard("📋", "Riwayat Setor", "Lihat semua riwayat", Modifier.weight(1f)) {
                                onNavigateToTransactionHistory()
                            }
                            ActionMenuCard("🎁", "Tukar Reward", "${p.points} poin tersedia", Modifier.weight(1f)) {
                                onNavigateToReward()
                            }
                        }
                        // Katalog Sampah — full width
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onNavigateToWasteCatalog),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = KatalogBg),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("📖", fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            "Katalog Sampah",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Black
                                        )
                                        Text(
                                            "Lihat jenis sampah & nilai poinnya",
                                            fontSize = 11.sp,
                                            color = Black.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                                Text("›", fontSize = 22.sp, color = Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Info Pengguna", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(label = "Email", value = p.email)
                            InfoRow(label = "Nomor HP", value = p.phone)
                            InfoRow(label = "Bergabung", value = p.joinDate)
                        }
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun ActionMenuCard(emoji: String, title: String, subtitle: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(110.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Black)
                Text(text = subtitle, fontSize = 10.sp, color = Black.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 13.sp, color = Black.copy(alpha = 0.6f))
        Text(text = value, fontSize = 13.sp, color = Black, fontWeight = FontWeight.Medium)
    }
}
