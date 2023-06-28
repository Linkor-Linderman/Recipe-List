package com.example.recipe_list.data.remote.dto

import com.example.recipe_list.domain.model.RecipeInfo

data class RecipeInfoDTO(
    val uuid: String,
    val name: String,
    val images: List<String>,
    val lastUpdated: Int,
    val description: String,
    val instructions: String,
    val difficulty: String
) {
    fun toRecipeInfo(): RecipeInfo =
        RecipeInfo(uuid, name, images, lastUpdated, description, instructions, difficulty)
}
