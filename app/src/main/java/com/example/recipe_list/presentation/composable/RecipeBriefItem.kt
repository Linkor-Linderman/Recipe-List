package com.example.recipe_list.presentation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipe_list.domain.model.RecipeBriefInfo

@Composable
fun RecipeBriefItem(
    recipe: RecipeBriefInfo,
    onRecipeClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecipeClick(recipe.uuid) },
        elevation = 4.dp
    ) {
        Text(
            text = recipe.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h6
        )
    }
}