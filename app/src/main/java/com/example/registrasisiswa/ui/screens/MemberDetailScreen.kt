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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.data.entity.level
import com.example.registrasisiswa.ui.theme.BackgroundCream
import com.example.registrasisiswa.ui.theme.DarkText
import com.example.registrasisiswa.ui.theme.MediumText
import com.example.registrasisiswa.ui.theme.RoseGold
import com.example.registrasisiswa.ui.theme.RoseGoldDark
import com.example.registrasisiswa.ui.theme.SuccessGreen
import com.example.registrasisiswa.ui.theme.SuccessGreenBg
import com.example.registrasisiswa.ui.theme.SurfaceWhite
import com.example.registrasisiswa.viewmodel.EcoBankViewModel
import com.example.registrasisiswa.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    viewModel: EcoBankViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToMemberCard: () -> Unit,
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactionHistory: () -> Unit,
    onNavigateToReward: () -> Unit,
    onNavigateToWasteCatalog: () -> Unit
) {
    val member by viewModel.currentMember.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val isNasabah = userRole == UserRole.NASABAH
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Nasabah") },
            text = { Text("Hapus nasabah ${member?.name}? Semua data riwayat setor akan hilang.") },
            confirmButton = {
                TextButton(onClick = {
                    member?.let { viewModel.deleteMember(it) }
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
                title = { Text("Detail Nasabah", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                actions = {
                    if (isNasabah) {
                        IconButton(onClick = {
                            viewModel.logout()
                            onNavigateBack()
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Keluar", tint = Color.White)
                        }
                    } else {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoseGold)
            )
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        member?.let { m ->
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
                                Brush.verticalGradient(colors = listOf(RoseGold, RoseGoldDark)),
                                RoundedCornerShape(20.dp)
                            )
                            .padding(24.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = m.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(m.name, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color.White)
                            Text(m.formattedId(), fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("${m.points}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                                    Text("Total Poin", fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f))
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = when (m.level()) { "Champion" -> "🏆"; "Aktif" -> "⭐"; else -> "🌱" },
                                        fontSize = 28.sp
                                    )
                                    Text(m.level(), fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f))
                                }
                            }
                        }
                    }
                }

                item { Text("Menu Nasabah", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DarkText) }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ActionMenuCard("💳", "Kartu Nasabah", "Tampilkan kartu digital", Modifier.weight(1f)) {
                                onNavigateToMemberCard()
                            }
                            ActionMenuCard("♻️", "Setor Sampah", "Catat setoran sampah", Modifier.weight(1f)) {
                                onNavigateToAddTransaction()
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ActionMenuCard("📋", "Riwayat Setor", "Lihat semua riwayat", Modifier.weight(1f)) {
                                onNavigateToTransactionHistory()
                            }
                            ActionMenuCard("🎁", "Tukar Reward", "${m.points} poin tersedia", Modifier.weight(1f)) {
                                onNavigateToReward()
                            }
                        }
                        // Katalog Sampah — full width
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onNavigateToWasteCatalog),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SuccessGreenBg),
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
                                            color = SuccessGreen
                                        )
                                        Text(
                                            "Lihat jenis sampah & nilai poinnya",
                                            fontSize = 11.sp,
                                            color = SuccessGreen.copy(alpha = 0.75f)
                                        )
                                    }
                                }
                                Text("›", fontSize = 22.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Info Nasabah", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkText)
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(label = "Email", value = m.email)
                            InfoRow(label = "Nomor HP", value = m.phone)
                            InfoRow(label = "Bergabung", value = m.joinDate)
                        }
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = RoseGold)
        }
    }
}

@Composable
fun ActionMenuCard(emoji: String, title: String, subtitle: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(110.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Column {
                Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DarkText)
                Text(text = subtitle, fontSize = 10.sp, color = MediumText)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, fontSize = 13.sp, color = MediumText)
        Text(text = value, fontSize = 13.sp, color = DarkText, fontWeight = FontWeight.Medium)
    }
}
