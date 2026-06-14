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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.ui.theme.RoseGold
import com.example.registrasisiswa.ui.theme.RoseGoldDark
import com.example.registrasisiswa.ui.theme.RoseGoldDeep
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
            .background(
                Brush.verticalGradient(colors = listOf(RoseGold, RoseGoldDark, RoseGoldDeep))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        ) {
            Surface(
                modifier = Modifier.size(110.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.18f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "♻️", fontSize = 56.sp)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "ECOBANK",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Bank Sampah Digital",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.85f),
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                listOf("♻️", "🌱", "🌍").forEach {
                    Text(it, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = "\"Sampah Jadi Poin, Bumi Jadi Bersih\"",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.55f),
                fontStyle = FontStyle.Italic
            )
        }
    }
}
