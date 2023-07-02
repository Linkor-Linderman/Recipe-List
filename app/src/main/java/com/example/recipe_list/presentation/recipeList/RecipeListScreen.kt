package com.example.recipe_list.presentation.recipeList

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipe_list.R
import com.example.recipe_list.common.Screen
import com.example.recipe_list.presentation.composable.OrderSection
import com.example.recipe_list.presentation.composable.RecipeItem


@Composable
fun RecipeListScreen(
    navController: NavController,
    viewModel: RecipeListScreenViewModel = hiltViewModel()
) {
    val searchText by viewModel.searchText.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier.weight(9f),
                placeholder = { Text(text = stringResource(id = R.string.search_recipe)) }
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.toggleOrderSection()
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.sort_icon),
                    modifier = Modifier.size(32.dp),
                    contentDescription = stringResource(id = R.string.sort)
                )
            }
        }
        AnimatedVisibility(
            visible = viewModel.isVisible.value,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            OrderSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                recipeOrder = viewModel.recipeOrder.value,
                onOrderChange = {
                    viewModel.onOrderChange(it)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isSearching || viewModel.isLoading.value) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else if (viewModel.errorMessage.value != null) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Icon(
                        modifier = Modifier
                            .size(128.dp)
                            .align(CenterHorizontally),
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(R.string.error_icon)
                    )
                    Text(
                        modifier = Modifier.align(CenterHorizontally),
                        text = viewModel.errorMessage.value!!,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(recipes) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onRecipeClick = {
                            navController.navigate(Screen.RecipeDetailScreen.withArg(it.uuid))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}