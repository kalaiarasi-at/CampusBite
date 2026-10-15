package com.example

import com.example.data.model.OrderStatus
import com.example.data.repository.CampusBiteRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {

    private lateinit var repository: CampusBiteRepository

    @Before
    fun setUp() {
        repository = CampusBiteRepository.getInstance()
        repository.resetDemoData()
        repository.clearCart()
    }

    @Test
    fun testMenuHasAtLeast15Items() {
        val items = repository.foodItems.value
        assertTrue("Menu should contain at least 15 items, found ${items.size}", items.size >= 15)
    }

    @Test
    fun testStudentRegistrationAndProfile() {
        val user = repository.registerStudent(
            name = "Rohan Verma",
            studentId = "STU-2024-9911",
            email = "rohan.v@campusbite.edu",
            department = "Mechanical Engineering",
            yearOfStudy = "2nd Year",
            phoneNumber = "+91 99887 76655"
        )
        assertNotNull(user)
        assertEquals("Rohan Verma", repository.currentUser.value?.name)
        assertEquals("STU-2024-9911", repository.currentUser.value?.studentId)
    }

    @Test
    fun testCartOperationsAndOrderPlacement() {
        val items = repository.foodItems.value
        val dosa = items.find { it.name.contains("Dosa") }
        assertNotNull(dosa)

        repository.addToCart(dosa!!, 2)
        assertEquals(1, repository.cartItems.value.size)
        assertEquals(2, repository.cartItems.value[0].quantity)
        assertEquals(dosa.price * 2, repository.cartItems.value[0].subtotal, 0.01)

        val initialStock = dosa.stock
        val orderResult = repository.placeOrder(
            pickupDate = "Today",
            pickupSlot = "10:15 AM - 10:30 AM",
            specialInstructions = "Crispy please",
            paymentMethod = "Demo UPI Payment Simulation"
        )

        assertTrue(orderResult.isSuccess)
        val order = orderResult.getOrThrow()
        assertNotNull(order)
        assertEquals(0, repository.cartItems.value.size) // Cart cleared

        val updatedDosa = repository.foodItems.value.find { it.id == dosa.id }
        assertEquals(initialStock - 2, updatedDosa?.stock) // Stock deducted
    }

    @Test
    fun testOrderStatusAdvancement() {
        val firstOrder = repository.orders.value.first()
        val success = repository.updateOrderStatus(firstOrder.id, OrderStatus.READY_FOR_PICKUP)
        assertTrue(success)

        val updated = repository.orders.value.find { it.id == firstOrder.id }
        assertEquals(OrderStatus.READY_FOR_PICKUP, updated?.orderStatus)
    }
}
