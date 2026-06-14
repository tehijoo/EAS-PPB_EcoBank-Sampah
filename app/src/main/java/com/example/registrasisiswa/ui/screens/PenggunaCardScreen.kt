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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.Pengguna
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.data.entity.level
import com.example.registrasisiswa.ui.theme.Black
import com.example.registrasisiswa.ui.theme.GoldLevel
import com.example.registrasisiswa.ui.theme.SilverLevel
import com.example.registrasisiswa.ui.theme.White
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PenggunaCardScreen(
    viewModel: EcoBankViewModel,
    onNavigateBack: () -> Unit
) {
    val pengguna by viewModel.currentPengguna.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kartu Pengguna", color = White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        pengguna?.let { p ->
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
                        .background(MaterialTheme.colorScheme.primary)
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
                                color = White,
                                letterSpacing = 3.sp
                            )
                            Text(
                                "BANK SAMPAH DIGITAL",
                                fontSize = 7.5.sp,
                                color = White.copy(alpha = 0.7f),
                                letterSpacing = 1.5.sp
                            )
                        }
                        Text("🌱", fontSize = 30.sp)
                    }

                    // Center: name
                    Column(modifier = Modifier.align(Alignment.CenterStart)) {
                        Text(
                            text = p.name.uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = White,
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
                            Text("PENGGUNA ID", fontSize = 8.sp, color = White.copy(alpha = 0.65f), letterSpacing = 1.sp)
                            Text(p.formattedId(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Surface(
                                color = when (p.level()) {
                                    "Champion" -> GoldLevel.copy(alpha = 0.9f)
                                    "Aktif" -> SilverLevel.copy(alpha = 0.85f)
                                    else -> Color(0xFF4CAF50).copy(alpha = 0.85f)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = when (p.level()) {
                                        "Champion" -> "🏆 CHAMPION"
                                        "Aktif" -> "⭐ AKTIF"
                                        else -> "🌱 PEMULA"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${p.points} POIN", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = White)
                        }
                    }
                }

                // Info cards
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoCardSmall(Modifier.weight(1f), "⭐", "Total Poin", "${p.points}")
                    InfoCardSmall(
                        Modifier.weight(1f),
                        when (p.level()) { "Champion" -> "🏆"; "Aktif" -> "⭐"; else -> "🌱" },
                        "Level",
                        p.level()
                    )
                    InfoCardSmall(Modifier.weight(1f), "📅", "Bergabung", p.joinDate)
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "🌱 Kumpulkan poin dari setiap setoran sampah\nTukar poin dengan reward menarik!",
                        fontSize = 12.sp,
                        color = Black.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun InfoCardSmall(modifier: Modifier = Modifier, emoji: String, label: String, value: String) {
    Surface(modifier = modifier, shape = RoundedCornerShape(12.dp), color = White, shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = Black)
            Text(text = label, fontSize = 9.sp, color = Black.copy(alpha = 0.5f), textAlign = TextAlign.Center)
        }
    }
}
