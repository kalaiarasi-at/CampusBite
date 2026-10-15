package com.example.data.model

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int
) {
    val subtotal: Double
        get() = foodItem.price * quantity
}
