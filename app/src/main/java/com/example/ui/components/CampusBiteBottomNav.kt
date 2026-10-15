package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.Screen
import com.example.ui.theme.OrangePrimary

@Composable
fun CampusBiteBottomNav(
    currentScreen: Screen,
    userRole: UserRole?,
    cartCount: Int,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        if (userRole == UserRole.ADMIN) {
            NavigationBarItem(
                selected = currentScreen == Screen.AdminDashboard,
                onClick = { onNavigate(Screen.AdminDashboard) },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                label = { Text("Dashboard", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_dashboard"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
            NavigationBarItem(
                selected = currentScreen == Screen.AdminFoodManage,
                onClick = { onNavigate(Screen.AdminFoodManage) },
                icon = { Icon(Icons.Default.Fastfood, contentDescription = "Manage Food") },
                label = { Text("Menu Editor", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_food_manage"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
            NavigationBarItem(
                selected = currentScreen == Screen.Home,
                onClick = { onNavigate(Screen.Home) },
                icon = { Icon(Icons.Default.Storefront, contentDescription = "Student View") },
                label = { Text("Student View", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_admin_to_student_view"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
        } else {
            NavigationBarItem(
                selected = currentScreen == Screen.Home,
                onClick = { onNavigate(Screen.Home) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_home"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
            NavigationBarItem(
                selected = currentScreen == Screen.Menu,
                onClick = { onNavigate(Screen.Menu) },
                icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = "Food Menu") },
                label = { Text("Menu", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_menu"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
            NavigationBarItem(
                selected = currentScreen == Screen.Cart,
                onClick = { onNavigate(Screen.Cart) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(
                                    containerColor = OrangePrimary,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = cartCount.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                },
                label = { Text("Cart", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_cart"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
            NavigationBarItem(
                selected = currentScreen == Screen.OrdersList,
                onClick = { onNavigate(Screen.OrdersList) },
                icon = { Icon(Icons.Default.ReceiptLong, contentDescription = "My Orders") },
                label = { Text("Orders", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_orders"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
            NavigationBarItem(
                selected = currentScreen == Screen.Profile,
                onClick = { onNavigate(Screen.Profile) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile", fontSize = 11.sp) },
                modifier = Modifier.testTag("nav_profile"),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OrangePrimary,
                    selectedTextColor = OrangePrimary,
                    indicatorColor = OrangePrimary.copy(alpha = 0.15f)
                )
            )
        }
    }
}
