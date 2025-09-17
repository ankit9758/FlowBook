package com.example.flowbook.data.model

enum class ExpenseCategory(
    val displayName: String,
    val icon: String
) {
    STAFF("Staff", "👥"),
    TRAVEL("Travel", "✈️"),
    FOOD("Food", "🍽️"),
    UTILITY("Utility", "⚡");
    
    companion object {
        fun fromDisplayName(displayName: String): ExpenseCategory? {
            return values().find { it.displayName == displayName }
        }
    }
}
