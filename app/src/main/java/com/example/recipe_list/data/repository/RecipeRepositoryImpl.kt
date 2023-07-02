package com.example.recipe_list.data.repository

import com.example.recipe_list.data.remote.RecipeApi
import com.example.recipe_list.domain.model.RecipeDetails
import com.example.recipe_list.domain.model.RecipeInfo
import com.example.recipe_list.domain.repository.RecipeRepository

class RecipeRepositoryImpl(
    private val api: RecipeApi
) : RecipeRepository {
    override suspend fun getRecipes(): List<RecipeInfo> {
        return api.getRecipes().recipes.map { it.toRecipeInfo() }
    }

    override suspend fun getRecipeDetails(uuid: String): RecipeDetails {
        return api.getRecipeDetail(uuid).recipe.toRecipeDetails()
    }
}