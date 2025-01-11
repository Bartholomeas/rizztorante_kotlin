package com.pam.rizztorante.model

data class AddToCartRequest(
    val menuPositionId: String,
    val quantity: Int = 1
) 