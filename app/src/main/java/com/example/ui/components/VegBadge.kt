package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NonVegBrown
import com.example.ui.theme.VegGreen

@Composable
fun VegBadge(
    isVegetarian: Boolean,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false
) {
    val borderColor = if (isVegetarian) VegGreen else NonVegBrown
    val dotColor = if (isVegetarian) VegGreen else NonVegBrown

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .border(1.5.dp, borderColor, RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .background(dotColor, CircleShape)
            )
        }
        if (showLabel) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isVegetarian) "Pure Veg" else "Non-Veg",
                fontSize = 11.sp,
                color = borderColor,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
