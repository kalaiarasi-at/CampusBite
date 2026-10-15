package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Store
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
import com.example.data.model.OrderStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.RedDanger

@Composable
fun OrderTrackingScreen(
    order: Order,
    onBackToOrders: () -> Unit
) {
    val scrollState = rememberScrollState()

    val timelineSteps = listOf(
        OrderStatus.PLACED to "Order Placed",
        OrderStatus.ACCEPTED to "Accepted by Kitchen",
        OrderStatus.PREPARING to "Preparing Your Meal",
        OrderStatus.READY_FOR_PICKUP to "Ready for Pickup",
        OrderStatus.COMPLETED to "Picked Up & Completed"
    )

    val currentStepIndex = when (order.orderStatus) {
        OrderStatus.PLACED -> 0
        OrderStatus.ACCEPTED -> 1
        OrderStatus.PREPARING -> 2
        OrderStatus.READY_FOR_PICKUP -> 3
        OrderStatus.COMPLETED -> 4
        OrderStatus.CANCELLED -> -1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("order_tracking_screen")
    ) {
        // Token & Status Header Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (order.orderStatus == OrderStatus.READY_FOR_PICKUP) Color(0xFFDCFCE7) else Color(0xFFFFF7ED)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pickup Token",
                            fontSize = 12.sp,
                            color = if (order.orderStatus == OrderStatus.READY_FOR_PICKUP) Color(0xFF166534) else OrangeDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "#${order.tokenNumber}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (order.orderStatus == OrderStatus.READY_FOR_PICKUP) Color(0xFF14532D) else OrangeDark
                        )
                    }

                    StatusBadge(status = order.orderStatus)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Order ID: ${order.id}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Alert message based on status
                if (order.orderStatus == OrderStatus.READY_FOR_PICKUP) {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = GreenSuccess,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Your food is HOT and READY! Walk up to Counter Bay 1 and show Token #${order.tokenNumber}.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14532D)
                            )
                        }
                    }
                } else if (order.orderStatus == OrderStatus.PREPARING) {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Fastfood,
                                contentDescription = null,
                                tint = OrangePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Chef is currently preparing your dishes. Estimated ready in ~${order.estimatedMinutes} mins.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OrangeDark
                            )
                        }
                    }
                } else if (order.orderStatus == OrderStatus.CANCELLED) {
                    Surface(
                        color = Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = RedDanger,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Order cancelled: ${order.cancellationReason ?: "Item unavailable in kitchen"}",
                                fontSize = 12.sp,
                                color = RedDanger,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Timeline
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Live Order Timeline",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                timelineSteps.forEachIndexed { index, pair ->
                    val isCompleted = currentStepIndex >= index
                    val isCurrent = currentStepIndex == index

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Circle Indicator
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    when {
                                        isCompleted -> GreenSuccess
                                        isCurrent -> OrangePrimary
                                        else -> Color(0xFFE2E8F0)
                                    },
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isCompleted) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = (index + 1).toString(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) Color.White else Color(0xFF64748B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = pair.second,
                                fontSize = 13.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCompleted || isCurrent) MaterialTheme.colorScheme.onSurface else Color(0xFF94A3B8)
                            )
                            if (isCurrent) {
                                Text(
                                    text = "Current Status",
                                    fontSize = 11.sp,
                                    color = OrangeDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    if (index < timelineSteps.size - 1) {
                        Box(
                            modifier = Modifier
                                .padding(start = 13.dp)
                                .width(2.dp)
                                .height(22.dp)
                                .background(
                                    if (currentStepIndex > index) GreenSuccess else Color(0xFFE2E8F0)
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pickup Location & Slot Details
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = OrangePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pickup Spot: Counter Bay 1, Central Canteen",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Scheduled Slot: ${order.pickupDate} • ${order.pickupSlot}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                if (order.specialInstructions.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Notes: \"${order.specialInstructions}\"",
                        fontSize = 12.sp,
                        color = Color(0xFF475569),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Items in this order
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Items Ordered",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                order.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.quantity}x ${item.foodName}",
                            fontSize = 13.sp,
                            color = Color(0xFF334155)
                        )
                        Text(
                            text = "₹${item.subtotal.toInt()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total (incl. packaging)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        "₹${order.total.toInt()}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = OrangeDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            onClick = onBackToOrders,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("btn_back_to_orders_history")
        ) {
            Text("Back to My Orders", fontWeight = FontWeight.SemiBold)
        }
    }
}
