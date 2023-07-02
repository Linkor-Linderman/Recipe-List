package com.example.recipe_list.presentation.recipeDetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.recipe_list.R
import com.example.recipe_list.common.Screen
import com.example.recipe_list.presentation.composable.HtmlText
import com.example.recipe_list.presentation.composable.RatingBar
import com.example.recipe_list.presentation.composable.RecipeBriefItem
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun RecipeDetailScreen(
    navController: NavController,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val recipe = viewModel.recipeDetails.value
    if (viewModel.isLoading.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else if (viewModel.errorMessage.value != null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
            ) {
                Icon(
                    modifier = Modifier
                        .size(128.dp)
                        .align(Alignment.CenterHorizontally),
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.error_icon)
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = viewModel.errorMessage.value!!,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (recipe != null) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.h4,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (recipe.description != null && recipe.description.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.description),
                        style = MaterialTheme.typography.h5,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = recipe.description,
                        style = MaterialTheme.typography.body1,
                        maxLines = 10,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                FlowRow(
                    mainAxisAlignment = MainAxisAlignment.Center,
                    crossAxisAlignment = FlowCrossAxisAlignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.difficulty_level),
                        style = MaterialTheme.typography.h5,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    RatingBar(
                        value = recipe.difficulty.toInt(),
                        size = 24.dp,
                        spacing = 8.dp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (recipe.images.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.images),
                        style = MaterialTheme.typography.h5,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val state = rememberPagerState(
                        pageCount = recipe.images.size
                    )
                    val imageUrl =
                        remember { mutableStateOf("") }
                    HorizontalPager(
                        state = state,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    ) { page ->
                        imageUrl.value = recipe.images[page]

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                contentAlignment = Alignment.BottomCenter,
                                modifier = Modifier
                                    .clickable {
                                        val encodedUrl = URLEncoder.encode(
                                            recipe.images[page],
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        navController.navigate(Screen.ImageScreen.withArg(encodedUrl))
                                    }
                                    .widthIn(0.dp, 350.dp)
                            ) {
                                GlideImage(
                                    model = imageUrl.value,
                                    contentDescription = stringResource(id = R.string.image),
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = "${page + 1}/${recipe.images.size}",
                                    Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .padding(8.dp)
                                        .padding(8.dp),
                                    textAlign = TextAlign.End,
                                    color = Color.Red,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.cooking_instructions),
                        style = MaterialTheme.typography.h5,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HtmlText(
                        html = recipe.instructions,
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (recipe.similar.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.similar_recipes),
                            style = MaterialTheme.typography.h5,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        recipe.similar.forEach { recipeInfo ->
                            RecipeBriefItem(
                                recipe = recipeInfo,
                                onRecipeClick = {
                                    navController.navigate(Screen.RecipeDetailScreen.withArg(it))
                                })
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}