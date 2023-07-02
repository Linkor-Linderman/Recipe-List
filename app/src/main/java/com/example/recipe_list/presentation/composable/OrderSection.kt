package com.example.recipe_list.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.recipe_list.R
import com.example.recipe_list.domain.utill.OrderType
import com.example.recipe_list.domain.utill.RecipeOrder

@Composable
fun OrderSection(
    modifier: Modifier = Modifier,
    recipeOrder: RecipeOrder = RecipeOrder.Name(OrderType.Ascending),
    onOrderChange: (RecipeOrder) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = stringResource(id = R.string.name),
                selected = recipeOrder is RecipeOrder.Name,
                onSelect = { onOrderChange(RecipeOrder.Name(recipeOrder.orderType)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = stringResource(id = R.string.date),
                selected = recipeOrder is RecipeOrder.Date,
                onSelect = { onOrderChange(RecipeOrder.Date(recipeOrder.orderType)) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultRadioButton(
                text = stringResource(id = R.string.ascending),
                selected = recipeOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(recipeOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = stringResource(id = R.string.descending),
                selected = recipeOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(recipeOrder.copy(OrderType.Descending)) }
            )
        }
    }
}