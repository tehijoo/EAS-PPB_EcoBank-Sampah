package com.example.registrasisiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.registrasisiswa.data.AppDatabase
import com.example.registrasisiswa.data.repository.EcoBankRepository
import com.example.registrasisiswa.ui.navigation.AppNavGraph
import com.example.registrasisiswa.ui.theme.EcoBankTheme
import com.example.registrasisiswa.viewmodel.EcoBankViewModel
import com.example.registrasisiswa.viewmodel.EcoBankViewModelFactory

class  MainActivity : ComponentActivity() {

    private val viewModel: EcoBankViewModel by viewModels {
        val db = AppDatabase.getDatabase(applicationContext)
        EcoBankViewModelFactory(EcoBankRepository(db.penggunaDao(), db.transactionDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoBankTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
