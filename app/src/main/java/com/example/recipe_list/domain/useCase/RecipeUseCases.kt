package com.example.recipe_list.domain.useCase

data class RecipeUseCases(
    val sortRecipeListUseCase: SortRecipeListUseCase,
    val getRecipeListUseCase: GetRecipeListUseCase,
    val searchRecipeUseCase: SearchRecipeUseCase,
    val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
)
