package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Order
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary

@Composable
fun OrderConfirmationScreen(
    order: Order,
    onTrackOrderClick: () -> Unit,
    onBackToMenuClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .verticalScroll(scrollState)
            .padding(24.dp)
            .testTag("order_confirmation_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Prominent Green Success Icon
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(Color(0xFFDCFCE7), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = GreenSuccess,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Mandatory Exact Headers
        Text(
            text = "Order Placed Successfully!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Thank you for ordering with CampusBite. Your order has been received by the canteen.",
            fontSize = 13.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Express Pickup Token Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, OrangePrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "CANTEEN EXPRESS TOKEN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeDark,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "#${order.tokenNumber}",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrangeDark
                )

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Show this token at Express Counter Bay 1",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actual Saved Order Details Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Order Receipt Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    StatusBadge(status = order.orderStatus)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OrderDetailRow("Unique Order ID", order.id)
                OrderDetailRow("Student Name", order.customerName)
                OrderDetailRow("Student ID", order.customerStudentId)
                if (order.customerEmail.isNotBlank()) {
                    OrderDetailRow("College Email", order.customerEmail)
                }
                OrderDetailRow("Order Date & Time", if (order.formattedDateTime.isNotBlank()) order.formattedDateTime else "Today")
                OrderDetailRow("Selected Pickup Time", "${order.pickupDate} • ${order.pickupSlot}")
                OrderDetailRow("Payment Method", order.paymentMethod)
                OrderDetailRow("Payment Status", order.paymentStatus)
                OrderDetailRow("Current Order Status", order.orderStatus.displayName)

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(12.dp))

                Text("Ordered Food Items", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))

                order.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.quantity}x ${item.foodName} (₹${item.unitPrice.toInt()} each)",
                            fontSize = 12.sp,
                            color = Color(0xFF334155)
                        )
                        Text(
                            text = "₹${item.subtotal.toInt()}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Packaging Fee", fontSize = 12.sp, color = Color(0xFF64748B))
                    Text("₹${order.packagingFee.toInt()}", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        "₹${order.total.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = OrangeDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Two Working Mandatory Action Buttons
        Button(
            onClick = onTrackOrderClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_confirmation_track_my_order")
        ) {
            Icon(Icons.Default.Timeline, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Track My Order", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onBackToMenuClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_confirmation_back_to_menu")
        ) {
            Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = OrangeDark, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back to Menu", color = OrangeDark, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun OrderDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
    }
}
