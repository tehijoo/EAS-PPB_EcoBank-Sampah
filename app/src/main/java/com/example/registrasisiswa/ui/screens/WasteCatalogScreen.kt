package com.example.registrasisiswa.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.ui.model.WASTE_TYPES
import com.example.registrasisiswa.ui.model.WasteType
import com.example.registrasisiswa.ui.theme.Black
import com.example.registrasisiswa.ui.theme.KatalogBg
import com.example.registrasisiswa.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WasteCatalogScreen(onNavigateBack: () -> Unit) {
    val grouped = WASTE_TYPES.groupBy { it.category }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Katalog Sampah", color = Black, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = KatalogBg)
            )
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
                // Header banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            KatalogBg,
                            RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🌱", fontSize = 42.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "Setor Sampah, Dapat Poin!",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = Black
                            )
                            Text(
                                "Sampah kamu bernilai di sini.\nSetor dan kumpulkan poin untuk reward!",
                                fontSize = 12.sp,
                                color = Black.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = KatalogBg.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💡", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Poin dihitung per kilogram (kg) sampah yang disetor",
                            fontSize = 12.sp,
                            color = Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            grouped.forEach { (category, wasteList) ->
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val categoryEmoji = when (category) {
                            "Plastik" -> "🔵"
                            "Kertas" -> "🟡"
                            "Logam" -> "⚪"
                            "Kaca" -> "🟢"
                            "Elektronik" -> "🟠"
                            else -> "⚫"
                        }
                        Text(categoryEmoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Kategori $category",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Black
                        )
                    }
                }

                items(wasteList) { waste ->
                    WasteCatalogItem(waste = waste)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = KatalogBg.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📌 Catatan Penting", fontWeight = FontWeight.Bold, color = Black, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "• Sampah harus bersih dan kering\n" +
                            "• Sortir sampah sesuai jenis\n" +
                            "• Hubungi petugas untuk sampah besar\n" +
                            "• Elektronik dikemas aman sebelum disetor",
                            fontSize = 12.sp,
                            color = Black.copy(alpha = 0.7f),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun WasteCatalogItem(waste: WasteType) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(KatalogBg.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = waste.emoji, fontSize = 28.sp)
                    Text(
                        text = "📷",
                        fontSize = 10.sp,
                        color = Black.copy(alpha = 0.3f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = waste.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Black
                )
                Text(
                    text = waste.description,
                    fontSize = 11.sp,
                    color = Black.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = KatalogBg.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "${waste.pointsPerKg} poin/kg",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Black,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${waste.pointsPerKg}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(text = "poin/kg", fontSize = 9.sp, color = Black.copy(alpha = 0.6f))
            }
        }
    }
}
