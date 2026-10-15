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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.model.CartItem
import com.example.data.model.User
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeContainer
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.RedDanger

@Composable
fun CheckoutScreen(
    currentUser: User?,
    cartItems: List<CartItem>,
    onRequireLogin: () -> Unit,
    onConfirmOrder: (
        pickupDate: String,
        pickupSlot: String,
        specialInstructions: String,
        paymentMethod: String
    ) -> Boolean
) {
    if (currentUser == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFDF9))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = OrangeDark,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Student Login Required",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Please log in or register your college student ID before placing your canteen pre-order.",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = onRequireLogin,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Text("Log In or Register", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    val scrollState = rememberScrollState()

    val availableSlots = listOf(
        "10:15 AM - 10:30 AM (Morning Recess Break)",
        "12:30 PM - 12:45 PM (Lunch Slot 1)",
        "01:00 PM - 01:15 PM (Lunch Slot 2)",
        "03:45 PM - 04:00 PM (Tea / Evening Break)",
        "05:00 PM - 05:15 PM (Post-Class Recess)"
    )

    var selectedDate by remember { mutableStateOf("Today") }
    var selectedSlot by remember { mutableStateOf(availableSlots[0]) }
    var specialInstructions by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Demo UPI Payment Simulation") }
    var confirmedReviewCheckbox by remember { mutableStateOf(false) }

    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val subtotal = cartItems.sumOf { it.subtotal }
    val packagingFee = 5.0
    val total = subtotal + packagingFee

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .verticalScroll(scrollState)
            .padding(20.dp)
            .testTag("checkout_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Checkout & Slot Confirmation",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Review your items and student details before submitting to the canteen kitchen.",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
        }

        if (errorMessage != null) {
            Surface(
                color = Color(0xFFFEE2E2),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = RedDanger, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = RedDanger,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Student Profile Verification Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = OrangePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Student Details Verification", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                StudentDetailRow("Full Name", currentUser.name)
                StudentDetailRow("Student ID / Reg No.", currentUser.studentId)
                StudentDetailRow("College Email", currentUser.email)
                StudentDetailRow("Department", currentUser.department)
                StudentDetailRow("Year of Study", currentUser.yearOfStudy)
                StudentDetailRow("Phone Number", currentUser.phoneNumber)
            }
        }

        // Order Items Review Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Selected Canteen Items (${cartItems.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))

                cartItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${item.quantity}x ${item.foodItem.name}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "₹${item.foodItem.price.toInt()} each • ${item.foodItem.category.displayName}",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Text(
                            text = "₹${item.subtotal.toInt()}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal", fontSize = 13.sp, color = Color(0xFF64748B))
                    Text("₹${subtotal.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Canteen Eco Packaging", fontSize = 13.sp, color = Color(0xFF64748B))
                    Text("₹${packagingFee.toInt()}", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Final Payable Amount", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("₹${total.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = OrangeDark)
                }
            }
        }

        // Campus Recess Slot Selection
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = OrangePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Select Recess Pickup Slot", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Pick the exact recess window when you will walk up to Counter Bay 1.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Today", "Tomorrow").forEach { d ->
                        val isSel = selectedDate == d
                        Surface(
                            onClick = { selectedDate = d },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) OrangePrimary else Color(0xFFF1F5F9),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = d,
                                color = if (isSel) Color.White else Color(0xFF334155),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(vertical = 10.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    availableSlots.forEach { slot ->
                        val isSel = selectedSlot == slot
                        Surface(
                            onClick = { selectedSlot = slot },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSel) OrangeContainer else Color(0xFFF8FAFC),
                            border = if (isSel) androidx.compose.foundation.BorderStroke(1.dp, OrangePrimary) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSel,
                                    onClick = { selectedSlot = slot },
                                    colors = RadioButtonDefaults.colors(selectedColor = OrangePrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = slot,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) OrangeDark else Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = specialInstructions,
                    onValueChange = { specialInstructions = it },
                    placeholder = { Text("Kitchen notes (e.g. less spicy, pack chutney in separate pouch)...", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("checkout_special_instructions"),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 2
                )
            }
        }

        // Payment Method & Review Checkbox
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Payment Method", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(10.dp))

                PaymentOptionRow(
                    title = "Demo UPI Payment Simulation",
                    subtitle = "Simulates instant UPI transfer (Google Pay / PhonePe / Paytm)",
                    selected = paymentMethod == "Demo UPI Payment Simulation",
                    onClick = { paymentMethod = "Demo UPI Payment Simulation" }
                )

                Spacer(modifier = Modifier.height(8.dp))

                PaymentOptionRow(
                    title = "Pay at Canteen Counter",
                    subtitle = "Pay cash or QR during token collection at Counter Bay 1",
                    selected = paymentMethod == "Pay at Canteen Counter",
                    onClick = { paymentMethod = "Pay at Canteen Counter" }
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(12.dp))

                // Required Review Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = confirmedReviewCheckbox,
                        onCheckedChange = { confirmedReviewCheckbox = it; errorMessage = null },
                        colors = CheckboxDefaults.colors(checkedColor = OrangePrimary),
                        modifier = Modifier.testTag("checkbox_order_review_confirmation")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "I have reviewed my order items, student registration details, and pickup slot timing.",
                        fontSize = 12.sp,
                        color = Color(0xFF334155),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!confirmedReviewCheckbox) {
                            errorMessage = "Please check the confirmation box to verify your order details."
                        } else if (cartItems.isEmpty()) {
                            errorMessage = "Your cart is empty. Add items from the menu."
                        } else if (!isSubmitting) {
                            isSubmitting = true
                            errorMessage = null
                            val success = onConfirmOrder(
                                selectedDate,
                                selectedSlot,
                                specialInstructions,
                                paymentMethod
                            )
                            if (!success) {
                                isSubmitting = false
                            }
                        }
                    },
                    enabled = !isSubmitting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_place_order_submit")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Place Order • ₹${total.toInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = Color(0xFF64748B))
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
    }
}

@Composable
private fun PaymentOptionRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color(0xFFFFF7ED) else Color(0xFFF8FAFC),
        border = if (selected) androidx.compose.foundation.BorderStroke(1.dp, OrangePrimary) else null,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = OrangePrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (selected) OrangeDark else Color(0xFF1E293B))
                Text(subtitle, fontSize = 11.sp, color = Color(0xFF64748B))
            }
        }
    }
}
