package com.example.flowbook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowbook.data.model.Expense
import com.example.flowbook.data.model.ExpenseCategory
import com.example.flowbook.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ExpenseListUiState(
    val expenses: List<Expense> = emptyList(),
    val selectedDate: String = getCurrentDateString(),
    val selectedCategory: ExpenseCategory? = null,
    val groupByCategory: Boolean = false,
    val totalAmount: Double = 0.0,
    val totalCount: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

private fun getCurrentDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date())
}

class ExpenseListViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseListUiState())
    val uiState: StateFlow<ExpenseListUiState> = _uiState.asStateFlow()

    init {
        loadExpenses()
    }

    fun loadExpenses() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            try {
                // Add a small delay to show the loading animation
                kotlinx.coroutines.delay(800)
                
                val currentState = _uiState.value
                val expenses = if (currentState.selectedCategory != null) {
                    expenseRepository.getExpensesByCategory(currentState.selectedCategory)
                } else {
                    expenseRepository.getExpensesByDate(currentState.selectedDate)
                }
                
                expenses.collect { expenseList ->
                    val totalAmount = expenseList.sumOf { it.amount }
                    val totalCount = expenseList.size
                    
                    _uiState.value = currentState.copy(
                        expenses = expenseList,
                        totalAmount = totalAmount,
                        totalCount = totalCount,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to load expenses: ${e.message}"
                )
            }
        }
    }

    fun updateSelectedDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadExpenses()
    }

    fun updateSelectedCategory(category: ExpenseCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        loadExpenses()
    }

    fun toggleGroupByCategory() {
        _uiState.value = _uiState.value.copy(groupByCategory = !_uiState.value.groupByCategory)
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expense)
                loadExpenses()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete expense: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun getGroupedExpenses(): Map<String, List<Expense>> {
        val expenses = _uiState.value.expenses
        return if (_uiState.value.groupByCategory) {
            expenses.groupBy { it.category.displayName }
        } else {
            expenses.groupBy { 
                SimpleDateFormat("dd MMM YYYY HH:mm a", Locale.getDefault()).format(it.createdAt)
            }
        }
    }

}
