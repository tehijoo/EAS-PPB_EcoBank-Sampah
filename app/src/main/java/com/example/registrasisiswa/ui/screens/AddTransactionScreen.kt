package com.example.registrasisiswa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasisiswa.data.entity.formattedId
import com.example.registrasisiswa.ui.model.WASTE_TYPES
import com.example.registrasisiswa.ui.model.WasteType
import com.example.registrasisiswa.ui.theme.BackgroundCream
import com.example.registrasisiswa.ui.theme.DarkText
import com.example.registrasisiswa.ui.theme.MediumText
import com.example.registrasisiswa.ui.theme.RoseGold
import com.example.registrasisiswa.ui.theme.RoseGoldDark
import com.example.registrasisiswa.ui.theme.RoseGoldLight
import com.example.registrasisiswa.ui.theme.SuccessGreen
import com.example.registrasisiswa.ui.theme.SuccessGreenBg
import com.example.registrasisiswa.ui.theme.SurfaceWhite
import com.example.registrasisiswa.viewmodel.EcoBankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: EcoBankViewModel,
    onNavigateBack: () -> Unit
) {
    val member by viewModel.currentMember.collectAsState()
    var selectedWaste by remember { mutableStateOf<WasteType?>(null) }
    var weightText by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf("") }

    val weightGrams = weightText.toDoubleOrNull() ?: 0.0
    val pointsEarned = selectedWaste?.let { (weightGrams * it.pointsPerKg / 1000).toInt() } ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setor Sampah", color = Color.White, fontWeight = FontWeight.Bold) },
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Member info
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = RoseGoldLight
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("♻️", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(m.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RoseGoldDark)
                            Text(m.formattedId(), fontSize = 12.sp, color = RoseGoldDark.copy(alpha = 0.7f))
                            Text("Poin saat ini: ${m.points}", fontSize = 12.sp, color = RoseGoldDark)
                        }
                    }
                }

                // Step 1: Pilih jenis sampah
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceWhite,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = RoseGold,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    "1",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pilih Jenis Sampah", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkText)
                        }

                        if (selectedWaste != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(color = SuccessGreenBg, shape = RoundedCornerShape(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedWaste!!.emoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(selectedWaste!!.name, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                        Text("${selectedWaste!!.pointsPerKg} poin/kg", fontSize = 11.sp, color = SuccessGreen)
                                    }
                                    Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Ketuk untuk ganti",
                                fontSize = 11.sp,
                                color = MediumText,
                                modifier = Modifier.clickable { selectedWaste = null }
                            )
                        } else {
                            Spacer(modifier = Modifier.height(12.dp))
                            // Grid 2 kolom untuk jenis sampah
                            // Tidak bisa pakai LazyVerticalGrid di dalam scroll, jadi kita buat manual grid
                            val chunked = WASTE_TYPES.chunked(2)
                            chunked.forEach { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    row.forEach { waste ->
                                        WasteTypeCard(
                                            waste = waste,
                                            isSelected = selectedWaste == waste,
                                            modifier = Modifier.weight(1f),
                                            onClick = { selectedWaste = waste }
                                        )
                                    }
                                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                // Step 2: Input berat (hanya muncul setelah pilih jenis)
                if (selectedWaste != null) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceWhite,
                        shadowElevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = RoseGold, shape = RoundedCornerShape(50)) {
                                    Text("2", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Masukkan Berat Sampah", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkText)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = weightText,
                                onValueChange = { weightText = it.filter { c -> c.isDigit() || c == '.' }; weightError = "" },
                                label = { Text("Berat (gram)") },
                                placeholder = { Text("Contoh: 500") },
                                suffix = { Text("gram", color = MediumText) },
                                isError = weightError.isNotEmpty(),
                                supportingText = {
                                    if (weightError.isNotEmpty()) Text(weightError, color = Color.Red)
                                    else Text("Masukkan berat dalam satuan gram", color = MediumText, fontSize = 11.sp)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = RoseGold,
                                    focusedLabelColor = RoseGold,
                                    cursorColor = RoseGold
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = RoseGoldLight)
                            Spacer(modifier = Modifier.height(10.dp))

                            // Formula hint
                            Surface(color = BackgroundCream, shape = RoundedCornerShape(10.dp)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("📌 Rumus Poin", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MediumText)
                                    Text("Berat (g) × ${selectedWaste!!.pointsPerKg} ÷ 1000 = Poin", fontSize = 11.sp, color = MediumText)
                                    Text("Contoh: 500g × ${selectedWaste!!.pointsPerKg} ÷ 1000 = ${(500 * selectedWaste!!.pointsPerKg / 1000)} poin",
                                        fontSize = 11.sp, color = MediumText)
                                }
                            }
                        }
                    }

                    // Preview poin
                    if (weightGrams > 0 && pointsEarned > 0) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = SuccessGreenBg
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Poin yang Didapat", fontSize = 13.sp, color = SuccessGreen)
                                    Text(
                                        "${weightGrams.toInt()} gram ${selectedWaste!!.name}",
                                        fontSize = 11.sp,
                                        color = SuccessGreen.copy(alpha = 0.75f)
                                    )
                                }
                                Text("+$pointsEarned", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            when {
                                weightGrams <= 0 -> weightError = "Masukkan berat sampah"
                                weightGrams < 100 -> weightError = "Berat minimal 100 gram"
                                pointsEarned <= 0 -> weightError = "Berat terlalu kecil untuk mendapat poin"
                                else -> {
                                    viewModel.addTransaction(m, selectedWaste!!.name, weightGrams, selectedWaste!!.pointsPerKg)
                                    onNavigateBack()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RoseGoldDark)
                    ) {
                        Text("♻️  Simpan Setoran", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = RoseGold)
        }
    }
}

@Composable
fun WasteTypeCard(
    waste: WasteType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) RoseGold else Color.Transparent
    val bgColor = if (isSelected) RoseGoldLight else BackgroundCream

    Surface(
        modifier = modifier
            .heightIn(min = 110.dp)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(waste.emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                waste.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) RoseGoldDark else DarkText,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 2,
                lineHeight = 14.sp
            )
            Spacer(modifier = Modifier.height(3.dp))
            Surface(
                color = if (isSelected) RoseGold.copy(alpha = 0.15f) else MediumText.copy(alpha = 0.1f),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    "${waste.pointsPerKg} poin/kg",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) RoseGoldDark else MediumText,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
