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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.Pengguna
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.data.entity.level
import com.example.registrasisiswa.ui.theme.Black
import com.example.registrasisiswa.ui.theme.BronzeLevel
import com.example.registrasisiswa.ui.theme.BronzeLevelBg
import com.example.registrasisiswa.ui.theme.GoldLevel
import com.example.registrasisiswa.ui.theme.GoldLevelBg
import com.example.registrasisiswa.ui.theme.KatalogBg
import com.example.registrasisiswa.ui.theme.SilverLevel
import com.example.registrasisiswa.ui.theme.SilverLevelBg
import com.example.registrasisiswa.ui.theme.White
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: EcoBankViewModel,
    onNavigateToAddPengguna: () -> Unit,
    onNavigateToWasteCatalog: () -> Unit,
    onNavigateToPenggunaDetail: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val penggunaList by viewModel.allPengguna.collectAsState()
    val totalPengguna by viewModel.totalPengguna.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "EcoBank",
                            fontWeight = FontWeight.ExtraBold,
                            color = White,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Bank Sampah Digital",
                            fontSize = 11.sp,
                            color = White.copy(alpha = 0.8f),
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Keluar", tint = White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddPengguna,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Pengguna")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Stats card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Pengguna", fontSize = 13.sp, color = White.copy(alpha = 0.8f))
                            Text(
                                text = "$totalPengguna",
                                fontSize = 44.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = White
                            )
                            Text("Pengguna Aktif", fontSize = 11.sp, color = White.copy(alpha = 0.65f))
                        }
                        Text(text = "👥", fontSize = 52.sp)
                    }
                }
            }

            item {
                // Katalog Sampah card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToWasteCatalog),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = KatalogBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(
                                    text = "Katalog Sampah",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Black
                                )
                                Text(
                                    text = "Lihat jenis sampah & nilai poinnya",
                                    fontSize = 12.sp,
                                    color = Black.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Black
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daftar Pengguna",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(text = "${penggunaList.size} pengguna", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            }

            if (penggunaList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🌱", fontSize = 52.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Belum ada pengguna", color = Black, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text("Tap tombol + untuk mendaftarkan pengguna", color = Black.copy(alpha = 0.5f), fontSize = 12.sp)
                        }
                    }
                }
            } else {
                items(penggunaList) { pengguna ->
                    PenggunaListItem(
                        pengguna = pengguna,
                        onClick = { onNavigateToPenggunaDetail(pengguna.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }
}

@Composable
fun PenggunaListItem(pengguna: Pengguna, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pengguna.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = pengguna.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Black)
                Text(text = pengguna.formattedId(), fontSize = 11.sp, color = Black.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(5.dp))
                LevelBadge(level = pengguna.level())
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${pengguna.points}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                Text(text = "poin", fontSize = 11.sp, color = Black.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun LevelBadge(level: String) {
    val (bg, textColor, emoji) = when (level) {
        "Champion" -> Triple(GoldLevelBg, GoldLevel, "🏆")
        "Aktif" -> Triple(SilverLevelBg, SilverLevel, "⭐")
        else -> Triple(BronzeLevelBg, BronzeLevel, "🌱")
    }
    Surface(color = bg, shape = RoundedCornerShape(50)) {
        Text(
            text = "$emoji $level",
            fontSize = 11.sp,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
        )
    }
}
