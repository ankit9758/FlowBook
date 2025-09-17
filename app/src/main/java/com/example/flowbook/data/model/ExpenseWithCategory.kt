package com.example.flowbook.data.model

data class ExpenseWithCategory(
    val expense: Expense,
    val category: ExpenseCategory
)

data class DailyExpenseSummary(
    val date: String,
    val totalAmount: Double,
    val expenseCount: Int,
    val expenses: List<Expense>
)

data class CategoryExpenseSummary(
    val category: ExpenseCategory,
    val totalAmount: Double,
    val expenseCount: Int,
    val percentage: Double
)
