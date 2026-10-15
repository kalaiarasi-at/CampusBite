package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItem
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.repository.CampusBiteRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CampusBiteViewModel(
    private val repository: CampusBiteRepository = CampusBiteRepository.getInstance()
) : ViewModel() {

    // Screen navigation state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>(Screen.Home)

    // Data from repository
    val currentUser: StateFlow<User?> = repository.currentUser
    val foodItems: StateFlow<List<FoodItem>> = repository.foodItems
    val cartItems: StateFlow<List<CartItem>> = repository.cartItems
    val orders: StateFlow<List<Order>> = repository.orders

    // Filtered orders: Logged in student only sees their own orders; admin sees all
    val userOrders: StateFlow<List<Order>> = combine(orders, currentUser) { allOrders, user ->
        if (user == null) {
            emptyList()
        } else if (user.role == UserRole.ADMIN) {
            allOrders
        } else {
            allOrders.filter { it.userId == user.id || it.customerStudentId == user.studentId }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search, Filter & Sort state for Menu
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(FoodCategory.ALL)
    val selectedCategory: StateFlow<FoodCategory> = _selectedCategory.asStateFlow()

    private val _vegOnlyFilter = MutableStateFlow(false)
    val vegOnlyFilter: StateFlow<Boolean> = _vegOnlyFilter.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.POPULARITY)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    // Dialog & Detail states
    private val _selectedFoodItem = MutableStateFlow<FoodItem?>(null)
    val selectedFoodItem: StateFlow<FoodItem?> = _selectedFoodItem.asStateFlow()

    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> = _selectedOrder.asStateFlow()

    private val _lastPlacedOrder = MutableStateFlow<Order?>(null)
    val lastPlacedOrder: StateFlow<Order?> = _lastPlacedOrder.asStateFlow()

    // Feedback message flow
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    // Filtered and Sorted Food Items
    val filteredFoodItems: StateFlow<List<FoodItem>> = combine(
        foodItems,
        _searchQuery,
        _selectedCategory,
        _vegOnlyFilter,
        _sortOption
    ) { items, query, category, vegOnly, sort ->
        var list = items

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.description.lowercase().contains(q) ||
                it.category.displayName.lowercase().contains(q)
            }
        }

        if (category != FoodCategory.ALL) {
            list = list.filter { it.category == category }
        }

        if (vegOnly) {
            list = list.filter { it.isVegetarian }
        }

        when (sort) {
            SortOption.POPULARITY -> list.sortedByDescending { it.rating }
            SortOption.PRICE_LOW_TO_HIGH -> list.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.price }
            SortOption.FASTEST_PREP -> list.sortedBy { it.preparationTime }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart calculations
    val cartCount: StateFlow<Int> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartSubtotal: StateFlow<Double> = cartItems.combine(cartItems) { items, _ ->
        items.sumOf { it.subtotal }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.size > 1) {
            screenBackStack.removeAt(screenBackStack.lastIndex)
            _currentScreen.value = screenBackStack.last()
            return true
        }
        return false
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: FoodCategory) {
        _selectedCategory.value = category
    }

    fun setVegOnlyFilter(enabled: Boolean) {
        _vegOnlyFilter.value = enabled
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun showFoodDetails(foodItem: FoodItem) {
        _selectedFoodItem.value = foodItem
    }

    fun dismissFoodDetails() {
        _selectedFoodItem.value = null
    }

    fun selectOrderForTracking(order: Order) {
        _selectedOrder.value = order
        navigateTo(Screen.OrderTracking)
    }

    fun addToCart(foodItem: FoodItem, quantity: Int = 1) {
        if (!foodItem.available || foodItem.stock <= 0) {
            viewModelScope.launch {
                _snackbarMessage.emit("This item is currently unavailable.")
            }
            return
        }
        repository.addToCart(foodItem, quantity)
        viewModelScope.launch {
            _snackbarMessage.emit("Added ${foodItem.name} to cart successfully.")
        }
    }

    fun updateCartQuantity(foodId: String, newQuantity: Int) {
        repository.updateCartItemQuantity(foodId, newQuantity)
    }

    fun removeCartItem(foodId: String) {
        val item = cartItems.value.find { it.foodItem.id == foodId }
        repository.removeCartItem(foodId)
        viewModelScope.launch {
            _snackbarMessage.emit("${item?.foodItem?.name ?: "Item"} removed from cart.")
        }
    }

    fun clearCart() {
        repository.clearCart()
        viewModelScope.launch {
            _snackbarMessage.emit("Your cart is empty.")
        }
    }

    fun placeOrder(
        pickupDate: String,
        pickupSlot: String,
        specialInstructions: String,
        paymentMethod: String
    ): Boolean {
        val result = repository.placeOrder(
            pickupDate = pickupDate,
            pickupSlot = pickupSlot,
            specialInstructions = specialInstructions,
            paymentMethod = paymentMethod
        )

        return if (result.isSuccess) {
            val order = result.getOrThrow()
            _lastPlacedOrder.value = order
            _selectedOrder.value = order
            navigateTo(Screen.OrderConfirmation)
            viewModelScope.launch {
                _snackbarMessage.emit("Order Placed Successfully!")
            }
            true
        } else {
            val error = result.exceptionOrNull()?.message ?: "Unable to place your order. Please try again."
            viewModelScope.launch {
                _snackbarMessage.emit(error)
            }
            false
        }
    }

    fun quickReorder(order: Order) {
        val count = repository.reorder(order)
        viewModelScope.launch {
            if (count > 0) {
                _snackbarMessage.emit("Added $count items from Order #${order.id} to cart")
                navigateTo(Screen.Cart)
            } else {
                _snackbarMessage.emit("Items from this order are currently unavailable.")
            }
        }
    }

    fun registerStudent(
        name: String,
        studentId: String,
        email: String,
        department: String,
        yearOfStudy: String,
        phoneNumber: String
    ) {
        repository.registerStudent(name, studentId, email, department, yearOfStudy, phoneNumber)
        viewModelScope.launch {
            _snackbarMessage.emit("Welcome to CampusBite, $name!")
            navigateTo(Screen.Home)
        }
    }

    fun loginStudent(email: String, name: String, studentId: String) {
        repository.loginStudent(email, name, studentId)
        viewModelScope.launch {
            _snackbarMessage.emit("Logged in as $name")
            navigateTo(Screen.Home)
        }
    }

    fun loginAdmin() {
        repository.loginAdmin()
        viewModelScope.launch {
            _snackbarMessage.emit("Logged in as Canteen Manager (Admin)")
            navigateTo(Screen.AdminDashboard)
        }
    }

    fun logout() {
        repository.logout()
        viewModelScope.launch {
            _snackbarMessage.emit("Logged out successfully.")
            navigateTo(Screen.Home)
        }
    }

    fun updateProfile(name: String, studentId: String, department: String, yearOfStudy: String, phoneNumber: String) {
        repository.updateProfile(name, studentId, department, yearOfStudy, phoneNumber)
        viewModelScope.launch {
            _snackbarMessage.emit("Profile updated successfully.")
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus, reason: String? = null) {
        val success = repository.updateOrderStatus(orderId, newStatus, reason)
        if (success) {
            if (_selectedOrder.value?.id == orderId) {
                _selectedOrder.value = repository.orders.value.find { it.id == orderId }
            }
            viewModelScope.launch {
                _snackbarMessage.emit("Order #$orderId marked as ${newStatus.displayName}")
            }
        }
    }

    fun addFoodItem(foodItem: FoodItem) {
        repository.addFoodItem(foodItem)
        viewModelScope.launch {
            _snackbarMessage.emit("${foodItem.name} added to canteen menu.")
        }
    }

    fun updateFoodItem(foodItem: FoodItem) {
        repository.updateFoodItem(foodItem)
        viewModelScope.launch {
            _snackbarMessage.emit("${foodItem.name} updated.")
        }
    }

    fun deleteFoodItem(foodId: String) {
        val dish = repository.foodItems.value.find { it.id == foodId }
        repository.deleteFoodItem(foodId)
        viewModelScope.launch {
            _snackbarMessage.emit("${dish?.name ?: "Item"} removed from menu.")
        }
    }

    fun resetDemoData() {
        repository.resetDemoData()
        viewModelScope.launch {
            _snackbarMessage.emit("Demo menu and orders reset to factory default.")
        }
    }
}
