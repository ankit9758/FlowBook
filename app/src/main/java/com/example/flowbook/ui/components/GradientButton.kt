package com.example.flowbook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flowbook.ui.theme.Gradients

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    gradient: Brush = Gradients.Button,
    textColor: Color = Color.White,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.SemiBold,
    cornerRadius: Int = 12,
    elevation: Int = 4,
    isLoading: Boolean = false,
    loadingText: String = "Loading..."
) {
    var isPressed by remember { mutableStateOf(false) }
    
    val buttonGradient = if (isPressed) {
        Gradients.ButtonPressed
    } else {
        gradient
    }
    
    val buttonElevation = if (isPressed) elevation / 2 else elevation
    
    Box(
        modifier = modifier
            .shadow(
                elevation = buttonElevation.dp,
                shape = RoundedCornerShape(cornerRadius.dp)
            )
            .clip(RoundedCornerShape(cornerRadius.dp))
            .background(buttonGradient)
            .clickable(
                enabled = enabled && !isLoading,
                onClick = {
                    if (!isLoading) {
                        onClick()
                    }
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = textColor,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            Text(
                text = if (isLoading) loadingText else text,
                color = textColor,
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradient: Brush = Gradients.Card,
    cornerRadius: Int = 16,
    elevation: Int = 4,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(cornerRadius.dp)
            ),
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = gradient,
                    shape = RoundedCornerShape(cornerRadius.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                content = content
            )
        }
    }
}
