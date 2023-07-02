package com.example.recipe_list.domain.useCase

import com.example.recipe_list.domain.model.RecipeInfo
import com.example.recipe_list.domain.utill.OrderType
import com.example.recipe_list.domain.utill.RecipeOrder

class SortRecipeListUseCase {
    suspend operator fun invoke(
        recipes: List<RecipeInfo>,
        recipeOrder: RecipeOrder = RecipeOrder.Name(OrderType.Ascending)
    ): List<RecipeInfo> {
        return when (recipeOrder.orderType) {
            is OrderType.Ascending -> {
                when (recipeOrder) {
                    is RecipeOrder.Name -> recipes.sortedBy { it.name.lowercase() }
                    is RecipeOrder.Date -> recipes.sortedBy { it.lastUpdated }
                }
            }
            is OrderType.Descending -> {
                when (recipeOrder) {
                    is RecipeOrder.Name -> recipes.sortedByDescending { it.name.lowercase() }
                    is RecipeOrder.Date -> recipes.sortedByDescending { it.lastUpdated }
                }
            }
        }
    }
}