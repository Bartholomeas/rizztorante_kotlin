package com.pam.rizztorante.model

data class CartItem(
    val id: String,
    val name: String,
    val price: Int,
    var quantity: Int,
    val coreImageUrl: CoreImageResponse
)
