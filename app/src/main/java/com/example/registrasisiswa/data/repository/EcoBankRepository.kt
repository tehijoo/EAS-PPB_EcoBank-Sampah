package com.example.registrasisiswa.data.repository

import com.example.registrasisiswa.data.dao.MemberDao
import com.example.registrasisiswa.data.dao.TransactionDao
import com.example.registrasisiswa.data.entity.Member
import com.example.registrasisiswa.data.entity.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EcoBankRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {
    fun getAllMembers() = memberDao.getAllMembers()
    fun getMemberById(id: Int) = memberDao.getMemberById(id)
    fun getTotalMembers() = memberDao.getTotalMembers()
    fun getTransactionsByMemberId(memberId: Int) = transactionDao.getTransactionsByMemberId(memberId)

    suspend fun insertMember(member: Member) = memberDao.insertMember(member)
    suspend fun insertMemberAndGetId(member: Member): Int = memberDao.insertMember(member).toInt()
    suspend fun updateMember(member: Member) = memberDao.updateMember(member)
    suspend fun deleteMember(member: Member) = memberDao.deleteMember(member)

    suspend fun addWasteTransaction(
        member: Member,
        wasteType: String,
        weightGrams: Double,
        pointsPerKg: Int
    ) {
        val pointEarned = (weightGrams * pointsPerKg / 1000).toInt()
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("id", "ID")).format(Date())
        transactionDao.insertTransaction(
            Transaction(
                memberId = member.id,
                amount = weightGrams,
                pointEarned = pointEarned,
                date = date,
                type = "SETOR",
                description = wasteType
            )
        )
        memberDao.updateMember(member.copy(points = member.points + pointEarned))
    }

    suspend fun redeemReward(member: Member, pointCost: Int, rewardName: String): Boolean {
        if (member.points < pointCost) return false
        val date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("id", "ID")).format(Date())
        transactionDao.insertTransaction(
            Transaction(
                memberId = member.id,
                amount = 0.0,
                pointEarned = -pointCost,
                date = date,
                type = "REDEEM",
                description = rewardName
            )
        )
        memberDao.updateMember(member.copy(points = member.points - pointCost))
        return true
    }
}
