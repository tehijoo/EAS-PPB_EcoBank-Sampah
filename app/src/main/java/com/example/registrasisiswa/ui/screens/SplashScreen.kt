package com.example.registrasisiswa.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.ui.theme.White
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "alpha"
    )
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.4f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2800)
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {
            Icon(
                imageVector = Icons.Default.Recycling,
                contentDescription = "Recycle",
                tint = Color(0xFF1E3D10),
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ECOBANK",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = White,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Bank Sampah Digital",
                fontSize = 15.sp,
                color = White.copy(alpha = 0.85f),
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                listOf("🌱", "🌍").forEach {
                    Text(it, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = "\"Sampah Jadi Poin, Bumi Jadi Bersih\"",
                fontSize = 11.sp,
                color = White.copy(alpha = 0.55f),
                fontStyle = FontStyle.Italic
            )
        }
    }
}
