package com.example.registrasisiswa.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.registrasisiswa.data.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE penggunaId = :penggunaId ORDER BY id DESC")
    fun getTransactionsByPenggunaId(penggunaId: Int): Flow<List<Transaction>>
}
