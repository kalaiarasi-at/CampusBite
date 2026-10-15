package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.OutdoorGrill
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun AdminDashboardScreen(
    orders: List<Order>,
    onUpdateOrderStatus: (orderId: String, newStatus: OrderStatus, reason: String?) -> Unit,
    onNavigateToFoodManage: () -> Unit
) {
    var selectedStatusFilter by remember { mutableStateOf<OrderStatus?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    var cancellingOrderId by remember { mutableStateOf<String?>(null) }
    var cancelReason by remember { mutableStateOf("Out of stock in kitchen") }

    // Analytics KPIs
    val totalOrders = orders.size
    val pendingCount = orders.count { it.orderStatus == OrderStatus.PLACED || it.orderStatus == OrderStatus.ACCEPTED }
    val preparingCount = orders.count { it.orderStatus == OrderStatus.PREPARING }
    val readyCount = orders.count { it.orderStatus == OrderStatus.READY_FOR_PICKUP }
    val completedCount = orders.count { it.orderStatus == OrderStatus.COMPLETED }
    val totalRevenue = orders.filter { it.orderStatus != OrderStatus.CANCELLED }.sumOf { it.total }

    val filteredOrders = orders.filter { order ->
        val matchesStatus = selectedStatusFilter == null || order.orderStatus == selectedStatusFilter
        val matchesQuery = if (searchQuery.isBlank()) true else {
            val q = searchQuery.trim().lowercase()
            order.id.lowercase().contains(q) ||
            order.tokenNumber.toString().contains(q) ||
            order.customerName.lowercase().contains(q) ||
            order.customerStudentId.lowercase().contains(q)
        }
        matchesStatus && matchesQuery
    }

    if (cancellingOrderId != null) {
        AlertDialog(
            onDismissRequest = { cancellingOrderId = null },
            title = { Text("Cancel Order $cancellingOrderId") },
            text = {
                Column {
                    Text("Select or enter reason for cancellation:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = cancelReason,
                        onValueChange = { cancelReason = it },
                        modifier = Modifier.fillMaxWidth().testTag("input_cancel_reason")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        cancellingOrderId?.let { id ->
                            onUpdateOrderStatus(id, OrderStatus.CANCELLED, cancelReason)
                        }
                        cancellingOrderId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDanger)
                ) {
                    Text("Confirm Cancellation")
                }
            },
            dismissButton = {
                TextButton(onClick = { cancellingOrderId = null }) {
                    Text("Back")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .testTag("admin_dashboard_screen")
    ) {
        // Top Header
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Canteen Admin Console",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Manage kitchen orders & update pickup status",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Button(
                        onClick = onNavigateToFoodManage,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier.testTag("btn_admin_goto_food_manage")
                    ) {
                        Icon(Icons.Default.Fastfood, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Edit Menu", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // KPIs Row
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        KpiChip(label = "Total Revenue", value = "₹${totalRevenue.toInt()}", color = OrangeDark)
                    }
                    item {
                        KpiChip(label = "Placed / Pending", value = "$pendingCount", color = Color(0xFF0284C7))
                    }
                    item {
                        KpiChip(label = "Cooking Now", value = "$preparingCount", color = OrangePrimary)
                    }
                    item {
                        KpiChip(label = "Ready to Pickup", value = "$readyCount", color = GreenSuccess)
                    }
                    item {
                        KpiChip(label = "Completed", value = "$completedCount", color = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Order #, Token #, or Student Name...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_search_orders_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangePrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Status Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item {
                        FilterChip(
                            selected = selectedStatusFilter == null,
                            onClick = { selectedStatusFilter = null },
                            label = { Text("All ($totalOrders)", fontSize = 11.sp) }
                        )
                    }
                    OrderStatus.values().forEach { status ->
                        val count = orders.count { it.orderStatus == status }
                        item {
                            FilterChip(
                                selected = selectedStatusFilter == status,
                                onClick = { selectedStatusFilter = status },
                                label = { Text("${status.displayName} ($count)", fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }
        }

        // Orders Table / List
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredOrders, key = { it.id }) { order ->
                AdminOrderCard(
                    order = order,
                    onStatusChange = { newStatus ->
                        onUpdateOrderStatus(order.id, newStatus, null)
                    },
                    onCancelClick = {
                        cancellingOrderId = order.id
                    }
                )
            }
        }
    }
}

@Composable
private fun KpiChip(label: String, value: String, color: Color) {
    Surface(
        color = Color(0xFFF8FAFC),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(text = label, fontSize = 10.sp, color = Color(0xFF64748B))
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
    }
}

@Composable
private fun AdminOrderCard(
    order: Order,
    onStatusChange: (OrderStatus) -> Unit,
    onCancelClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_order_card_${order.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Token, Customer & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFFFFF7ED),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Token #${order.tokenNumber}",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = OrangeDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = order.customerName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "ID: ${order.customerStudentId} • ${order.id}",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                StatusBadge(status = order.orderStatus)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items List
            order.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "• ${item.quantity}x ${item.foodName}",
                        fontSize = 13.sp,
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "₹${item.subtotal.toInt()}",
                        fontSize = 13.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Slot & Notes
            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Pickup: ${order.pickupDate} • ${order.pickupSlot}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    if (order.specialInstructions.isNotBlank()) {
                        Text(
                            text = "Note: \"${order.specialInstructions}\"",
                            fontSize = 11.sp,
                            color = OrangeDark,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                    Text(
                        text = "Payment: ${order.paymentMethod} (${order.paymentStatus}) • Total: ₹${order.total.toInt()}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            // Admin Workflow Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (order.orderStatus != OrderStatus.COMPLETED && order.orderStatus != OrderStatus.CANCELLED) {
                    TextButton(
                        onClick = onCancelClick,
                        modifier = Modifier.testTag("admin_cancel_${order.id}")
                    ) {
                        Text("Cancel Order", color = RedDanger, fontSize = 12.sp)
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Status Advancement Buttons
                when (order.orderStatus) {
                    OrderStatus.PLACED -> {
                        Button(
                            onClick = { onStatusChange(OrderStatus.ACCEPTED) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                            modifier = Modifier.testTag("admin_accept_${order.id}")
                        ) {
                            Text("Accept Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    OrderStatus.ACCEPTED -> {
                        Button(
                            onClick = { onStatusChange(OrderStatus.PREPARING) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            modifier = Modifier.testTag("admin_prep_${order.id}")
                        ) {
                            Icon(Icons.Default.OutdoorGrill, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Start Cooking", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    OrderStatus.PREPARING -> {
                        Button(
                            onClick = { onStatusChange(OrderStatus.READY_FOR_PICKUP) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
                            modifier = Modifier.testTag("admin_ready_${order.id}")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Mark Ready for Pickup", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    OrderStatus.READY_FOR_PICKUP -> {
                        Button(
                            onClick = { onStatusChange(OrderStatus.COMPLETED) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                            modifier = Modifier.testTag("admin_complete_${order.id}")
                        ) {
                            Text("Complete & Handed Over", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    OrderStatus.COMPLETED -> {
                        Text(
                            text = "Picked Up by Student",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenSuccess
                        )
                    }
                    OrderStatus.CANCELLED -> {
                        Text(
                            text = "Cancelled: ${order.cancellationReason ?: ""}",
                            fontSize = 12.sp,
                            color = RedDanger
                        )
                    }
                }
            }
        }
    }
}
