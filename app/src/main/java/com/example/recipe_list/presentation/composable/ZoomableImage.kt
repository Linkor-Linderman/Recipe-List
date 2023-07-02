package com.example.recipe_list.presentation.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ZoomableImageVariant(
    model: String,
    contentDescription: String? = null,
) {
    val angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var targetZoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    val animatedZoom by animateFloatAsState(targetZoom, animationSpec = spring())
    val animatedOffsetX by animateFloatAsState(offsetX, animationSpec = spring())
    val animatedOffsetY by animateFloatAsState(offsetY, animationSpec = spring())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
                onDoubleClick = {
                    targetZoom = if (targetZoom > 1f) 1f
                    else 3f
                }
            )
    ) {
        GlideImage(
            model,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.roundToInt(), animatedOffsetY.roundToInt()) }
                .graphicsLayer(
                    scaleX = animatedZoom,
                    scaleY = animatedZoom,
                    rotationZ = angle
                )
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, pan, gestureZoom, _ ->
                            zoom = (zoom * gestureZoom).coerceIn(1F..4F)
                            targetZoom = zoom
                            if (zoom > 1) {
                                val x = (pan.x * zoom)
                                val y = (pan.y * zoom)
                                val angleRad = angle * PI / 180.0

                                offsetX =
                                    (offsetX + (x * cos(angleRad) - y * sin(angleRad)).toFloat()).coerceIn(
                                        -(screenWidth * zoom)..(screenWidth * zoom)
                                    )
                                offsetY =
                                    (offsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                        -(screenHeight * zoom)..(screenHeight * zoom)
                                    )
                            } else {
                                offsetX = 0F
                                offsetY = 0F
                            }
                        }
                    )
                }
                .fillMaxSize()
        )
    }
}