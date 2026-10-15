package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderStatus
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.GreenSuccessContainer
import com.example.ui.theme.OnGreenSuccessContainer
import com.example.ui.theme.OrangeContainer
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.RedDanger
import com.example.ui.theme.RedDangerContainer

@Composable
fun StatusBadge(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        OrderStatus.PLACED -> Color(0xFFE0F2FE) to Color(0xFF0369A1)
        OrderStatus.ACCEPTED -> Color(0xFFEDE9FE) to Color(0xFF6D28D9)
        OrderStatus.PREPARING -> OrangeContainer to OrangeDark
        OrderStatus.READY_FOR_PICKUP -> GreenSuccessContainer to OnGreenSuccessContainer
        OrderStatus.COMPLETED -> Color(0xFFF1F5F9) to Color(0xFF475569)
        OrderStatus.CANCELLED -> RedDangerContainer to RedDanger
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.displayName,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
