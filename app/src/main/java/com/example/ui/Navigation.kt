package com.example.ui

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Menu : Screen("menu")
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object OrderConfirmation : Screen("order_confirmation")
    object OrderTracking : Screen("order_tracking")
    object OrdersList : Screen("orders_list")
    object AboutUs : Screen("about_us")
    object Contact : Screen("contact")
    object Auth : Screen("auth")
    object Profile : Screen("profile")
    object AdminDashboard : Screen("admin_dashboard")
    object AdminFoodManage : Screen("admin_food_manage")
}

enum class SortOption(val label: String) {
    POPULARITY("Popular"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    FASTEST_PREP("Fastest Prep Time")
}
