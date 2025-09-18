package com.example.flowbook.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flowbook.data.database.ExpenseDatabase
import com.example.flowbook.data.repository.ExpenseRepository
import com.example.flowbook.ui.animations.AnimatedLoadingButton
import com.example.flowbook.ui.animations.AnimatedSuccessMessage
import com.example.flowbook.ui.components.AmountInput
import com.example.flowbook.ui.components.CategorySelector
import com.example.flowbook.ui.components.TotalSummaryCard
import com.example.flowbook.ui.viewmodel.ExpenseEntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(
    onNavigateBack: () -> Unit,
    viewModel: ExpenseEntryViewModel? = null
) {
    val context = LocalContext.current
    val expenseViewModel = viewModel ?: remember {
        val database = ExpenseDatabase.getDatabase(context)
        val repository = ExpenseRepository(database.expenseDao())
        ExpenseEntryViewModel(repository)
    }
    val uiState by expenseViewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            // Show success message briefly, then navigate back
            kotlinx.coroutines.delay(1500) // Show success message for 1.5 seconds
            expenseViewModel.clearSuccess()
            onNavigateBack() // Navigate back to dashboard
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Today's Total Summary
            TotalSummaryCard(
                totalAmount = uiState.todayTotal,
                totalCount = 0, // We'll update this when we have the count
                title = "Today's Total"
            )

            // Title Input
            OutlinedTextField(
                value = uiState.title,
                onValueChange = expenseViewModel::updateTitle,
                label = { Text("Title") },
                placeholder = { Text("Enter expense title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                
            )

            // Amount Input
            AmountInput(
                value = uiState.amount,
                onValueChange = expenseViewModel::updateAmount,
                modifier = Modifier.fillMaxWidth()
            )

            // Category Selector
            CategorySelector(
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = expenseViewModel::updateCategory,
                modifier = Modifier.fillMaxWidth()
            )

            // Notes Input
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = expenseViewModel::updateNotes,
                label = { Text("Notes (Optional)") },
                placeholder = { Text("Add any additional notes...") },
                 singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Error Message
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.errorMessage ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Success Message
            AnimatedSuccessMessage(
                isVisible = uiState.isSuccess,
                message = "Expense added successfully!",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Expense Button
            AnimatedLoadingButton(
                isLoading = uiState.isLoading,
                text = "Add Expense",
                onClick = expenseViewModel::addExpense,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
    }
}
