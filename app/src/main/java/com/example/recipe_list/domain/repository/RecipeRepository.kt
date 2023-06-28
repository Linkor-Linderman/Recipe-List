package com.example.recipe_list.domain.repository

import com.example.recipe_list.domain.model.RecipeDetails
import com.example.recipe_list.domain.model.RecipeInfo

interface RecipeRepository {
    suspend fun getRecipes(): List<RecipeInfo>

    suspend fun getRecipeDetails(uuid: String): RecipeDetails
}