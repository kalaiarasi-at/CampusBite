package com.example.data.model

enum class OrderStatus(val displayName: String, val stepIndex: Int) {
    PLACED("Order Placed", 0),
    ACCEPTED("Accepted by Canteen", 1),
    PREPARING("Preparing in Kitchen", 2),
    READY_FOR_PICKUP("Ready for Pickup", 3),
    COMPLETED("Picked Up & Completed", 4),
    CANCELLED("Cancelled", -1)
}

data class OrderItemSnapshot(
    val foodId: String,
    val foodName: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val isVegetarian: Boolean
)

data class Order(
    val id: String,
    val tokenNumber: Int,
    val userId: String,
    val customerName: String,
    val customerStudentId: String,
    val customerEmail: String = "",
    val customerPhone: String = "",
    val customerDepartment: String = "",
    val items: List<OrderItemSnapshot>,
    val pickupDate: String,
    val pickupSlot: String,
    val specialInstructions: String = "",
    val subtotal: Double,
    val packagingFee: Double = 5.0,
    val total: Double,
    val paymentMethod: String, // "Pay at Canteen Counter" or "Demo UPI Payment (Simulated)"
    val paymentStatus: String, // "Pending" or "Paid (Demo UPI)"
    val orderStatus: OrderStatus = OrderStatus.PLACED,
    val formattedDateTime: String = "",
    val estimatedMinutes: Int = 15,
    val cancellationReason: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
