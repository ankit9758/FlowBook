package com.example.flowbook.data.repository

import com.example.flowbook.data.database.ExpenseDao
import com.example.flowbook.data.model.Expense
import com.example.flowbook.data.model.ExpenseCategory
import com.example.flowbook.data.model.DailyExpenseSummary
import com.example.flowbook.data.model.CategoryExpenseSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    
    fun getAllExpenses(): Flow<List<Expense>> = expenseDao.getAllExpenses()
    
    fun getTodayExpenses(): Flow<List<Expense>> = expenseDao.getTodayExpenses()
    
    fun getExpensesByDate(date: String): Flow<List<Expense>> = expenseDao.getExpensesByDate(date)
    
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<Expense>> = 
        expenseDao.getExpensesByCategory(category)
    
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> = 
        expenseDao.getExpensesByDateRange(startDate, endDate)
    
    suspend fun getTodayTotal(): Double = expenseDao.getTodayTotal() ?: 0.0
    
    suspend fun getTotalByDate(date: String): Double = expenseDao.getTotalByDate(date) ?: 0.0
    
    suspend fun getTodayCount(): Int = expenseDao.getTodayCount()
    
    suspend fun getCountByDate(date: String): Int = expenseDao.getCountByDate(date)
    
    suspend fun insertExpense(expense: Expense): Long = expenseDao.insertExpense(expense)
    
    suspend fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)
    
    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)
    
    suspend fun deleteExpenseById(expenseId: Long) = expenseDao.deleteExpenseById(expenseId)
    
    fun getLast7DaysExpenses(): Flow<List<DailyExpenseSummary>> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        
        return getAllExpenses().map { expenses ->
            val last7Days = mutableListOf<DailyExpenseSummary>()
            
            repeat(7) { i ->
                calendar.time = Date()
                calendar.add(Calendar.DAY_OF_YEAR, -i)
                val date = dateFormat.format(calendar.time)
                
                val dayExpenses = expenses.filter { 
                    dateFormat.format(it.createdAt) == date 
                }
                
                last7Days.add(
                    DailyExpenseSummary(
                        date = date,
                        totalAmount = dayExpenses.sumOf { it.amount },
                        expenseCount = dayExpenses.size,
                        expenses = dayExpenses
                    )
                )
            }
            
            last7Days.reversed().filter { it.expenseCount > 0 }
        }
    }
    
    fun getCategorySummary(): Flow<List<CategoryExpenseSummary>> {
        return getAllExpenses().map { expenses ->
            val totalAmount = expenses.sumOf { it.amount }
            
            ExpenseCategory.values().map { category ->
                val categoryExpenses = expenses.filter { it.category == category }
                val categoryTotal = categoryExpenses.sumOf { it.amount }
                val percentage = if (totalAmount > 0) (categoryTotal / totalAmount) * 100 else 0.0
                
                CategoryExpenseSummary(
                    category = category,
                    totalAmount = categoryTotal,
                    expenseCount = categoryExpenses.size,
                    percentage = percentage
                )
            }.filter { it.totalAmount > 0 }
        }
    }
}
