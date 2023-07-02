package com.example.recipe_list.common

sealed class Screen(val route: String) {
    object RecipeListScreen : Screen("recipe_list_screen")
    object RecipeDetailScreen : Screen("recipe_detail_screen")
    object ImageScreen : Screen("image_screen")

    fun withArg(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
