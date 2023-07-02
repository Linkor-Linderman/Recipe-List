package com.example.recipe_list.data.remote

import com.example.recipe_list.data.remote.dto.RecipeDetailModelDTO
import com.example.recipe_list.data.remote.dto.RecipeListModelDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {
    @GET("/recipes")
    suspend fun getRecipes(): RecipeListModelDTO

    @GET("/recipes/{uuid}")
    suspend fun getRecipeDetail(
        @Path("uuid") uuid: String
    ): RecipeDetailModelDTO
}