package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.CampusBiteViewModel
import com.example.ui.Screen
import com.example.ui.components.CampusBiteBottomNav
import com.example.ui.components.CampusBiteTopBar
import com.example.ui.components.FoodDetailDialog
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.AdminFoodManageScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.ContactScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.screens.OrderConfirmationScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.OrdersListScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CampusBiteApp()
            }
        }
    }
}

@Composable
fun CampusBiteApp(viewModel: CampusBiteViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val cartCount by viewModel.cartCount.collectAsState()
    val cartSubtotal by viewModel.cartSubtotal.collectAsState()
    val foodItems by viewModel.foodItems.collectAsState()
    val filteredFoodItems by viewModel.filteredFoodItems.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val userOrders by viewModel.userOrders.collectAsState()
    val selectedFoodItem by viewModel.selectedFoodItem.collectAsState()
    val selectedOrder by viewModel.selectedOrder.collectAsState()
    val lastPlacedOrder by viewModel.lastPlacedOrder.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val vegOnlyFilter by viewModel.vegOnlyFilter.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Top-level tabs for back press handling
    val isTopLevelScreen = currentScreen == Screen.Home ||
            currentScreen == Screen.AdminDashboard ||
            (currentUser?.role != UserRole.ADMIN && (
                currentScreen == Screen.Menu ||
                currentScreen == Screen.OrdersList ||
                currentScreen == Screen.Profile
            ))

    BackHandler(enabled = !isTopLevelScreen) {
        if (!viewModel.navigateBack()) {
            if (currentUser?.role == UserRole.ADMIN) {
                viewModel.navigateTo(Screen.AdminDashboard)
            } else {
                viewModel.navigateTo(Screen.Home)
            }
        }
    }

    // Modal dialog for food item details
    selectedFoodItem?.let { dish ->
        FoodDetailDialog(
            foodItem = dish,
            onDismiss = { viewModel.dismissFoodDetails() },
            onAddToCart = { quantity ->
                viewModel.addToCart(dish, quantity)
            }
        )
    }

    // Top bar titles and subtitles
    val topBarTitle = when (currentScreen) {
        Screen.Home -> "CampusBite"
        Screen.Menu -> "Canteen Menu"
        Screen.Cart -> "Your Tray"
        Screen.Checkout -> "Pickup & Checkout"
        Screen.OrderConfirmation -> "Order Placed"
        Screen.OrderTracking -> "Order Tracking"
        Screen.OrdersList -> "My Orders"
        Screen.AboutUs -> "About CampusBite"
        Screen.Contact -> "Canteen Support"
        Screen.Auth -> "Student Portal"
        Screen.Profile -> "Profile"
        Screen.AdminDashboard -> "Kitchen Console"
        Screen.AdminFoodManage -> "Menu Editor"
    }

    val topBarSubtitle = when (currentScreen) {
        Screen.Home -> "Central Canteen • Block B"
        Screen.Menu -> "${foodItems.size} items available"
        Screen.Cart -> if (cartCount > 0) "$cartCount items • ₹${cartSubtotal.toInt()}" else "Empty Tray"
        Screen.Checkout -> "Select Class Break Slot"
        Screen.OrderConfirmation -> "Token ready for collection"
        Screen.OrderTracking -> {
            val orderNum = selectedOrder?.tokenNumber ?: lastPlacedOrder?.tokenNumber
            if (orderNum != null) "Token #$orderNum" else "Active Token"
        }
        Screen.OrdersList -> "${userOrders.size} total orders"
        Screen.AboutUs -> "Smart Canteen Pre-Orders"
        Screen.Contact -> "Counter Bay 1 & Help"
        Screen.Auth -> "Quick sign in or registration"
        Screen.Profile -> currentUser?.studentId ?: "Student Account"
        Screen.AdminDashboard -> "Live Student Orders"
        Screen.AdminFoodManage -> "Update stock & pricing"
    }

    val showBackButton = !isTopLevelScreen

    val shouldShowBottomNav = when (currentScreen) {
        Screen.Checkout, Screen.OrderConfirmation, Screen.OrderTracking, Screen.Auth -> false
        else -> true
    }

    Scaffold(
        topBar = {
            CampusBiteTopBar(
                title = topBarTitle,
                subtitle = topBarSubtitle,
                showBackButton = showBackButton,
                onBackClick = {
                    if (!viewModel.navigateBack()) {
                        viewModel.navigateTo(Screen.Home)
                    }
                },
                cartItemCount = cartCount,
                onCartClick = { viewModel.navigateTo(Screen.Cart) },
                userRole = currentUser?.role,
                onRoleToggleClick = {
                    if (currentUser?.role == UserRole.ADMIN) {
                        viewModel.loginStudent("aarav.sharma@campusbite.edu", "Aarav Sharma", "STU-2024-8842")
                        viewModel.navigateTo(Screen.Home)
                    } else {
                        viewModel.loginAdmin()
                        viewModel.navigateTo(Screen.AdminDashboard)
                    }
                },
                onAboutClick = { viewModel.navigateTo(Screen.AboutUs) },
                onContactClick = { viewModel.navigateTo(Screen.Contact) },
                onResetDemoDataClick = { viewModel.resetDemoData() },
                onLogoutClick = { viewModel.logout() }
            )
        },
        bottomBar = {
            if (shouldShowBottomNav) {
                CampusBiteBottomNav(
                    currentScreen = currentScreen,
                    userRole = currentUser?.role,
                    cartCount = cartCount,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Home -> {
                    HomeScreen(
                        currentUser = currentUser,
                        foodItems = foodItems,
                        cartItems = cartItems,
                        activeOrders = userOrders,
                        onNavigateToMenu = { category ->
                            if (category != null) {
                                viewModel.setSelectedCategory(category)
                            }
                            viewModel.navigateTo(Screen.Menu)
                        },
                        onNavigateToCart = { viewModel.navigateTo(Screen.Cart) },
                        onNavigateToTracking = { order ->
                            viewModel.selectOrderForTracking(order)
                        },
                        onFoodCardClick = { dish ->
                            viewModel.showFoodDetails(dish)
                        },
                        onAddToCart = { dish ->
                            viewModel.addToCart(dish)
                        },
                        onIncrementCart = { dish ->
                            val currentQty = cartItems.find { it.foodItem.id == dish.id }?.quantity ?: 0
                            viewModel.updateCartQuantity(dish.id, currentQty + 1)
                        },
                        onDecrementCart = { dish ->
                            val currentQty = cartItems.find { it.foodItem.id == dish.id }?.quantity ?: 0
                            viewModel.updateCartQuantity(dish.id, currentQty - 1)
                        },
                        onNavigateToAbout = { viewModel.navigateTo(Screen.AboutUs) },
                        onNavigateToContact = { viewModel.navigateTo(Screen.Contact) }
                    )
                }

                Screen.Menu -> {
                    MenuScreen(
                        foodItems = filteredFoodItems,
                        cartItems = cartItems,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.setSelectedCategory(it) },
                        vegOnlyFilter = vegOnlyFilter,
                        onToggleVegOnly = { viewModel.setVegOnlyFilter(it) },
                        sortOption = sortOption,
                        onSelectSortOption = { viewModel.setSortOption(it) },
                        onFoodCardClick = { dish ->
                            viewModel.showFoodDetails(dish)
                        },
                        onAddToCart = { dish ->
                            viewModel.addToCart(dish)
                        },
                        onIncrementCart = { dish ->
                            val currentQty = cartItems.find { it.foodItem.id == dish.id }?.quantity ?: 0
                            viewModel.updateCartQuantity(dish.id, currentQty + 1)
                        },
                        onDecrementCart = { dish ->
                            val currentQty = cartItems.find { it.foodItem.id == dish.id }?.quantity ?: 0
                            viewModel.updateCartQuantity(dish.id, currentQty - 1)
                        }
                    )
                }

                Screen.Cart -> {
                    CartScreen(
                        cartItems = cartItems,
                        onUpdateQuantity = { foodId, newQty ->
                            viewModel.updateCartQuantity(foodId, newQty)
                        },
                        onRemoveItem = { foodId ->
                            viewModel.removeCartItem(foodId)
                        },
                        onClearCart = {
                            viewModel.clearCart()
                        },
                        onBrowseMenu = {
                            viewModel.navigateTo(Screen.Menu)
                        },
                        onProceedToCheckout = {
                            if (currentUser == null) {
                                viewModel.navigateTo(Screen.Auth)
                            } else {
                                viewModel.navigateTo(Screen.Checkout)
                            }
                        }
                    )
                }

                Screen.Checkout -> {
                    CheckoutScreen(
                        currentUser = currentUser,
                        cartItems = cartItems,
                        onRequireLogin = { viewModel.navigateTo(Screen.Auth) },
                        onConfirmOrder = { pickupDate, pickupSlot, instructions, paymentMethod ->
                            viewModel.placeOrder(
                                pickupDate = pickupDate,
                                pickupSlot = pickupSlot,
                                specialInstructions = instructions,
                                paymentMethod = paymentMethod
                            )
                        }
                    )
                }

                Screen.OrderConfirmation -> {
                    val orderToShow = lastPlacedOrder ?: userOrders.firstOrNull() ?: orders.firstOrNull()
                    if (orderToShow != null) {
                        OrderConfirmationScreen(
                            order = orderToShow,
                            onTrackOrderClick = {
                                viewModel.selectOrderForTracking(orderToShow)
                            },
                            onBackToMenuClick = {
                                viewModel.navigateTo(Screen.Menu)
                            }
                        )
                    } else {
                        viewModel.navigateTo(Screen.Home)
                    }
                }

                Screen.OrderTracking -> {
                    val orderToTrack = selectedOrder ?: userOrders.firstOrNull() ?: orders.firstOrNull()
                    if (orderToTrack != null) {
                        OrderTrackingScreen(
                            order = orderToTrack,
                            onBackToOrders = {
                                viewModel.navigateTo(Screen.OrdersList)
                            }
                        )
                    } else {
                        viewModel.navigateTo(Screen.OrdersList)
                    }
                }

                Screen.OrdersList -> {
                    OrdersListScreen(
                        orders = userOrders,
                        onSelectOrderForTracking = { order ->
                            viewModel.selectOrderForTracking(order)
                        },
                        onQuickReorder = { order ->
                            viewModel.quickReorder(order)
                        },
                        onBrowseMenu = {
                            viewModel.navigateTo(Screen.Menu)
                        }
                    )
                }

                Screen.AboutUs -> {
                    AboutScreen(
                        onExploreMenuClick = { viewModel.navigateTo(Screen.Menu) },
                        onContactClick = { viewModel.navigateTo(Screen.Contact) }
                    )
                }

                Screen.Contact -> {
                    ContactScreen(
                        currentUser = currentUser,
                        onSendMessage = { _ -> }
                    )
                }

                Screen.Auth -> {
                    AuthScreen(
                        onStudentLoginSuccess = { email, name, studentId ->
                            viewModel.loginStudent(email, name, studentId)
                        },
                        onStudentRegisterSuccess = { name, studentId, email, dept, year, phone ->
                            viewModel.registerStudent(name, studentId, email, dept, year, phone)
                        },
                        onAdminLoginSuccess = {
                            viewModel.loginAdmin()
                        }
                    )
                }

                Screen.Profile -> {
                    ProfileScreen(
                        currentUser = currentUser,
                        orders = userOrders,
                        onUpdateProfile = { name, studentId, dept ->
                            viewModel.updateProfile(name, studentId, dept, currentUser?.yearOfStudy ?: "3rd Year", currentUser?.phoneNumber ?: "")
                        },
                        onNavigateToOrders = {
                            viewModel.navigateTo(Screen.OrdersList)
                        },
                        onNavigateToAdmin = {
                            viewModel.loginAdmin()
                            viewModel.navigateTo(Screen.AdminDashboard)
                        },
                        onResetDemoData = {
                            viewModel.resetDemoData()
                        },
                        onLogout = {
                            viewModel.logout()
                        }
                    )
                }

                Screen.AdminDashboard -> {
                    AdminDashboardScreen(
                        orders = orders,
                        onUpdateOrderStatus = { orderId, newStatus, reason ->
                            viewModel.updateOrderStatus(orderId, newStatus, reason)
                        },
                        onNavigateToFoodManage = {
                            viewModel.navigateTo(Screen.AdminFoodManage)
                        }
                    )
                }

                Screen.AdminFoodManage -> {
                    AdminFoodManageScreen(
                        foodItems = foodItems,
                        onAddFoodItem = { newItem ->
                            viewModel.addFoodItem(newItem)
                        },
                        onUpdateFoodItem = { updatedItem ->
                            viewModel.updateFoodItem(updatedItem)
                        },
                        onDeleteFoodItem = { foodId ->
                            viewModel.deleteFoodItem(foodId)
                        }
                    )
                }
            }

            // Floating Cart Notification / Action Bar
            AnimatedVisibility(
                visible = cartCount > 0 && (currentScreen == Screen.Home || currentScreen == Screen.Menu),
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Surface(
                    onClick = { viewModel.navigateTo(Screen.Cart) },
                    shape = RoundedCornerShape(16.dp),
                    color = OrangePrimary,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("floating_cart_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = OrangeDark,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "$cartCount item${if (cartCount > 1) "s" else ""} in Tray",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "₹${cartSubtotal.toInt()} • Tap to Checkout",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.88f)
                                )
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "View Tray",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
