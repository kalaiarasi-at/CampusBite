package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary

@Composable
fun AboutScreen(
    onExploreMenuClick: () -> Unit,
    onContactClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .verticalScroll(scrollState)
            .testTag("about_us_screen")
    ) {
        // Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_campus_hero),
                contentDescription = "Campus Canteen",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xCC181A1B))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(24.dp)
            ) {
                Surface(
                    color = OrangePrimary,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "ABOUT CAMPUSBITE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Smart College Canteen Pre-Order System",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Designed by students, built for college dining convenience.",
                    fontSize = 13.sp,
                    color = Color(0xFFCBD5E1)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Mission Statement
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Our Mission: Skip the Queue, Enjoy Your Food",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "College break intervals are only 15 to 20 minutes long. Traditional canteen queues cause students to spend the entire break waiting in line, rushing their meals, or missing nutritious food. CampusBite solves this with pre-ordered token scheduling.",
                        fontSize = 13.sp,
                        color = Color(0xFF475569),
                        lineHeight = 20.sp
                    )
                }
            }

            // Key Highlights Grid
            Text(
                text = "Key System Highlights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HighlightCard(
                    icon = Icons.Default.AccessTime,
                    title = "Scheduled Slots",
                    description = "Choose morning or afternoon break slots. Food is prepared fresh on schedule.",
                    modifier = Modifier.weight(1f)
                )
                HighlightCard(
                    icon = Icons.Default.QrCode,
                    title = "Express Tokens",
                    description = "Flash your digital token at Counter Bay 1 for zero-wait pickup.",
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HighlightCard(
                    icon = Icons.Default.Fastfood,
                    title = "Live Kitchen Stock",
                    description = "Real-time inventory prevents ordering items that are sold out.",
                    modifier = Modifier.weight(1f)
                )
                HighlightCard(
                    icon = Icons.Default.Security,
                    title = "Role-Protected",
                    description = "Student private orders are segregated from kitchen manager tools.",
                    modifier = Modifier.weight(1f)
                )
            }

            // Canteen Facilities & Timings
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = OrangePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Central Canteen Facility",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• Location: Ground Floor, Block B Central Court, Main Campus", fontSize = 12.sp, color = Color(0xFF475569))
                    Text("• Timings: Monday – Saturday, 7:30 AM to 7:00 PM", fontSize = 12.sp, color = Color(0xFF475569))
                    Text("• Kitchen Standards: FSSAI Certified hygiene & 100% vegetarian preparations", fontSize = 12.sp, color = Color(0xFF475569))
                }
            }

            // CTAs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onExploreMenuClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Explore Menu", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }

                Button(
                    onClick = onContactClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Contact Us", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun HighlightCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFF7ED), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = OrangeDark, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, fontSize = 11.sp, color = Color(0xFF64748B), lineHeight = 16.sp)
        }
    }
}
