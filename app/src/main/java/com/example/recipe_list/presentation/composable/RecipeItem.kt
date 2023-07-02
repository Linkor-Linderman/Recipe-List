package com.example.recipe_list.presentation.composable

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.recipe_list.domain.model.RecipeInfo
import com.example.recipe_list.ui.theme.CardBackgroundBlack
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RecipeItem(
    recipe: RecipeInfo,
    onRecipeClick: (RecipeInfo) -> Unit
) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    val updatedDate = sdf.format(Date(recipe.lastUpdated.toLong()))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRecipeClick(recipe) }
            .height(114.dp),
        backgroundColor = CardBackgroundBlack,
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            GlideImage(
                modifier = Modifier
                    .background(CardBackgroundBlack)
                    .fillMaxSize()
                    .weight(1f)
                    .drawWithCache {
                        val gradient = Brush.horizontalGradient(
                            colors = listOf(Color.Transparent, CardBackgroundBlack),
                            startX = size.width - (size.width * 0.14).toInt(),
                            endX = size.width
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(gradient)
                        }
                    },
                model = recipe.images[0],
                contentScale = ContentScale.Crop,
                contentDescription = "avatar_cover"
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .weight(if (!isPortrait) 2f else 1f),
            ) {
                Text(
                    text = recipe.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = recipe.description ?: "",
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight(400),
                    color = Color.LightGray,
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = updatedDate,
                        fontSize = 11.sp,
                        color = Color.LightGray.copy(alpha = 0.9f),
                    )
                }
            }
        }
    }
}