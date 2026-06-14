package com.example.registrasisiswa.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.ui.model.ECO_REWARDS
import com.example.registrasisiswa.ui.model.EcoReward
import com.example.registrasisiswa.ui.theme.Black
import com.example.registrasisiswa.ui.theme.ErrorRed
import com.example.registrasisiswa.ui.theme.GoldLevel
import com.example.registrasisiswa.ui.theme.GoldLevelBg
import com.example.registrasisiswa.ui.theme.SuccessGreen
import com.example.registrasisiswa.ui.theme.SuccessGreenBg
import com.example.registrasisiswa.ui.theme.White
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    viewModel: EcoBankViewModel,
    onNavigateBack: () -> Unit
) {
    val pengguna by viewModel.currentPengguna.collectAsState()
    val uiMessage by viewModel.uiMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedReward by remember { mutableStateOf<EcoReward?>(null) }

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    if (selectedReward != null) {
        AlertDialog(
            onDismissRequest = { selectedReward = null },
            title = { Text("Konfirmasi Penukaran") },
            text = {
                Column {
                    Text("${selectedReward!!.emoji}  ${selectedReward!!.name}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Tukar ${selectedReward!!.pointCost} poin dengan ${selectedReward!!.name}?", color = Black.copy(alpha = 0.7f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    pengguna?.let {
                        val remaining = it.points - selectedReward!!.pointCost
                        Text(
                            "Poin tersisa: $remaining",
                            fontSize = 13.sp,
                            color = if (remaining >= 0) SuccessGreen else ErrorRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        pengguna?.let { p ->
                            viewModel.redeemReward(p, selectedReward!!.name, selectedReward!!.pointCost)
                        }
                        selectedReward = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ya, Tukar!", color = White)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedReward = null }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tukar Reward", color = White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        pengguna?.let { p ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Points balance card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("⭐", fontSize = 36.sp)
                            Text("${p.points}", fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = White)
                            Text("Poin Tersedia", fontSize = 14.sp, color = White.copy(alpha = 0.85f))
                        }
                    }
                }

                // Rumah Tangga section
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🏠", fontSize = 16.sp)
                        Text(
                            "  Produk Rumah Tangga",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Black
                        )
                    }
                }

                items(ECO_REWARDS.filter { it.category == "Rumah Tangga" }) { reward ->
                    EcoRewardCard(reward = reward, penggunaPoints = p.points, onRedeem = { selectedReward = reward })
                }

                // E-Money section
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💳", fontSize = 16.sp)
                        Text("  E-Money / Saldo Digital", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Black)
                    }
                }

                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = GoldLevelBg
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("💡", fontSize = 16.sp)
                            Text(
                                "  500 poin = Rp500 (berlaku kelipatan)",
                                fontSize = 12.sp,
                                color = GoldLevel,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                items(ECO_REWARDS.filter { it.category == "E-Money" }) { reward ->
                    EcoRewardCard(reward = reward, penggunaPoints = p.points, onRedeem = { selectedReward = reward })
                }

                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            "🎁 Reward tersedia sesuai stok. Hubungi petugas untuk konfirmasi pengambilan.",
                            fontSize = 12.sp,
                            color = Black.copy(alpha = 0.7f),
                            modifier = Modifier.padding(14.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun EcoRewardCard(reward: EcoReward, penggunaPoints: Int, onRedeem: () -> Unit) {
    val canRedeem = penggunaPoints >= reward.pointCost

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.padding(end = 12.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (canRedeem) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f) else Black.copy(alpha = 0.05f)
            ) {
                Text(reward.emoji, fontSize = 30.sp, modifier = Modifier.padding(10.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(reward.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Black)
                Text(reward.description, fontSize = 11.sp, color = Black.copy(alpha = 0.6f))
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = if (canRedeem) SuccessGreenBg else Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        "${reward.pointCost} poin",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (canRedeem) SuccessGreen else Black.copy(alpha = 0.4f),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Button(
                onClick = onRedeem,
                enabled = canRedeem,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Black.copy(alpha = 0.1f)
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (canRedeem) "Tukar" else "Kurang",
                    fontSize = 12.sp,
                    color = if (canRedeem) White else Black.copy(alpha = 0.4f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
