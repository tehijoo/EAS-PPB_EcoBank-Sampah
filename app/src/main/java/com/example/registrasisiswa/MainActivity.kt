package com.example.registrasisiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.registrasisiswa.data.AppDatabase
import com.example.registrasisiswa.ui.theme.MainScreen
import com.example.registrasisiswa.ui.theme.RegistrasiSiswaTheme
import com.example.registrasisiswa.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = AppDatabase.getDatabase(applicationContext).siswaDao()
        val viewModel = StudentViewModel(dao)

        setContent {
            RegistrasiSiswaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}