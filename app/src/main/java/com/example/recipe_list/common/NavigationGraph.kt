package com.example.recipe_list.common

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.recipe_list.presentation.imageScreen.ImageScreen
import com.example.recipe_list.presentation.recipeDetails.RecipeDetailScreen
import com.example.recipe_list.presentation.recipeList.RecipeListScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String = Screen.RecipeListScreen.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.RecipeListScreen.route
        ) {
            RecipeListScreen(navController)
        }
        composable(
            route = "${Screen.RecipeDetailScreen.route}/{${Constants.ID}}",
            arguments = listOf(
                navArgument(Constants.ID) { type = NavType.StringType },
            )
        ) {
            RecipeDetailScreen(navController)
        }
        composable(
            route = "${Screen.ImageScreen.route}/{${Constants.URL}}",
            arguments = listOf(
                navArgument(Constants.URL) { type = NavType.StringType },
            )
        ) {
            ImageScreen(navController)
        }
    }
}