package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CartItem
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.model.Order
import com.example.data.model.OrderItemSnapshot
import com.example.data.model.OrderStatus
import com.example.data.model.User
import com.example.data.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class CampusBiteRepository private constructor(context: Context? = null) {

    private val prefs: SharedPreferences? = context?.getSharedPreferences("campusbite_db", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: CampusBiteRepository? = null

        fun getInstance(context: Context? = null): CampusBiteRepository {
            return instance ?: synchronized(this) {
                instance ?: CampusBiteRepository(context?.applicationContext).also { instance = it }
            }
        }

        val DEFAULT_STUDENT = User(
            id = "usr_student_01",
            name = "Aarav Sharma",
            studentId = "STU-2024-8842",
            email = "aarav.sharma@campusbite.edu",
            department = "Computer Science & Engineering",
            yearOfStudy = "3rd Year",
            phoneNumber = "+91 98765 43210",
            role = UserRole.STUDENT
        )

        val DEFAULT_ADMIN = User(
            id = "usr_admin_01",
            name = "Priya Verma",
            studentId = "STAFF-ADM-01",
            email = "admin@campusbite.com",
            department = "Campus Dining & Canteen Operations",
            yearOfStudy = "Faculty Staff",
            phoneNumber = "+91 91234 56789",
            role = UserRole.ADMIN
        )
    }

    private val _currentUser = MutableStateFlow<User?>(DEFAULT_STUDENT)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _foodItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val foodItems: StateFlow<List<FoodItem>> = _foodItems.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private var nextTokenNumber = 50

    init {
        loadDataOrDefaults()
    }

    private fun loadDataOrDefaults() {
        val savedDishesJson = prefs?.getString("food_items", null)
        val savedOrdersJson = prefs?.getString("orders", null)
        val savedUserJson = prefs?.getString("current_user", null)

        if (savedUserJson != null) {
            try {
                val uObj = JSONObject(savedUserJson)
                _currentUser.value = User(
                    id = uObj.getString("id"),
                    name = uObj.getString("name"),
                    studentId = uObj.optString("studentId", ""),
                    email = uObj.getString("email"),
                    department = uObj.optString("department", "Computer Science"),
                    yearOfStudy = uObj.optString("yearOfStudy", "3rd Year"),
                    phoneNumber = uObj.optString("phoneNumber", ""),
                    role = if (uObj.optString("role") == "ADMIN") UserRole.ADMIN else UserRole.STUDENT,
                    createdAt = uObj.optLong("createdAt", System.currentTimeMillis())
                )
            } catch (e: Exception) {
                _currentUser.value = DEFAULT_STUDENT
            }
        } else {
            _currentUser.value = DEFAULT_STUDENT
        }

        if (savedDishesJson != null) {
            try {
                val arr = JSONArray(savedDishesJson)
                val list = mutableListOf<FoodItem>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(
                        FoodItem(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            description = obj.getString("description"),
                            category = FoodCategory.valueOf(obj.getString("category")),
                            price = obj.getDouble("price"),
                            available = obj.getBoolean("available"),
                            stock = obj.getInt("stock"),
                            preparationTime = obj.getInt("preparationTime"),
                            isVegetarian = obj.getBoolean("isVegetarian"),
                            isTodaySpecial = obj.optBoolean("isTodaySpecial", false),
                            rating = obj.optDouble("rating", 4.7).toFloat()
                        )
                    )
                }
                _foodItems.value = list
            } catch (e: Exception) {
                seedInitialDishes()
            }
        } else {
            seedInitialDishes()
        }

        if (savedOrdersJson != null) {
            try {
                val arr = JSONArray(savedOrdersJson)
                val list = mutableListOf<Order>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    val itemsArr = obj.getJSONArray("items")
                    val itemsList = mutableListOf<OrderItemSnapshot>()
                    for (j in 0 until itemsArr.length()) {
                        val itemObj = itemsArr.getJSONObject(j)
                        itemsList.add(
                            OrderItemSnapshot(
                                foodId = itemObj.getString("foodId"),
                                foodName = itemObj.getString("foodName"),
                                quantity = itemObj.getInt("quantity"),
                                unitPrice = itemObj.getDouble("unitPrice"),
                                subtotal = itemObj.getDouble("subtotal"),
                                isVegetarian = itemObj.getBoolean("isVegetarian")
                            )
                        )
                    }

                    list.add(
                        Order(
                            id = obj.getString("id"),
                            tokenNumber = obj.getInt("tokenNumber"),
                            userId = obj.getString("userId"),
                            customerName = obj.getString("customerName"),
                            customerStudentId = obj.getString("customerStudentId"),
                            customerEmail = obj.optString("customerEmail", ""),
                            customerPhone = obj.optString("customerPhone", ""),
                            customerDepartment = obj.optString("customerDepartment", ""),
                            items = itemsList,
                            pickupDate = obj.getString("pickupDate"),
                            pickupSlot = obj.getString("pickupSlot"),
                            specialInstructions = obj.optString("specialInstructions", ""),
                            subtotal = obj.getDouble("subtotal"),
                            packagingFee = obj.optDouble("packagingFee", 5.0),
                            total = obj.getDouble("total"),
                            paymentMethod = obj.getString("paymentMethod"),
                            paymentStatus = obj.getString("paymentStatus"),
                            orderStatus = OrderStatus.valueOf(obj.getString("orderStatus")),
                            formattedDateTime = obj.optString("formattedDateTime", ""),
                            estimatedMinutes = obj.optInt("estimatedMinutes", 15),
                            cancellationReason = if (obj.has("cancellationReason") && !obj.isNull("cancellationReason")) obj.getString("cancellationReason") else null,
                            createdAt = obj.getLong("createdAt"),
                            updatedAt = obj.getLong("updatedAt")
                        )
                    )
                }
                _orders.value = list
            } catch (e: Exception) {
                seedInitialOrders()
            }
        } else {
            seedInitialOrders()
        }
    }

    private fun persistDishes() {
        prefs?.let { sp ->
            try {
                val arr = JSONArray()
                _foodItems.value.forEach { item ->
                    val obj = JSONObject()
                    obj.put("id", item.id)
                    obj.put("name", item.name)
                    obj.put("description", item.description)
                    obj.put("category", item.category.name)
                    obj.put("price", item.price)
                    obj.put("available", item.available)
                    obj.put("stock", item.stock)
                    obj.put("preparationTime", item.preparationTime)
                    obj.put("isVegetarian", item.isVegetarian)
                    obj.put("isTodaySpecial", item.isTodaySpecial)
                    obj.put("rating", item.rating)
                    arr.put(obj)
                }
                sp.edit().putString("food_items", arr.toString()).apply()
            } catch (ignored: Exception) {}
        }
    }

    private fun persistOrders() {
        prefs?.let { sp ->
            try {
                val arr = JSONArray()
                _orders.value.forEach { order ->
                    val obj = JSONObject()
                    obj.put("id", order.id)
                    obj.put("tokenNumber", order.tokenNumber)
                    obj.put("userId", order.userId)
                    obj.put("customerName", order.customerName)
                    obj.put("customerStudentId", order.customerStudentId)
                    obj.put("customerEmail", order.customerEmail)
                    obj.put("customerPhone", order.customerPhone)
                    obj.put("customerDepartment", order.customerDepartment)
                    obj.put("pickupDate", order.pickupDate)
                    obj.put("pickupSlot", order.pickupSlot)
                    obj.put("specialInstructions", order.specialInstructions)
                    obj.put("subtotal", order.subtotal)
                    obj.put("packagingFee", order.packagingFee)
                    obj.put("total", order.total)
                    obj.put("paymentMethod", order.paymentMethod)
                    obj.put("paymentStatus", order.paymentStatus)
                    obj.put("orderStatus", order.orderStatus.name)
                    obj.put("formattedDateTime", order.formattedDateTime)
                    obj.put("estimatedMinutes", order.estimatedMinutes)
                    obj.put("cancellationReason", order.cancellationReason)
                    obj.put("createdAt", order.createdAt)
                    obj.put("updatedAt", order.updatedAt)

                    val itemsArr = JSONArray()
                    order.items.forEach { itm ->
                        val itmObj = JSONObject()
                        itmObj.put("foodId", itm.foodId)
                        itmObj.put("foodName", itm.foodName)
                        itmObj.put("quantity", itm.quantity)
                        itmObj.put("unitPrice", itm.unitPrice)
                        itmObj.put("subtotal", itm.subtotal)
                        itmObj.put("isVegetarian", itm.isVegetarian)
                        itemsArr.put(itmObj)
                    }
                    obj.put("items", itemsArr)
                    arr.put(obj)
                }
                sp.edit().putString("orders", arr.toString()).apply()
            } catch (ignored: Exception) {}
        }
    }

    private fun persistUser(user: User?) {
        prefs?.let { sp ->
            if (user == null) {
                sp.edit().remove("current_user").apply()
            } else {
                val obj = JSONObject()
                obj.put("id", user.id)
                obj.put("name", user.name)
                obj.put("studentId", user.studentId)
                obj.put("email", user.email)
                obj.put("department", user.department)
                obj.put("yearOfStudy", user.yearOfStudy)
                obj.put("phoneNumber", user.phoneNumber)
                obj.put("role", user.role.name)
                obj.put("createdAt", user.createdAt)
                sp.edit().putString("current_user", obj.toString()).apply()
            }
        }
    }

    private fun seedInitialDishes() {
        val initialDishes = listOf(
            FoodItem("dish_01", "Steamed Idli (2 Pcs)", "Soft fermented rice cakes served with aromatic spiced sambar & fresh coconut chutney.", FoodCategory.BREAKFAST, 35.0, stock = 40, preparationTime = 5, isVegetarian = true, rating = 4.8f),
            FoodItem("dish_02", "Crispy Masala Dosa", "Golden fermented crepe filled with spiced potato bhaji, served with sambar & chutneys.", FoodCategory.BREAKFAST, 60.0, stock = 30, preparationTime = 12, isVegetarian = true, isTodaySpecial = true, rating = 4.9f),
            FoodItem("dish_03", "Ghee Ven Pongal & Medu Vada", "Traditional rice & yellow moong dal cooked in pure cow ghee with roasted cashew nuts & crispy vada.", FoodCategory.BREAKFAST, 50.0, stock = 25, preparationTime = 8, isVegetarian = true, rating = 4.7f),
            FoodItem("dish_04", "Poori Masala (3 Pcs)", "Golden puffed whole wheat pooris served with savory potato-onion saagu.", FoodCategory.BREAKFAST, 55.0, stock = 20, preparationTime = 10, isVegetarian = true, rating = 4.6f),
            FoodItem("dish_05", "Executive South Indian Veg Thali", "Unlimited campus lunch with steamed basmati rice, sambar, rasam, kootu, poriyal, curd & appalam.", FoodCategory.MEALS, 90.0, stock = 50, preparationTime = 5, isVegetarian = true, rating = 4.8f),
            FoodItem("dish_06", "Tangy Lemon Rice", "Tempered rice infused with fresh lemon juice, mustard seeds, curry leaves & crunchy roasted peanuts.", FoodCategory.MEALS, 45.0, stock = 25, preparationTime = 5, isVegetarian = true, rating = 4.5f),
            FoodItem("dish_07", "Cooling Curd Rice", "Homestyle seasoned yogurt rice tempered with green chillies, mustard & fresh pomegranate pearls.", FoodCategory.MEALS, 45.0, stock = 30, preparationTime = 5, isVegetarian = true, rating = 4.6f),
            FoodItem("dish_08", "Ghee Sambar Rice", "Classic comfort rice and lentils cooked with drumsticks, carrots & pure melted ghee.", FoodCategory.MEALS, 50.0, stock = 20, preparationTime = 6, isVegetarian = true, rating = 4.7f),
            FoodItem("dish_09", "Grilled Veg Cheese Sandwich", "Golden toasted bread layered with cheese slice, crisp capsicum, cucumber, tomato & mint chutney.", FoodCategory.SNACKS, 65.0, stock = 35, preparationTime = 10, isVegetarian = true, rating = 4.8f),
            FoodItem("dish_10", "Crispy Bakery Veg Puff", "Freshly baked flaky pastry filled with mildly spiced potato, peas and diced carrots.", FoodCategory.SNACKS, 25.0, stock = 45, preparationTime = 3, isVegetarian = true, rating = 4.6f),
            FoodItem("dish_11", "Punjabi Samosa (2 Pcs)", "Hot crispy pyramid pastries stuffed with spiced potatoes & peas, served with sweet imli & mint dip.", FoodCategory.SNACKS, 30.0, stock = 40, preparationTime = 4, isVegetarian = true, rating = 4.7f),
            FoodItem("dish_12", "Crispy French Fries", "Golden salted potato fries tossed in mild peri peri spices and herbs, served with dip.", FoodCategory.FAST_FOOD, 55.0, stock = 28, preparationTime = 8, isVegetarian = true, rating = 4.5f),
            FoodItem("dish_13", "Cheesy Veggie Crunch Burger", "Crispy herb potato patty in a toasted sesame bun with melted cheese, lettuce & burger sauce.", FoodCategory.FAST_FOOD, 70.0, stock = 22, preparationTime = 12, isVegetarian = true, rating = 4.7f),
            FoodItem("dish_14", "South Indian Filter Coffee", "Traditional chicory-blend aromatic South Indian filter coffee frothed with hot creamy milk.", FoodCategory.BEVERAGES, 25.0, stock = 60, preparationTime = 4, isVegetarian = true, rating = 4.9f),
            FoodItem("dish_15", "Kadak Masala Chai", "Freshly brewed hot milk tea infused with crushed ginger, green cardamom and cloves.", FoodCategory.BEVERAGES, 20.0, stock = 60, preparationTime = 4, isVegetarian = true, rating = 4.8f),
            FoodItem("dish_16", "Chilled Fresh Lime Soda", "Refreshing hand-squeezed lime juice served sparkling chilled (sweet & salted mix).", FoodCategory.BEVERAGES, 30.0, stock = 35, preparationTime = 5, isVegetarian = true, rating = 4.6f),
            FoodItem("dish_17", "Spicy Paneer Tikka Frankie", "Whole wheat wrap filled with juicy spiced paneer cubes, chopped onions & chatpata herbs.", FoodCategory.FAST_FOOD, 75.0, stock = 20, preparationTime = 12, isVegetarian = true, rating = 4.8f),
            FoodItem("dish_18", "Steamed Sweet Corn Chaat", "Tender sweet corn kernels tossed in dairy butter, chaat masala, lemon zest and herbs.", FoodCategory.HEALTHY, 40.0, stock = 25, preparationTime = 6, isVegetarian = true, rating = 4.7f)
        )
        _foodItems.value = initialDishes
        persistDishes()
    }

    private fun seedInitialOrders() {
        val currentTime = System.currentTimeMillis()
        val dtFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

        val seedOrders = listOf(
            Order(
                id = "CB-1042",
                tokenNumber = 42,
                userId = DEFAULT_STUDENT.id,
                customerName = DEFAULT_STUDENT.name,
                customerStudentId = DEFAULT_STUDENT.studentId,
                customerEmail = DEFAULT_STUDENT.email,
                customerPhone = DEFAULT_STUDENT.phoneNumber,
                customerDepartment = DEFAULT_STUDENT.department,
                items = listOf(
                    OrderItemSnapshot("dish_02", "Crispy Masala Dosa", 1, 60.0, 60.0, true),
                    OrderItemSnapshot("dish_14", "South Indian Filter Coffee", 1, 25.0, 25.0, true)
                ),
                pickupDate = "Today",
                pickupSlot = "10:15 AM - 10:30 AM (Morning Break)",
                specialInstructions = "Please make the dosa extra crispy.",
                subtotal = 85.0,
                packagingFee = 5.0,
                total = 90.0,
                paymentMethod = "Demo UPI Payment (Simulated)",
                paymentStatus = "Paid (Demo UPI)",
                orderStatus = OrderStatus.PREPARING,
                formattedDateTime = dtFormat.format(Date(currentTime - 15 * 60 * 1000)),
                estimatedMinutes = 8,
                createdAt = currentTime - 15 * 60 * 1000
            ),
            Order(
                id = "CB-1038",
                tokenNumber = 38,
                userId = DEFAULT_STUDENT.id,
                customerName = DEFAULT_STUDENT.name,
                customerStudentId = DEFAULT_STUDENT.studentId,
                customerEmail = DEFAULT_STUDENT.email,
                customerPhone = DEFAULT_STUDENT.phoneNumber,
                customerDepartment = DEFAULT_STUDENT.department,
                items = listOf(
                    OrderItemSnapshot("dish_09", "Grilled Veg Cheese Sandwich", 1, 65.0, 65.0, true),
                    OrderItemSnapshot("dish_16", "Chilled Fresh Lime Soda", 1, 30.0, 30.0, true)
                ),
                pickupDate = "Yesterday",
                pickupSlot = "03:45 PM - 04:00 PM (Tea Break)",
                subtotal = 95.0,
                packagingFee = 5.0,
                total = 100.0,
                paymentMethod = "Pay at Canteen Counter",
                paymentStatus = "Paid at Counter",
                orderStatus = OrderStatus.COMPLETED,
                formattedDateTime = dtFormat.format(Date(currentTime - 24 * 60 * 60 * 1000)),
                createdAt = currentTime - 24 * 60 * 60 * 1000
            )
        )
        _orders.value = seedOrders
        persistOrders()
    }

    fun resetDemoData() {
        seedInitialDishes()
        seedInitialOrders()
        _currentUser.value = DEFAULT_STUDENT
        persistUser(_currentUser.value)
    }

    fun registerStudent(
        name: String,
        studentId: String,
        email: String,
        department: String,
        yearOfStudy: String,
        phoneNumber: String
    ): User {
        val newUser = User(
            id = "usr_${System.currentTimeMillis()}",
            name = name.trim(),
            studentId = studentId.trim(),
            email = email.trim(),
            department = department.trim(),
            yearOfStudy = yearOfStudy.trim(),
            phoneNumber = phoneNumber.trim(),
            role = UserRole.STUDENT
        )
        _currentUser.value = newUser
        persistUser(newUser)
        return newUser
    }

    fun loginStudent(email: String, name: String = "", studentId: String = ""): Boolean {
        val user = User(
            id = "usr_${System.currentTimeMillis()}",
            name = if (name.isNotBlank()) name else "Aarav Sharma",
            studentId = if (studentId.isNotBlank()) studentId else "STU-2024-8842",
            email = email.trim(),
            department = "Computer Science & Engineering",
            yearOfStudy = "3rd Year",
            phoneNumber = "+91 98765 43210",
            role = UserRole.STUDENT
        )
        _currentUser.value = user
        persistUser(user)
        return true
    }

    fun loginAdmin(): Boolean {
        _currentUser.value = DEFAULT_ADMIN
        persistUser(DEFAULT_ADMIN)
        return true
    }

    fun logout() {
        _currentUser.value = null
        persistUser(null)
    }

    fun updateProfile(name: String, studentId: String, department: String, yearOfStudy: String, phoneNumber: String) {
        val user = _currentUser.value ?: return
        val updated = user.copy(
            name = name.trim(),
            studentId = studentId.trim(),
            department = department.trim(),
            yearOfStudy = yearOfStudy.trim(),
            phoneNumber = phoneNumber.trim()
        )
        _currentUser.value = updated
        persistUser(updated)
    }

    fun addToCart(foodItem: FoodItem, quantity: Int = 1) {
        if (!foodItem.available || foodItem.stock <= 0) return
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.foodItem.id == foodItem.id }
        if (index != -1) {
            val existing = current[index]
            val newQty = (existing.quantity + quantity).coerceAtMost(foodItem.stock)
            current[index] = existing.copy(quantity = newQty)
        } else {
            val validQty = quantity.coerceIn(1, foodItem.stock)
            current.add(CartItem(foodItem = foodItem, quantity = validQty))
        }
        _cartItems.value = current
    }

    fun updateCartItemQuantity(foodId: String, newQuantity: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.foodItem.id == foodId }
        if (index != -1) {
            if (newQuantity <= 0) {
                current.removeAt(index)
            } else {
                val item = current[index]
                val safeQty = newQuantity.coerceAtMost(item.foodItem.stock)
                current[index] = item.copy(quantity = safeQty)
            }
            _cartItems.value = current
        }
    }

    fun removeCartItem(foodId: String) {
        val current = _cartItems.value.toMutableList()
        current.removeAll { it.foodItem.id == foodId }
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    /**
     * Atomic, trusted order placement:
     * - Recalculates prices using fresh trusted data
     * - Validates item stock
     * - Safely decrements stock
     * - Generates order ID and token
     * - Saves order and clears cart
     */
    fun placeOrder(
        pickupDate: String,
        pickupSlot: String,
        specialInstructions: String,
        paymentMethod: String
    ): Result<Order> {
        val cart = _cartItems.value
        if (cart.isEmpty()) {
            return Result.failure(IllegalStateException("Your cart is empty. Please add items from the menu."))
        }

        val user = _currentUser.value
        if (user == null) {
            return Result.failure(IllegalStateException("You must be logged in to place a canteen order."))
        }

        // STEP 3 & 4 & 5: Retrieve trusted food items and validate
        val currentDishes = _foodItems.value.toMutableList()
        var trustedSubtotal = 0.0
        val itemsSnapshot = mutableListOf<OrderItemSnapshot>()

        for (cItem in cart) {
            val trustedDish = currentDishes.find { it.id == cItem.foodItem.id }
                ?: return Result.failure(IllegalStateException("${cItem.foodItem.name} is no longer on the menu."))

            if (!trustedDish.available || trustedDish.stock <= 0) {
                return Result.failure(IllegalStateException("${trustedDish.name} is currently out of stock."))
            }

            if (cItem.quantity > trustedDish.stock) {
                return Result.failure(IllegalStateException("Only ${trustedDish.stock} available for ${trustedDish.name}."))
            }

            val itemTotal = trustedDish.price * cItem.quantity
            trustedSubtotal += itemTotal

            itemsSnapshot.add(
                OrderItemSnapshot(
                    foodId = trustedDish.id,
                    foodName = trustedDish.name,
                    quantity = cItem.quantity,
                    unitPrice = trustedDish.price,
                    subtotal = itemTotal,
                    isVegetarian = trustedDish.isVegetarian
                )
            )
        }

        val packagingFee = 5.0
        val trustedTotal = trustedSubtotal + packagingFee
        val orderId = "CB-${1000 + Random.nextInt(100, 9999)}"
        val token = nextTokenNumber++
        val dtFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val formattedDate = dtFormat.format(Date())

        val newOrder = Order(
            id = orderId,
            tokenNumber = token,
            userId = user.id,
            customerName = user.name,
            customerStudentId = user.studentId,
            customerEmail = user.email,
            customerPhone = user.phoneNumber,
            customerDepartment = user.department,
            items = itemsSnapshot,
            pickupDate = pickupDate,
            pickupSlot = pickupSlot,
            specialInstructions = specialInstructions.trim(),
            subtotal = trustedSubtotal,
            packagingFee = packagingFee,
            total = trustedTotal,
            paymentMethod = paymentMethod,
            paymentStatus = if (paymentMethod.contains("UPI")) "Paid (Demo UPI)" else "Pending at Counter",
            orderStatus = OrderStatus.PLACED,
            formattedDateTime = formattedDate,
            estimatedMinutes = (10..20).random(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        // Decrement stock
        cart.forEach { cItem ->
            val dIdx = currentDishes.indexOfFirst { it.id == cItem.foodItem.id }
            if (dIdx != -1) {
                val dish = currentDishes[dIdx]
                val updatedStock = (dish.stock - cItem.quantity).coerceAtLeast(0)
                currentDishes[dIdx] = dish.copy(
                    stock = updatedStock,
                    available = updatedStock > 0
                )
            }
        }
        _foodItems.value = currentDishes
        persistDishes()

        // Save order
        _orders.value = listOf(newOrder) + _orders.value
        persistOrders()

        // Clear cart ONLY after successful order placement
        clearCart()

        return Result.success(newOrder)
    }

    fun reorder(order: Order): Int {
        var addedCount = 0
        order.items.forEach { item ->
            val dish = _foodItems.value.find { it.id == item.foodId }
            if (dish != null && dish.available && dish.stock > 0) {
                addToCart(dish, item.quantity.coerceAtMost(dish.stock))
                addedCount++
            }
        }
        return addedCount
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus, cancellationReason: String? = null): Boolean {
        val currentOrders = _orders.value.toMutableList()
        val index = currentOrders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            val existing = currentOrders[index]
            currentOrders[index] = existing.copy(
                orderStatus = newStatus,
                cancellationReason = cancellationReason ?: existing.cancellationReason,
                updatedAt = System.currentTimeMillis()
            )
            _orders.value = currentOrders
            persistOrders()
            return true
        }
        return false
    }

    fun addFoodItem(foodItem: FoodItem) {
        val current = _foodItems.value.toMutableList()
        current.add(0, foodItem)
        _foodItems.value = current
        persistDishes()
    }

    fun updateFoodItem(foodItem: FoodItem) {
        val current = _foodItems.value.toMutableList()
        val index = current.indexOfFirst { it.id == foodItem.id }
        if (index != -1) {
            current[index] = foodItem
            _foodItems.value = current
            persistDishes()
        }
    }

    fun deleteFoodItem(foodId: String) {
        val current = _foodItems.value.toMutableList()
        current.removeAll { it.id == foodId }
        _foodItems.value = current
        persistDishes()
    }
}
