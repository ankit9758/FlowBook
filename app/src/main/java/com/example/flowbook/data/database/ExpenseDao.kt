package com.example.flowbook.data.database

import androidx.room.*
import com.example.flowbook.data.model.Expense
import com.example.flowbook.data.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY createdAt DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date(createdAt/1000, 'unixepoch') = date('now') ORDER BY createdAt DESC")
    fun getTodayExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date(createdAt/1000, 'unixepoch') = :date ORDER BY createdAt DESC")
    fun getExpensesByDate(date: String): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY createdAt DESC")
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE createdAt >= :startDate AND createdAt <= :endDate ORDER BY createdAt DESC")
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date(createdAt/1000, 'unixepoch') = date('now')")
    suspend fun getTodayTotal(): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE date(createdAt/1000, 'unixepoch') = :date")
    suspend fun getTotalByDate(date: String): Double?

    @Query("SELECT COUNT(*) FROM expenses WHERE date(createdAt/1000, 'unixepoch') = date('now')")
    suspend fun getTodayCount(): Int

    @Query("SELECT COUNT(*) FROM expenses WHERE date(createdAt/1000, 'unixepoch') = :date")
    suspend fun getCountByDate(date: String): Int

    @Insert
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)
}
