package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.ui.components.VegBadge
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.RedDanger
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminFoodManageScreen(
    foodItems: List<FoodItem>,
    onAddFoodItem: (FoodItem) -> Unit,
    onUpdateFoodItem: (FoodItem) -> Unit,
    onDeleteFoodItem: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<FoodItem?>(null) }
    var deletingItem by remember { mutableStateOf<FoodItem?>(null) }

    val filteredList = if (searchQuery.isBlank()) {
        foodItems
    } else {
        val q = searchQuery.trim().lowercase()
        foodItems.filter {
            it.name.lowercase().contains(q) ||
            it.category.displayName.lowercase().contains(q)
        }
    }

    // Add or Edit Dialog
    if (showAddDialog || editingItem != null) {
        val isEdit = editingItem != null
        val target = editingItem

        var name by remember(target) { mutableStateOf(target?.name ?: "") }
        var description by remember(target) { mutableStateOf(target?.description ?: "") }
        var priceStr by remember(target) { mutableStateOf(target?.price?.toInt()?.toString() ?: "40") }
        var stockStr by remember(target) { mutableStateOf(target?.stock?.toString() ?: "30") }
        var prepTimeStr by remember(target) { mutableStateOf(target?.preparationTime?.toString() ?: "10") }
        var isVeg by remember(target) { mutableStateOf(target?.isVegetarian ?: true) }
        var isSpecial by remember(target) { mutableStateOf(target?.isTodaySpecial ?: false) }
        var category by remember(target) { mutableStateOf(target?.category ?: FoodCategory.SNACKS) }
        var categoryExpanded by remember { mutableStateOf(false) }

        var validationError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                editingItem = null
            },
            title = { Text(if (isEdit) "Edit Menu Dish" else "Add New Canteen Dish") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (validationError != null) {
                        Text(validationError ?: "", color = RedDanger, fontSize = 12.sp)
                    }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; validationError = null },
                        label = { Text("Dish Name") },
                        modifier = Modifier.fillMaxWidth().testTag("admin_input_dish_name")
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth().testTag("admin_input_dish_desc"),
                        maxLines = 3
                    )

                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = category.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            FoodCategory.values().filter { it != FoodCategory.ALL }.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.displayName) },
                                    onClick = {
                                        category = cat
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { priceStr = it },
                            label = { Text("Price (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("admin_input_dish_price")
                        )
                        OutlinedTextField(
                            value = stockStr,
                            onValueChange = { stockStr = it },
                            label = { Text("Stock") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("admin_input_dish_stock")
                        )
                    }

                    OutlinedTextField(
                        value = prepTimeStr,
                        onValueChange = { prepTimeStr = it },
                        label = { Text("Prep Time (minutes)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("admin_input_dish_preptime")
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isVeg,
                            onCheckedChange = { isVeg = it },
                            colors = CheckboxDefaults.colors(checkedColor = GreenSuccess)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Vegetarian Dish", fontSize = 13.sp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isSpecial,
                            onCheckedChange = { isSpecial = it },
                            colors = CheckboxDefaults.colors(checkedColor = OrangePrimary)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mark as Today's Special Banner", fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedPrice = priceStr.toDoubleOrNull()
                        val parsedStock = stockStr.toIntOrNull()
                        val parsedPrep = prepTimeStr.toIntOrNull()

                        if (name.isBlank()) {
                            validationError = "Dish name cannot be empty."
                        } else if (parsedPrice == null || parsedPrice <= 0) {
                            validationError = "Please enter a valid price."
                        } else if (parsedStock == null || parsedStock < 0) {
                            validationError = "Please enter a valid stock count."
                        } else {
                            val newItem = FoodItem(
                                id = target?.id ?: "dish_${UUID.randomUUID().toString().take(8)}",
                                name = name.trim(),
                                description = description.trim(),
                                category = category,
                                price = parsedPrice,
                                stock = parsedStock,
                                available = parsedStock > 0,
                                preparationTime = parsedPrep ?: 10,
                                isVegetarian = isVeg,
                                isTodaySpecial = isSpecial,
                                rating = target?.rating ?: 4.8f
                            )

                            if (isEdit) {
                                onUpdateFoodItem(newItem)
                            } else {
                                onAddFoodItem(newItem)
                            }

                            showAddDialog = false
                            editingItem = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    modifier = Modifier.testTag("admin_btn_save_dish")
                ) {
                    Text(if (isEdit) "Update Item" else "Add Item")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddDialog = false
                    editingItem = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    if (deletingItem != null) {
        val dish = deletingItem!!
        AlertDialog(
            onDismissRequest = { deletingItem = null },
            title = { Text("Delete Dish?") },
            text = { Text("Are you sure you want to remove \"${dish.name}\" from the canteen menu?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteFoodItem(dish.id)
                        deletingItem = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedDanger)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingItem = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFDF9))
                .testTag("admin_food_manage_screen")
        ) {
            // Header Search
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Canteen Menu Editor",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${foodItems.size} dishes configured",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        Button(
                            onClick = { showAddDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            modifier = Modifier.testTag("btn_admin_add_dish")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New Dish", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search dishes to edit or toggle stock...", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_manage_search_input"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OrangePrimary,
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )
                }
            }

            // Dishes list
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredList, key = { it.id }) { dish ->
                    AdminDishRow(
                        dish = dish,
                        onToggleAvailable = { newAvailable ->
                            onUpdateFoodItem(dish.copy(available = newAvailable))
                        },
                        onEdit = { editingItem = dish },
                        onDelete = { deletingItem = dish }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminDishRow(
    dish: FoodItem,
    onToggleAvailable: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_dish_row_${dish.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VegBadge(isVegetarian = dish.isVegetarian)

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = dish.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (dish.isTodaySpecial) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFFEF08A),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "Special",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF854D0E),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${dish.category.displayName} • ₹${dish.price.toInt()} • Stock: ${dish.stock}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            // Availability Toggle Switch
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Switch(
                    checked = dish.available && dish.stock > 0,
                    onCheckedChange = onToggleAvailable,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GreenSuccess
                    ),
                    modifier = Modifier.testTag("switch_available_${dish.id}")
                )
                Text(
                    text = if (dish.available && dish.stock > 0) "In Stock" else "Sold Out",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (dish.available && dish.stock > 0) GreenSuccess else RedDanger
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Edit Button
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(32.dp).testTag("btn_edit_dish_${dish.id}")
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF475569), modifier = Modifier.size(18.dp))
            }

            // Delete Button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp).testTag("btn_delete_dish_${dish.id}")
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RedDanger, modifier = Modifier.size(18.dp))
            }
        }
    }
}
