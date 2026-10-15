package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Spa
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CartItem
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.User
import com.example.ui.components.FoodItemCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary

@Composable
fun HomeScreen(
    currentUser: User?,
    foodItems: List<FoodItem>,
    cartItems: List<CartItem>,
    activeOrders: List<Order>,
    onNavigateToMenu: (FoodCategory?) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToTracking: (Order) -> Unit,
    onFoodCardClick: (FoodItem) -> Unit,
    onAddToCart: (FoodItem) -> Unit,
    onIncrementCart: (FoodItem) -> Unit,
    onDecrementCart: (FoodItem) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToContact: () -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedCategoryFilter by remember { mutableStateOf(FoodCategory.ALL) }

    val todaySpecial = foodItems.find { it.isTodaySpecial } ?: foodItems.firstOrNull()
    val activeOrder = activeOrders.firstOrNull {
        it.orderStatus != OrderStatus.COMPLETED && it.orderStatus != OrderStatus.CANCELLED
    }

    val displayDishes = if (selectedCategoryFilter == FoodCategory.ALL) {
        foodItems.take(8)
    } else {
        foodItems.filter { it.category == selectedCategoryFilter }.take(8)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .verticalScroll(scrollState)
            .testTag("home_screen")
    ) {
        // Large Attractive Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_campus_hero),
                contentDescription = "College Canteen Dining",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xF2181A1B),
                                Color(0xD9181A1B),
                                Color(0x66181A1B)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    color = OrangePrimary,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "SMART COLLEGE CANTEEN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Exact headline from prompt: "Skip the Queue. Enjoy Your Food."
                Text(
                    text = "Skip the Queue.\nEnjoy Your Food.",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    lineHeight = 38.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pre-order hot breakfast, meals & snacks from your college canteen. Select your class break pickup slot and grab your meal in seconds.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE2E8F0),
                    maxLines = 2,
                    lineHeight = 20.sp,
                    modifier = Modifier.fillMaxWidth(0.85f)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { onNavigateToMenu(null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("hero_btn_explore_menu")
                    ) {
                        Text("Explore Menu", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }

                    if (activeOrder != null) {
                        Button(
                            onClick = { onNavigateToTracking(activeOrder) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("hero_btn_track_active_order")
                        ) {
                            Icon(Icons.Default.Timeline, contentDescription = null, tint = OrangeDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Track Active Token #${activeOrder.tokenNumber}", color = OrangeDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Live Canteen Status & Next Slot Banner
        Surface(
            color = Color(0xFFF8FAFC),
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(GreenSuccess, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Central Canteen Open (07:30 AM – 07:00 PM)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF14532D)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = OrangeDark,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Next Recess Pickup: 10:15 AM (Morning Break) • Bay 1",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OrangeDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Active Order Banner Card (if present)
        if (activeOrder != null) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7ED)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, OrangePrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateToTracking(activeOrder) }
                    .testTag("home_active_order_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(OrangePrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Order #${activeOrder.id} • Token #${activeOrder.tokenNumber}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusBadge(status = activeOrder.orderStatus)
                            }
                            Text(
                                text = "Slot: ${activeOrder.pickupSlot} • Counter Bay 1",
                                fontSize = 12.sp,
                                color = OrangeDark,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigateToTracking(activeOrder) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                    ) {
                        Text("Track Live Progress", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Today's Special Dish Highlight Banner
        if (todaySpecial != null) {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Today's Canteen Special 🌟",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Chef's recommended dish prepared fresh in limited quantity",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onFoodCardClick(todaySpecial) }
                        .testTag("home_special_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                color = Color(0xFFFEF08A),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "FEATURED COMBO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF854D0E),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = todaySpecial.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = todaySpecial.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF475569),
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹${todaySpecial.price.toInt()}",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OrangeDark
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "Ready in ~${todaySpecial.preparationTime} mins • Pure Veg",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = { onAddToCart(todaySpecial) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text("Add to Cart", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        // Featured Food Section & Category Filters
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Featured Canteen Dishes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Popular student picks ready for quick recess collection",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Text(
                    text = "View Full Menu (${foodItems.size}) →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeDark,
                    modifier = Modifier.clickable { onNavigateToMenu(null) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category Filter Pills
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(FoodCategory.values()) { cat ->
                    val isSel = selectedCategoryFilter == cat
                    Surface(
                        onClick = { selectedCategoryFilter = cat },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSel) OrangePrimary else Color.White,
                        border = if (!isSel) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)) else null,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = cat.displayName,
                            color = if (isSel) Color.White else Color(0xFF334155),
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Professional Multi-Column Grid on Wide/Desktop Viewports
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val isWide = maxWidth > 600.dp
                val columns = if (isWide) 2 else 1

                val rows = displayDishes.chunked(columns)
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowItems.forEach { dish ->
                                val cartQty = cartItems.find { it.foodItem.id == dish.id }?.quantity ?: 0
                                Box(modifier = Modifier.weight(1f)) {
                                    FoodItemCard(
                                        foodItem = dish,
                                        currentCartQty = cartQty,
                                        onCardClick = { onFoodCardClick(dish) },
                                        onAddToCart = { onAddToCart(dish) },
                                        onIncrement = { onIncrementCart(dish) },
                                        onDecrement = { onDecrementCart(dish) }
                                    )
                                }
                            }
                            if (columns > 1 && rowItems.size < columns) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // "How CampusBite Works" Mobile Steps Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "How CampusBite Works",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Skip canteen rush in 3 easy steps between lectures",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MobileStepItem(
                    stepNumber = "1",
                    title = "Browse & Pre-Order",
                    description = "Choose your favorite dishes, view real-time preparation times, and check live stock.",
                    icon = Icons.Default.Restaurant
                )
                MobileStepItem(
                    stepNumber = "2",
                    title = "Pick Your Break Slot",
                    description = "Schedule pickup during morning recess (10:15 AM) or lunch break (12:30 PM).",
                    icon = Icons.Default.AccessTime
                )
                MobileStepItem(
                    stepNumber = "3",
                    title = "Express Collection at Bay 1",
                    description = "Show your digital token code at Counter Bay 1 and grab hot, freshly packed food instantly.",
                    icon = Icons.Default.QrCode
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Clean Mobile Canteen Info & Support Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(OrangePrimary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = OrangeDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Central Dining Hall",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Block B Central Court • Ground Floor",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Operating Hours",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "Mon – Sat: 7:30 AM – 7:00 PM",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF1E293B)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onNavigateToAbout,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("About", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onNavigateToContact,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Help & Bay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Padding at bottom for floating cart banner and bottom nav bar
        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
private fun MobileStepItem(
    stepNumber: String,
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFFFEDD5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    fontWeight = FontWeight.ExtraBold,
                    color = OrangeDark,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OrangeDark.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
