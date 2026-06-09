package com.example.registrasisiswa.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.registrasisiswa.viewmodel.StudentViewModel

@Composable
fun MainScreen(viewModel: StudentViewModel) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val siswaList by viewModel.siswaList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Registrasi Siswa", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Kelola data siswa", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(20.dp))

        FormInput(
            nama = nama,
            email = email,
            onNamaChange = { nama = it },
            onEmailChange = { email = it },
            onTambahClick = {
                // Validasi input sederhana
                if (nama.isBlank() || email.isBlank() || !email.contains("@")) {
                    return@FormInput
                }
                viewModel.tambahSiswa(nama, email)
                nama = ""
                email = ""
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (siswaList.isEmpty()) {
            Text(text = "Belum ada data siswa")
        }

        LazyColumn {
            items(siswaList) { siswa ->
                StudentItem(
                    siswa = siswa,
                    onDelete = { viewModel.hapusSiswa(siswa) },
                    onEdit = {
                        // Fitur Edit sederhana: menambahkan kata "Updated" pada entri data yang dipilih
                        viewModel.editSiswa(
                            siswa.copy(nama = siswa.nama + " Updated")
                        )
                    }
                )
            }
        }
    }
}