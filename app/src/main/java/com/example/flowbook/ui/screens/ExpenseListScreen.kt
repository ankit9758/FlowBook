package com.example.flowbook.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flowbook.data.database.ExpenseDatabase
import com.example.flowbook.data.repository.ExpenseRepository
import com.example.flowbook.data.model.ExpenseCategory
import com.example.flowbook.ui.components.ExpenseCard
import com.example.flowbook.ui.components.TotalSummaryCard
import com.example.flowbook.ui.viewmodel.ExpenseListViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    onNavigateToAddExpense: () -> Unit,
    onNavigateToReports: () -> Unit,
    viewModel: ExpenseListViewModel? = null
) {
    val context = LocalContext.current
    val expenseViewModel = viewModel ?: remember {
        val database = ExpenseDatabase.getDatabase(context)
        val repository = ExpenseRepository(database.expenseDao())
        ExpenseListViewModel(repository)
    }
    val uiState by expenseViewModel.uiState.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null) {
            // Handle error - could show snackbar
            expenseViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Filter")
                    }
                    IconButton(onClick = expenseViewModel::toggleGroupByCategory) {
                        Icon(Icons.Default.Info, contentDescription = "Group by category")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddExpense
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Summary Card
            TotalSummaryCard(
                totalAmount = uiState.totalAmount,
                totalCount = uiState.totalCount,
                title = "Total Spent",
                modifier = Modifier.padding(16.dp)
            )

            // Group Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.groupByCategory) "Grouped by Category" else "Grouped by Time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = "Tap group icon to toggle",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Expenses List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.expenses.isEmpty()) {
                EmptyState(
                    onAddExpense = onNavigateToAddExpense,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                val groupedExpenses = expenseViewModel.getGroupedExpenses()
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    groupedExpenses.forEach { (groupKey, expenses) ->
                        item {
                            Text(
                                text = groupKey,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(expenses) { expense ->
                            ExpenseCard(
                                expense = expense,
                                onDelete = { expenseViewModel.deleteExpense(expense) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { category ->
                expenseViewModel.updateSelectedCategory(category)
                showFilterDialog = false
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@Composable
private fun EmptyState(
    onAddExpense: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No expenses yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Add your first expense to get started",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onAddExpense) {
            Text("Add Expense")
        }
    }
}

@Composable
private fun FilterDialog(
    selectedCategory: ExpenseCategory?,
    onCategorySelected: (ExpenseCategory?) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filter by Category") },
        text = {
            Column {
                ExpenseCategory.values().forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedCategory == category,
                            onClick = { onCategorySelected(category) }
                        )
                        Text(
                            text = "${category.icon} ${category.displayName}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedCategory == null,
                        onClick = { onCategorySelected(null) }
                    )
                    Text(
                        text = "All Categories",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
