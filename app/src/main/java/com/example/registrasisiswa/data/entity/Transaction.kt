package com.example.registrasisiswa.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val memberId: Int,
    val amount: Double,        // berat dalam gram untuk SETOR, 0 untuk REDEEM
    val pointEarned: Int,
    val date: String,
    val type: String = "SETOR", // SETOR atau REDEEM
    val description: String = "" // jenis sampah untuk SETOR, nama reward untuk REDEEM
)
