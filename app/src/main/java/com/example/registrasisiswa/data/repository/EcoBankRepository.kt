package com.example.registrasisiswa.data.repository

import com.example.registrasisiswa.data.dao.PenggunaDao
import com.example.registrasisiswa.data.dao.TransactionDao
import com.example.registrasisiswa.data.entity.Pengguna
import com.example.registrasisiswa.data.entity.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EcoBankRepository(
    private val penggunaDao: PenggunaDao,
    private val transactionDao: TransactionDao
) {
    fun getAllPengguna() = penggunaDao.getAllPengguna()
    fun getPenggunaById(id: Int) = penggunaDao.getPenggunaById(id)
    fun getTotalPengguna() = penggunaDao.getTotalPengguna()
    fun getTransactionsByPenggunaId(penggunaId: Int) = transactionDao.getTransactionsByPenggunaId(penggunaId)

    suspend fun insertPengguna(pengguna: Pengguna) = penggunaDao.insertPengguna(pengguna)
    suspend fun insertPenggunaAndGetId(pengguna: Pengguna): Int = penggunaDao.insertPengguna(pengguna).toInt()
    suspend fun updatePengguna(pengguna: Pengguna) = penggunaDao.updatePengguna(pengguna)
    suspend fun deletePengguna(pengguna: Pengguna) = penggunaDao.deletePengguna(pengguna)

    suspend fun addWasteTransaction(
        pengguna: Pengguna,
        wasteType: String,
        weightGrams: Double,
        pointsPerKg: Int
    ) {
        val pointEarned = (weightGrams * pointsPerKg / 1000).toInt()
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("id", "ID")).format(Date())
        transactionDao.insertTransaction(
            Transaction(
                penggunaId = pengguna.id,
                amount = weightGrams,
                pointEarned = pointEarned,
                date = date,
                type = "SETOR",
                description = wasteType
            )
        )
        penggunaDao.updatePengguna(pengguna.copy(points = pengguna.points + pointEarned))
    }

    suspend fun redeemReward(pengguna: Pengguna, pointCost: Int, rewardName: String): Boolean {
        if (pengguna.points < pointCost) return false
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("id", "ID")).format(Date())
        transactionDao.insertTransaction(
            Transaction(
                penggunaId = pengguna.id,
                amount = 0.0,
                pointEarned = -pointCost,
                date = date,
                type = "REDEEM",
                description = rewardName
            )
        )
        penggunaDao.updatePengguna(pengguna.copy(points = pengguna.points - pointCost))
        return true
    }
}
