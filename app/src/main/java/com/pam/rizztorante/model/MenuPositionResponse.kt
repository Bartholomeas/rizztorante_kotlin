package com.pam.rizztorante.model

data class MenuPositionDetailsResponse(
        val id: String,
        val longDescription: String,
        val calories: Int,
        val allergens: List<String>,
        val nutritionalInfo: NutritionalInfo,
        val menuPosition: MenuPosition,
        val images: List<String>
)

data class NutritionalInfo(val protein: Int, val carbs: Int, val fat: Int, val fiber: Int)
