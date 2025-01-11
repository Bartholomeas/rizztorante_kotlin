package com.pam.rizztorante.model

data class MenuCategoryResponse(
        val id: String,
        val name: String,
        val description: String?,
        val positions: List<MenuPosition>
)

data class MenuPosition(
        val id: String,
        val name: String,
        val price: Int,
        val description: String,
        val isVegetarian: Boolean,
        val isVegan: Boolean,
        val isGlutenFree: Boolean,
        val coreImage: CoreImage?
)

data class CoreImage(val id: String, val url: String, val alt: String, val caption: String)
