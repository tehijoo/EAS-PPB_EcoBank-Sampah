package com.example.registrasisiswa.data.dao

import androidx.room.*
import com.example.registrasisiswa.data.entity.Member
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Update
    suspend fun updateMember(member: Member)

    @Delete
    suspend fun deleteMember(member: Member)

    @Query("SELECT * FROM members ORDER BY id DESC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: Int): Flow<Member?>

    @Query("SELECT COUNT(*) FROM members")
    fun getTotalMembers(): Flow<Int>
}
