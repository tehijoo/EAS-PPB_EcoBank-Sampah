package com.example.registrasisiswa.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.ui.theme.DashboardBg
import com.example.registrasisiswa.ui.theme.PenggunaCardBg
import com.example.registrasisiswa.ui.theme.PengelolaCardBg
import com.example.registrasisiswa.ui.theme.White
import com.example.registrasisiswa.ui.theme.Black

@Composable
fun RoleSelectionScreen(
    onNavigateToAdminLogin: () -> Unit,
    onNavigateToPenggunaLogin: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(600),
        label = "fade"
    )
    val slideY by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = tween(600),
        label = "slide"
    )

    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DashboardBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha)
                .scale(slideY)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                "ECOBANK",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = White,
                letterSpacing = 4.sp
            )
            Text(
                "Bank Sampah Digital",
                fontSize = 13.sp,
                color = White.copy(alpha = 0.8f),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Sampah Jadi Poin, Bumi Jadi Bersih",
                fontSize = 12.sp,
                color = White.copy(alpha = 0.65f),
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                "Masuk sebagai",
                fontSize = 14.sp,
                color = White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Pengelola card
            RoleCard(
                emoji = "🏢",
                title = "Pengelola",
                subtitle = "Kelola semua pengguna & transaksi",
                backgroundColor = PengelolaCardBg,
                titleColor = Black,
                subtitleColor = Black.copy(alpha = 0.7f),
                onClick = onNavigateToAdminLogin
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pengguna card
            RoleCard(
                emoji = "👤",
                title = "Pengguna",
                subtitle = "Lihat poin & riwayat setor saya",
                backgroundColor = PenggunaCardBg,
                titleColor = Black,
                subtitleColor = Black.copy(alpha = 0.7f),
                onClick = onNavigateToPenggunaLogin
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                "v1.0  •  EcoBank © 2025",
                fontSize = 11.sp,
                color = White.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun RoleCard(
    emoji: String,
    title: String,
    subtitle: String,
    backgroundColor: Color,
    titleColor: Color,
    subtitleColor: Color,
    onClick: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "press"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                pressed = true
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(titleColor.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = titleColor)
                Text(subtitle, fontSize = 12.sp, color = subtitleColor)
            }
            Text("›", fontSize = 24.sp, color = titleColor.copy(alpha = 0.5f), fontWeight = FontWeight.Bold)
        }
    }
}
