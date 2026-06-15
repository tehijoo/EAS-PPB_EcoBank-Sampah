package com.example.registrasisiswa.data.dao

import androidx.room.*
import com.example.registrasisiswa.data.entity.Pengguna
import kotlinx.coroutines.flow.Flow

@Dao
interface PenggunaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPengguna(pengguna: Pengguna): Long

    @Update
    suspend fun updatePengguna(pengguna: Pengguna)

    @Delete
    suspend fun deletePengguna(pengguna: Pengguna)

    @Query("SELECT * FROM pengguna ORDER BY id DESC")
    fun getAllPengguna(): Flow<List<Pengguna>>

    @Query("SELECT * FROM pengguna WHERE id = :id")
    fun getPenggunaById(id: Int): Flow<Pengguna?>

    @Query("SELECT COUNT(*) FROM pengguna")
    fun getTotalPengguna(): Flow<Int>
}
