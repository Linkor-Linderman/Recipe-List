package com.example.recipe_list.presentation.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.recipe_list.R

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    value: Int,
    size: Dp = 16.dp,
    spacing: Dp = 2.dp,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        (0 until 5).forEach { i ->
            if (i < value) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = stringResource(R.string.star),
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(size)
                )
            } else {
                Icon(
                    Icons.Default.Star,
                    contentDescription = stringResource(R.string.empty_star),
                    modifier = Modifier.size(size)
                )
            }
            if (i != 4) Spacer(modifier = Modifier.width(spacing))
        }
    }
}