package com.example.registrasisiswa.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    nama: String,
    email: String,
    onNamaChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onTambahClick: () -> Unit
) {
    // Definisi Warna
    val Brown40 = Color(0xFF5D4037)
    val Cream40 = Color(0xFFFEF5E7)

    Column {
        // Kotak Input Nama
        OutlinedTextField(
            value = nama,
            onValueChange = onNamaChange,
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Brown40,
                unfocusedBorderColor = Brown40,
                focusedLabelColor = Brown40,
                cursorColor = Brown40,
                focusedContainerColor = Cream40,
                unfocusedContainerColor = Cream40
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Kotak Input Email
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Brown40,
                unfocusedBorderColor = Brown40,
                focusedLabelColor = Brown40,
                cursorColor = Brown40,
                focusedContainerColor = Cream40,
                unfocusedContainerColor = Cream40
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Tambah Siswa (Cokelat Solid)
        Button(
            onClick = onTambahClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Brown40,
                contentColor = Color.White
            )
        ) {
            Text("Tambah Siswa", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}