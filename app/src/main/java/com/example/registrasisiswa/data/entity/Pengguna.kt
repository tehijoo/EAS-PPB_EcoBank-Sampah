package com.example.registrasisiswa.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengguna")
data class Pengguna(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val phone: String,
    val points: Int = 0,
    val joinDate: String = ""
)

fun Pengguna.formattedId(): String = "PGN" + id.toString().padStart(5, '0')

fun Pengguna.level(): String = when {
    points >= 500 -> "Champion"
    points >= 200 -> "Aktif"
    else -> "Pemula"
}
