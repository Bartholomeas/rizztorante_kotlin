package com.pam.rizztorante.model

data class CartResponse(
    val id: String,
    val total: Int,
    val items: List<CartItemResponse>
)

data class CartItemResponse(
    val id: String,
    val quantity: Int,
    val amount: Int,
    val menuPosition: MenuPositionResponse,
    val ingredients: List<Any> 
)

data class MenuPositionResponse(
    val id: String,
    val name: String,
    val price: Int,
    val description: String,
    val isVegetarian: Boolean,
    val isVegan: Boolean,
    val isGlutenFree: Boolean,
    val coreImage: CoreImageResponse
)

data class CoreImageResponse(
    val id: String,
    val url: String,
    val alt: String,
    val caption: String?
)