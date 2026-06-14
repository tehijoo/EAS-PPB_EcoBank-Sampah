package com.example.registrasisiswa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.data.entity.level
import com.example.registrasisiswa.ui.theme.BackgroundCream
import com.example.registrasisiswa.ui.theme.GoldLevel
import com.example.registrasisiswa.ui.theme.MediumText
import com.example.registrasisiswa.ui.theme.RoseGold
import com.example.registrasisiswa.ui.theme.RoseGoldDark
import com.example.registrasisiswa.ui.theme.RoseGoldDeep
import com.example.registrasisiswa.ui.theme.SilverLevel
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCardScreen(
    viewModel: EcoBankViewModel,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.currentMember.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kartu Nasabah", color = Color.White, fontWeight = FontWeight.Bold) },
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
        member?.let { m ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // Digital Membership Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(RoseGold, RoseGoldDark, RoseGoldDeep, Color(0xFF3A1F28))
                            )
                        )
                        .padding(24.dp)
                ) {
                    // Top: brand
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "ECOBANK",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 3.sp
                            )
                            Text(
                                "BANK SAMPAH DIGITAL",
                                fontSize = 7.5.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                letterSpacing = 1.5.sp
                            )
                        }
                        Text("♻️", fontSize = 30.sp)
                    }

                    // Center: name
                    Column(modifier = Modifier.align(Alignment.CenterStart)) {
                        Text(
                            text = m.name.uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }

                    // Bottom: ID + level + points
                    Row(
                        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text("NASABAH ID", fontSize = 8.sp, color = Color.White.copy(alpha = 0.65f), letterSpacing = 1.sp)
                            Text(m.formattedId(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Surface(
                                color = when (m.level()) {
                                    "Champion" -> GoldLevel.copy(alpha = 0.9f)
                                    "Aktif" -> SilverLevel.copy(alpha = 0.85f)
                                    else -> Color(0xFF4CAF50).copy(alpha = 0.85f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = when (m.level()) {
                                        "Champion" -> "🏆 CHAMPION"
                                        "Aktif" -> "⭐ AKTIF"
                                        else -> "🌱 PEMULA"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${m.points} POIN", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                        }
                    }
                }

                // Info cards
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoCardSmall(Modifier.weight(1f), "⭐", "Total Poin", "${m.points}")
                    InfoCardSmall(
                        Modifier.weight(1f),
                        when (m.level()) { "Champion" -> "🏆"; "Aktif" -> "⭐"; else -> "🌱" },
                        "Level",
                        m.level()
                    )
                    InfoCardSmall(Modifier.weight(1f), "📅", "Bergabung", m.joinDate)
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = RoseGold.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "♻️ Kumpulkan poin dari setiap setoran sampah\nTukar poin dengan reward menarik!",
                        fontSize = 12.sp,
                        color = RoseGoldDark,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = RoseGold)
        }
    }
}

@Composable
fun InfoCardSmall(modifier: Modifier = Modifier, emoji: String, label: String, value: String) {
    Surface(modifier = modifier, shape = RoundedCornerShape(12.dp), color = Color.White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = RoseGoldDark)
            Text(text = label, fontSize = 9.sp, color = MediumText, textAlign = TextAlign.Center)
        }
    }
}
