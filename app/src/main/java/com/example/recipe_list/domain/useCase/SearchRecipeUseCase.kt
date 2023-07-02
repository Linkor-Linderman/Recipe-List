package com.example.recipe_list.domain.useCase

import com.example.recipe_list.domain.model.RecipeInfo

class SearchRecipeUseCase {
    suspend operator fun invoke(
        searchQuery: String,
        recipes: List<RecipeInfo>
    ): List<RecipeInfo> {
        return recipes.filter { recipe ->
            recipe.name.contains(searchQuery, ignoreCase = true) ||
                    if (recipe.description.isNullOrEmpty()) false else recipe.description.contains(
                        searchQuery,
                        ignoreCase = true
                    ) ||
                            recipe.instructions.contains(searchQuery, ignoreCase = true)
        }
    }
}