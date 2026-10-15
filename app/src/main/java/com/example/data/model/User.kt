package com.example.data.model

enum class UserRole {
    STUDENT,
    ADMIN
}

data class User(
    val id: String,
    val name: String,
    val studentId: String = "",
    val email: String,
    val department: String = "Computer Science & Engineering",
    val yearOfStudy: String = "3rd Year",
    val phoneNumber: String = "+91 98765 43210",
    val role: UserRole = UserRole.STUDENT,
    val createdAt: Long = System.currentTimeMillis()
)
