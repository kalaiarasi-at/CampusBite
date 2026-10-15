package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OrangeContainer
import com.example.ui.theme.OrangeDark
import com.example.ui.theme.OrangePrimary
import com.example.ui.theme.RedDanger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onStudentLoginSuccess: (email: String, name: String, studentId: String) -> Unit,
    onStudentRegisterSuccess: (
        name: String,
        studentId: String,
        email: String,
        department: String,
        yearOfStudy: String,
        phoneNumber: String
    ) -> Unit,
    onAdminLoginSuccess: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Student Login, 1: Student Register, 2: Admin Login

    // Login state
    var loginEmail by remember { mutableStateOf("aarav.sharma@campusbite.edu") }
    var loginPassword by remember { mutableStateOf("campus123") }
    var loginPasswordVisible by remember { mutableStateOf(false) }

    // Register state
    var regName by remember { mutableStateOf("") }
    var regStudentId by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regDepartment by remember { mutableStateOf("Computer Science & Engineering") }
    var regYear by remember { mutableStateOf("3rd Year") }
    var regPhone by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regConfirmPassword by remember { mutableStateOf("") }
    var regPasswordVisible by remember { mutableStateOf(false) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }

    // Admin state
    var adminEmail by remember { mutableStateOf("admin@campusbite.com") }
    var adminPassword by remember { mutableStateOf("admin123") }
    var adminPasswordVisible by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    val yearsList = listOf("1st Year (Freshman)", "2nd Year (Sophomore)", "3rd Year (Junior)", "4th Year (Senior)", "Postgraduate / Faculty")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDF9))
            .verticalScroll(scrollState)
            .padding(24.dp)
            .testTag("auth_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Branding
        Surface(
            color = OrangeContainer,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.size(54.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = OrangeDark,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "CampusBite College Canteen",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Sign in or register your student profile to pre-order food",
            fontSize = 13.sp,
            color = Color(0xFF64748B)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = OrangePrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = OrangePrimary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0; errorMessage = null },
                text = { Text("Student Login", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier.testTag("tab_student_login")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1; errorMessage = null },
                text = { Text("Register", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier.testTag("tab_student_register")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2; errorMessage = null },
                text = { Text("Staff / Admin", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                modifier = Modifier.testTag("tab_admin_login")
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                if (errorMessage != null) {
                    Surface(
                        color = Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = RedDanger,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                if (selectedTab == 0) {
                    // Student Login Form
                    Text("Student Login", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = loginEmail,
                        onValueChange = { loginEmail = it; errorMessage = null },
                        label = { Text("College Email Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("input_student_email"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it; errorMessage = null },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { loginPasswordVisible = !loginPasswordVisible }) {
                                Icon(
                                    if (loginPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("input_student_password"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (loginEmail.isBlank() || !loginEmail.contains("@") || !loginEmail.contains(".")) {
                                errorMessage = "Please enter a valid college email address."
                            } else if (loginPassword.length < 4) {
                                errorMessage = "Password must be at least 4 characters."
                            } else {
                                onStudentLoginSuccess(loginEmail, "Aarav Sharma", "STU-2024-8842")
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_submit_student_login")
                    ) {
                        Text("Sign In to Canteen Portal", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            loginEmail = "aarav.sharma@campusbite.edu"
                            loginPassword = "campus123"
                            onStudentLoginSuccess(loginEmail, "Aarav Sharma", "STU-2024-8842")
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_demo_student_quickfill")
                    ) {
                        Text("Fill Demo Student (Aarav Sharma - STU-2024-8842)", fontSize = 12.sp)
                    }

                } else if (selectedTab == 1) {
                    // Registration Form with all required fields
                    Text("Register New Student", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = regName,
                        onValueChange = { regName = it; errorMessage = null },
                        label = { Text("Full Name *") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("input_reg_fullname"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regStudentId,
                        onValueChange = { regStudentId = it; errorMessage = null },
                        label = { Text("Student ID / Register Number *") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("input_reg_studentid"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regEmail,
                        onValueChange = { regEmail = it; errorMessage = null },
                        label = { Text("College Email Address *") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("input_reg_email"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regDepartment,
                        onValueChange = { regDepartment = it; errorMessage = null },
                        label = { Text("Department / Branch *") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("input_reg_department"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Year of study dropdown
                    ExposedDropdownMenuBox(
                        expanded = yearDropdownExpanded,
                        onExpandedChange = { yearDropdownExpanded = !yearDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = regYear,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Year of Study *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearDropdownExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = yearDropdownExpanded,
                            onDismissRequest = { yearDropdownExpanded = false }
                        ) {
                            yearsList.forEach { y ->
                                DropdownMenuItem(
                                    text = { Text(y) },
                                    onClick = {
                                        regYear = y
                                        yearDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regPhone,
                        onValueChange = { regPhone = it; errorMessage = null },
                        label = { Text("Phone Number (+91 XXXXX XXXXX) *") },
                        leadingIcon = { Icon(Icons.Default.Call, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("input_reg_phone"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regPassword,
                        onValueChange = { regPassword = it; errorMessage = null },
                        label = { Text("Create Password *") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = if (regPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("input_reg_password"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regConfirmPassword,
                        onValueChange = { regConfirmPassword = it; errorMessage = null },
                        label = { Text("Confirm Password *") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = if (regPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("input_reg_confirm_password"),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (regName.isBlank()) {
                                errorMessage = "Please enter your full name."
                            } else if (regStudentId.isBlank()) {
                                errorMessage = "Please enter your student ID / register number."
                            } else if (regEmail.isBlank() || !regEmail.contains("@") || !regEmail.contains(".")) {
                                errorMessage = "Please enter a valid college email address."
                            } else if (regDepartment.isBlank()) {
                                errorMessage = "Please enter your department."
                            } else if (regPhone.length < 10) {
                                errorMessage = "Please enter a valid 10-digit mobile number."
                            } else if (regPassword.length < 6) {
                                errorMessage = "Password must be at least 6 characters long."
                            } else if (regPassword != regConfirmPassword) {
                                errorMessage = "Passwords do not match."
                            } else {
                                onStudentRegisterSuccess(
                                    regName,
                                    regStudentId,
                                    regEmail,
                                    regDepartment,
                                    regYear,
                                    regPhone
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_submit_student_register")
                    ) {
                        Text("Create Canteen Account", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                } else {
                    // Admin Login Form
                    Text("Canteen Staff / Manager Portal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Authorized kitchen & manager credentials required.", fontSize = 11.sp, color = Color(0xFF64748B))

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = adminEmail,
                        onValueChange = { adminEmail = it; errorMessage = null },
                        label = { Text("Staff Email") },
                        leadingIcon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("input_admin_email"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = adminPassword,
                        onValueChange = { adminPassword = it; errorMessage = null },
                        label = { Text("Manager Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = if (adminPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("input_admin_password"),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            if (adminEmail.trim().lowercase() == "admin@campusbite.com" && adminPassword == "admin123") {
                                onAdminLoginSuccess()
                            } else {
                                errorMessage = "Invalid Admin credentials. Use admin@campusbite.com / admin123"
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_submit_admin_login")
                    ) {
                        Text("Access Admin Console", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = {
                            adminEmail = "admin@campusbite.com"
                            adminPassword = "admin123"
                            onAdminLoginSuccess()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("btn_demo_admin_quickfill")
                    ) {
                        Text("Quick Staff Login (admin@campusbite.com)", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
