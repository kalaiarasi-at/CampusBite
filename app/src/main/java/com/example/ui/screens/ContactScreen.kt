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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.User
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary

@Composable
fun ContactScreen(
    currentUser: User?,
    onSendMessage: (String) -> Unit
) {
    var name by remember(currentUser) { mutableStateOf(currentUser?.name ?: "") }
    var email by remember(currentUser) { mutableStateOf(currentUser?.email ?: "") }
    var studentId by remember(currentUser) { mutableStateOf(currentUser?.studentId ?: "") }
    var message by remember { mutableStateOf("") }
    var submittedMessage by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .verticalScroll(scrollState)
            .padding(20.dp)
            .testTag("contact_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column {
            Text(
                text = "Contact Canteen & Support",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Have questions about pre-orders, dietary queries, or feedback? Get in touch with us.",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
        }

        // Contact Information Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ContactInfoCard(
                icon = Icons.Default.LocationOn,
                title = "Location",
                info = "Block B Central Court",
                modifier = Modifier.weight(1f)
            )
            ContactInfoCard(
                icon = Icons.Default.Call,
                title = "Helpline",
                info = "+91 91234 56789",
                modifier = Modifier.weight(1f)
            )
        }

        ContactInfoCard(
            icon = Icons.Default.Email,
            title = "Canteen Email",
            info = "canteen-operations@campusbite.edu",
            modifier = Modifier.fillMaxWidth()
        )

        // Operating Meal Timings
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = OrangePrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Service Hours & Slots", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                TimingRow("Breakfast & Kaapi", "07:30 AM – 11:00 AM", "Idli, Dosa, Pongal, Poori")
                TimingRow("Lunch Meals & Thali", "12:00 PM – 02:30 PM", "Executive Thali, Sambar Rice, Lemon Rice")
                TimingRow("Evening Snacks & Chai", "03:30 PM – 07:00 PM", "Samosa, Veg Puff, Frankie, Coffee, Soda")
            }
        }

        // Interactive Feedback Form
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Send Feedback / Canteen Enquiry",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Suggestions to add new dishes or improve pickup queues are always welcome.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (submittedMessage != null) {
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = submittedMessage ?: "",
                            color = Color(0xFF14532D),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth().testTag("contact_input_name"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = studentId,
                        onValueChange = { studentId = it },
                        label = { Text("Student ID") },
                        modifier = Modifier.weight(1f).testTag("contact_input_id"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.weight(1f).testTag("contact_input_email"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Feedback or message...") },
                    modifier = Modifier.fillMaxWidth().testTag("contact_input_message"),
                    shape = RoundedCornerShape(10.dp),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isBlank() || message.isBlank()) {
                            submittedMessage = "Please provide your name and message."
                        } else {
                            submittedMessage = "Thank you, $name! Your message has been forwarded to Canteen Operations."
                            onSendMessage(message)
                            message = ""
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("contact_submit_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Feedback", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ContactInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    info: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFEDD5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = OrangeDark, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, fontSize = 11.sp, color = Color(0xFF64748B))
                Text(info, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun TimingRow(meal: String, timing: String, items: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(meal, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(timing, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OrangeDark)
        }
        Text(items, fontSize = 11.sp, color = Color(0xFF64748B))
    }
}
