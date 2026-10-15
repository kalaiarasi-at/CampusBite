package com.example.data.model

enum class FoodCategory(val displayName: String) {
    ALL("All Items"),
    BREAKFAST("Breakfast"),
    MEALS("Meals"),
    SNACKS("Snacks"),
    BEVERAGES("Beverages"),
    FAST_FOOD("Fast Food"),
    HEALTHY("Healthy Options")
}

data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val category: FoodCategory,
    val price: Double,
    val image: String = "",
    val available: Boolean = true,
    val stock: Int = 20,
    val preparationTime: Int = 10, // minutes
    val isVegetarian: Boolean = true,
    val isTodaySpecial: Boolean = false,
    val rating: Float = 4.6f
)
