package com.example.recipe_list.data.remote.dto

import com.example.recipe_list.domain.model.RecipeDetails

data class RecipeDetailsDTO(
    val uuid: String,
    val name: String,
    val images: List<String>,
    val lastUpdated: Int,
    val description: String,
    val instructions: String,
    val difficulty: String,
    val similar: List<RecipeBriefInfoDTO>
) {
    fun toRecipeDetails(): RecipeDetails =
        RecipeDetails(
            uuid,
            name,
            images,
            lastUpdated,
            description,
            instructions,
            difficulty,
            similar.map { it.toRecipeBriefInfo() })
}
