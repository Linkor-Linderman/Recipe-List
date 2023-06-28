package com.example.recipe_list.data.remote

import com.example.recipe_list.data.remote.dto.RecipeDetailsDTO
import com.example.recipe_list.data.remote.dto.RecipeInfoDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {
    @GET("/recipes")
    suspend fun getRecipes(): List<RecipeInfoDTO>

    @GET("/recipes/{uuid}")
    suspend fun getRecipeDetail(
        @Path("uuid") uuid: String
    ): RecipeDetailsDTO
}