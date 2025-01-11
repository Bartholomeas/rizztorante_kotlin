package com.pam.rizztorante.model

data class MenuResponse(
        val id: String,
        val name: String,
        val slug: String,
        val description: String,
)

data class MenuItemResponse(
        val id: String,
        val name: String,
        val description: String,
        val price: Double
)
