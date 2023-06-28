package com.example.recipe_list.domain.model

data class RecipeDetails(
    val uuid: String,
    val name: String,
    val images: List<String>,
    val lastUpdated: Int,
    val description: String,
    val instructions: String,
    val difficulty: String,
    val similar: List<RecipeBriefInfo>
)
