package com.example.flowbook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flowbook.ui.screens.ExpenseDashboard
import com.example.flowbook.ui.screens.ExpenseEntryScreen
import com.example.flowbook.ui.screens.ExpenseListScreen
import com.example.flowbook.ui.screens.ExpenseReportScreen

@Composable
fun ExpenseNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        composable("dashboard") {
            ExpenseDashboard(
                onNavigateToAddExpense = {
                    navController.navigate("expense_entry")
                },
                onNavigateToExpenseList = {
                    navController.navigate("expense_list")
                },
                onNavigateToReports = {
                    navController.navigate("expense_report")
                }
            )
        }
        
        composable("expense_list") {
            ExpenseListScreen(
                onNavigateToAddExpense = {
                    navController.navigate("expense_entry")
                },
                onNavigateToReports = {
                    navController.navigate("expense_report")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("expense_entry") {
            ExpenseEntryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable("expense_report") {
            ExpenseReportScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
