package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CartItem
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.ui.SortOption
import com.example.ui.components.FoodItemCard
import com.example.ui.components.VegBadge
import com.example.ui.theme.OrangePrimary

@Composable
fun MenuScreen(
    foodItems: List<FoodItem>,
    cartItems: List<CartItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: FoodCategory,
    onSelectCategory: (FoodCategory) -> Unit,
    vegOnlyFilter: Boolean,
    onToggleVegOnly: (Boolean) -> Unit,
    sortOption: SortOption,
    onSelectSortOption: (SortOption) -> Unit,
    onFoodCardClick: (FoodItem) -> Unit,
    onAddToCart: (FoodItem) -> Unit,
    onIncrementCart: (FoodItem) -> Unit,
    onDecrementCart: (FoodItem) -> Unit
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .testTag("menu_screen")
    ) {
        // Sticky Header: Search & Quick Filters
        Surface(
            color = Color.White,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search canteen menu (e.g. Idli, Thali, Chai)...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF64748B)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("menu_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangePrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Secondary row: Veg toggle chip & Sort dropdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Veg Only Filter
                    FilterChip(
                        selected = vegOnlyFilter,
                        onClick = { onToggleVegOnly(!vegOnlyFilter) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                VegBadge(isVegetarian = true)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pure Veg Only", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFDCFCE7),
                            selectedLabelColor = Color(0xFF14532D)
                        ),
                        modifier = Modifier.testTag("filter_veg_only")
                    )

                    // Sort dropdown button
                    Box {
                        Surface(
                            onClick = { sortMenuExpanded = true },
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.testTag("btn_sort_options")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Sort,
                                    contentDescription = null,
                                    tint = Color(0xFF475569),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = sortOption.label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF334155)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.label, fontSize = 13.sp) },
                                    onClick = {
                                        onSelectSortOption(option)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Chips Horizontal Scroll
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(FoodCategory.values()) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectCategory(category) },
                            label = { Text(category.displayName, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = OrangePrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("category_chip_${category.name}")
                        )
                    }
                }
            }
        }

        // Result count header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${foodItems.size} items available",
                fontSize = 12.sp,
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Medium
            )
            if (searchQuery.isNotEmpty() || selectedCategory != FoodCategory.ALL || vegOnlyFilter) {
                Text(
                    text = "Clear All Filters",
                    fontSize = 12.sp,
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onSearchQueryChange("")
                        onSelectCategory(FoodCategory.ALL)
                        onToggleVegOnly(false)
                    }
                )
            }
        }

        // Dishes List
        if (foodItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = null,
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No matching dishes found",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try adjusting your search query or category filters.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onSearchQueryChange("")
                            onSelectCategory(FoodCategory.ALL)
                            onToggleVegOnly(false)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reset Filters")
                    }
                }
            }
        } else {
            androidx.compose.foundation.layout.BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isWide = maxWidth > 600.dp
                val columns = if (isWide) 2 else 1
                val chunkedRows = foodItems.chunked(columns)

                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(chunkedRows) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
    }
}
