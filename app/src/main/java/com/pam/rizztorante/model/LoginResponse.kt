package com.pam.rizztorante.model

enum class UserRole {
  ADMIN,
  USER,
  GUEST
}

data class LoginResponse(
        val id: String,
        val createdAt: String,
        val updatedAt: String,
        val email: String,
        val role: UserRole
)
