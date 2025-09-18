package com.example.flowbook.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
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
    onNavigateBack: () -> Unit,
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
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
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
                onClick = onNavigateToAddExpense,
                containerColor = MaterialTheme.colorScheme.primary
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
                AnimatedLoadingState(
                    modifier = Modifier.fillMaxSize()
                )
            } else if (uiState.expenses.isEmpty()) {
                EmptyState(
                    onAddExpense = onNavigateToAddExpense,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                val groupedExpenses = expenseViewModel.getGroupedExpenses()
                
                AnimatedExpenseList(
                    groupedExpenses = groupedExpenses,
                    onDeleteExpense = { expenseViewModel.deleteExpense(it) }
                )
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

@Composable
private fun AnimatedLoadingState(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .alpha(0.8f),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            
            Text(
                text = "Loading expenses...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AnimatedExpenseList(
    groupedExpenses: Map<String, List<com.example.flowbook.data.model.Expense>>,
    onDeleteExpense: (com.example.flowbook.data.model.Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedExpenses.forEach { (groupKey, expenses) ->
            item {
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn(
                        animationSpec = tween(300)
                    )
                ) {
                    Text(
                        text = groupKey,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            
            itemsIndexed(
                items = expenses,
                key = { _, expense -> expense.id }
            ) { index, expense ->
                AnimatedExpenseCard(
                    expense = expense,
                    onDelete = { onDeleteExpense(expense) },
                    index = index
                )
            }
        }
    }
}

@Composable
private fun AnimatedExpenseCard(
    expense: com.example.flowbook.data.model.Expense,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0
) {
    var isVisible by remember { mutableStateOf(false) }
    
    // Staggered animation delay based on index
    val delay = (index * 100).coerceAtMost(500)
    
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(400)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessHigh
            )
        ) + fadeOut(
            animationSpec = tween(200)
        ),
        modifier = modifier
    ) {
        ExpenseCard(
            expense = expense,
            onDelete = onDelete
        )
    }
}
