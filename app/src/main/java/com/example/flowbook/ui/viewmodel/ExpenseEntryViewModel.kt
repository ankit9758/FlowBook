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

data class ExpenseEntryUiState(
    val title: String = "",
    val amount: String = "",
    val selectedCategory: ExpenseCategory? = null,
    val notes: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val todayTotal: Double = 0.0
)

class ExpenseEntryViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseEntryUiState())
    val uiState: StateFlow<ExpenseEntryUiState> = _uiState.asStateFlow()

    init {
        loadTodayTotal()
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount)
    }

    fun updateCategory(category: ExpenseCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }

    fun addExpense() {
        val currentState = _uiState.value
        
        if (!validateInput()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            try {
                val expense = Expense(
                    title = currentState.title.trim(),
                    amount = currentState.amount.toDouble(),
                    category = currentState.selectedCategory!!,
                    notes = currentState.notes.takeIf { it.isNotBlank() }
                )
                
                expenseRepository.insertExpense(expense)
                
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isSuccess = true,
                    title = "",
                    amount = "",
                    selectedCategory = null,
                    notes = ""
                )
                
                loadTodayTotal()
                
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Failed to add expense: ${e.message}"
                )
            }
        }
    }

    private fun validateInput(): Boolean {
        val currentState = _uiState.value
        val errors = mutableListOf<String>()

        if (currentState.title.isBlank()) {
            errors.add("Title is required")
        }

        if (currentState.amount.isBlank()) {
            errors.add("Amount is required")
        } else {
            try {
                val amount = currentState.amount.toDouble()
                if (amount <= 0) {
                    errors.add("Amount must be greater than 0")
                }
            } catch (e: NumberFormatException) {
                errors.add("Invalid amount format")
            }
        }

        if (currentState.selectedCategory == null) {
            errors.add("Category is required")
        }

        if (currentState.notes.length > 100) {
            errors.add("Notes must be 100 characters or less")
        }

        if (errors.isNotEmpty()) {
            _uiState.value = currentState.copy(errorMessage = errors.joinToString(", "))
            return false
        }

        return true
    }

    private fun loadTodayTotal() {
        viewModelScope.launch {
            val total = expenseRepository.getTodayTotal()
            _uiState.value = _uiState.value.copy(todayTotal = total)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}
