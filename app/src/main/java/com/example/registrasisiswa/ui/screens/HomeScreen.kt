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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.Member
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.data.entity.level
import com.example.registrasisiswa.ui.theme.BackgroundCream
import com.example.registrasisiswa.ui.theme.BronzeLevel
import com.example.registrasisiswa.ui.theme.BronzeLevelBg
import com.example.registrasisiswa.ui.theme.DarkText
import com.example.registrasisiswa.ui.theme.GoldLevel
import com.example.registrasisiswa.ui.theme.GoldLevelBg
import com.example.registrasisiswa.ui.theme.MediumText
import com.example.registrasisiswa.ui.theme.RoseGold
import com.example.registrasisiswa.ui.theme.RoseGoldDark
import com.example.registrasisiswa.ui.theme.RoseGoldLight
import com.example.registrasisiswa.ui.theme.SilverLevel
import com.example.registrasisiswa.ui.theme.SilverLevelBg
import com.example.registrasisiswa.ui.theme.SuccessGreen
import com.example.registrasisiswa.ui.theme.SuccessGreenBg
import com.example.registrasisiswa.ui.theme.SurfaceWhite
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: EcoBankViewModel,
    onNavigateToAddMember: () -> Unit,
    onNavigateToWasteCatalog: () -> Unit,
    onNavigateToMemberDetail: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val members by viewModel.allMembers.collectAsState()
    val totalMembers by viewModel.totalMembers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "EcoBank",
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Bank Sampah Digital",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RoseGold),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Keluar", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddMember,
                containerColor = RoseGold,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Nasabah")
            }
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Stats gradient card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(colors = listOf(RoseGold, RoseGoldDark)),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Nasabah", fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(
                                text = "$totalMembers",
                                fontSize = 44.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text("Nasabah Aktif", fontSize = 11.sp, color = Color.White.copy(alpha = 0.65f))
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
                    colors = CardDefaults.cardColors(containerColor = SuccessGreenBg),
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
                            Text(text = "♻️", fontSize = 32.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Katalog Sampah",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = SuccessGreen
                                )
                                Text(
                                    text = "Lihat jenis sampah & nilai poinnya",
                                    fontSize = 12.sp,
                                    color = SuccessGreen.copy(alpha = 0.75f)
                                )
                            }
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = SuccessGreen
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
                        text = "Daftar Nasabah",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(text = "${members.size} nasabah", fontSize = 12.sp, color = MediumText)
                }
            }

            if (members.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "♻️", fontSize = 52.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Belum ada nasabah", color = MediumText, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Text("Tap tombol + untuk mendaftarkan nasabah", color = MediumText.copy(alpha = 0.65f), fontSize = 12.sp)
                        }
                    }
                }
            } else {
                items(members) { member ->
                    MemberListItem(
                        member = member,
                        onClick = { onNavigateToMemberDetail(member.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }
}

@Composable
fun MemberListItem(member: Member, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
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
                    .background(RoseGoldLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = RoseGoldDark
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = member.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
                Text(text = member.formattedId(), fontSize = 11.sp, color = MediumText)
                Spacer(modifier = Modifier.height(5.dp))
                LevelBadge(level = member.level())
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${member.points}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = RoseGold)
                Text(text = "poin", fontSize = 11.sp, color = MediumText)
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
