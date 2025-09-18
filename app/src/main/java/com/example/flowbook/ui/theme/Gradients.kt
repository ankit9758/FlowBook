package com.example.flowbook.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Gradient definitions for the app
object Gradients {
    // Primary gradient - Purple to Blue
    val Primary = Brush.linearGradient(
        colors = listOf(
            Purple40,
            Blue40
        )
    )
    
    // Secondary gradient - Purple variants
    val Secondary = Brush.linearGradient(
        colors = listOf(
            PurpleGrey40,
            Purple40
        )
    )
    
    // Accent gradient - Blue to Indigo
    val Accent = Brush.linearGradient(
        colors = listOf(
            Blue40,
            Indigo40
        )
    )
    
    // Success gradient - Green variants
    val Success = Brush.linearGradient(
        colors = listOf(
            ExpenseGreen,
            Color(0xFF00A651)
        )
    )
    
    // Error gradient - Red variants
    val Error = Brush.linearGradient(
        colors = listOf(
            ExpenseRed,
            Color(0xFFE53935)
        )
    )
    
    // Warning gradient - Orange variants
    val Warning = Brush.linearGradient(
        colors = listOf(
            ExpenseOrange,
            Color(0xFFFF6F00)
        )
    )
    
    // Card gradient - Subtle background
    val Card = Brush.linearGradient(
        colors = listOf(
            Color.White,
            Color(0xFFF8F9FA)
        )
    )
    
    // Dark card gradient
    val DarkCard = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2D2D2D),
            Color(0xFF1A1A1A)
        )
    )
    
    // Floating action button gradient
    val FAB = Brush.linearGradient(
        colors = listOf(
            Purple40,
            PurpleGrey40,
            Blue40
        )
    )
    
    // Button gradient - Interactive
    val Button = Brush.linearGradient(
        colors = listOf(
            Purple40,
            Blue40
        )
    )
    
    // Button pressed gradient
    val ButtonPressed = Brush.linearGradient(
        colors = listOf(
            Purple40.copy(alpha = 0.8f),
            Blue40.copy(alpha = 0.8f)
        )
    )
}
