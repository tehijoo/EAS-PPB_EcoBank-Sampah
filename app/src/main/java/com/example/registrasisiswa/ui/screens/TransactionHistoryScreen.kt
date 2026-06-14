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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.Transaction
import com.example.registrasisiswa.ui.theme.Black
import com.example.registrasisiswa.ui.theme.ErrorRed
import com.example.registrasisiswa.ui.theme.ErrorRedBg
import com.example.registrasisiswa.ui.theme.SuccessGreen
import com.example.registrasisiswa.ui.theme.SuccessGreenBg
import com.example.registrasisiswa.ui.theme.White
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    viewModel: EcoBankViewModel,
    onNavigateBack: () -> Unit
) {
    val transactions by viewModel.currentTransactions.collectAsState()
    val pengguna by viewModel.currentPengguna.collectAsState()

    val totalSetor = transactions.count { it.type == "SETOR" }
    val totalKg = transactions.filter { it.type == "SETOR" }.sumOf { it.amount } / 1000.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Setor", color = White, fontWeight = FontWeight.Bold) },
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
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Summary header
            pengguna?.let { p ->
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${p.points}", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                            Text("Total Poin", fontSize = 11.sp, color = Black.copy(alpha = 0.6f))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$totalSetor", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                            Text("Kali Setor", fontSize = 11.sp, color = Black.copy(alpha = 0.6f))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("%.1f".format(totalKg), fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                            Text("Total (kg)", fontSize = 11.sp, color = Black.copy(alpha = 0.6f))
                        }
                    }
                }
            }

            if (transactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🌱", fontSize = 52.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Belum ada riwayat setor", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Black)
                        Text("Mulai setor sampah dan kumpulkan poin!", fontSize = 12.sp, color = Black.copy(alpha = 0.5f))
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionItem(transaction = transaction)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val isSetor = transaction.type == "SETOR"
    val pointColor = if (isSetor) SuccessGreen else ErrorRed
    val pointBg = if (isSetor) SuccessGreenBg else ErrorRedBg
    val pointText = if (isSetor) "+${transaction.pointEarned}" else "${transaction.pointEarned}"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (isSetor) "🌱" else "🎁", fontSize = 28.sp)
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = if (isSetor && transaction.description.isNotEmpty())
                            transaction.description else if (isSetor) "Setor Sampah" else "Redeem Reward",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Black
                    )
                    if (isSetor && transaction.amount > 0) {
                        Text(
                            text = "${transaction.amount.toInt()} gram",
                            fontSize = 12.sp,
                            color = Black.copy(alpha = 0.6f)
                        )
                    }
                    if (!isSetor && transaction.description.isNotEmpty()) {
                        Text(transaction.description, fontSize = 12.sp, color = Black.copy(alpha = 0.6f))
                    }
                    Text(text = transaction.date, fontSize = 11.sp, color = Black.copy(alpha = 0.4f))
                }
            }

            Surface(color = pointBg, shape = RoundedCornerShape(8.dp)) {
                Text(
                    text = "$pointText poin",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = pointColor,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}
