package com.example.recipe_list.presentation.composable

import android.text.TextUtils
import android.widget.TextView
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    html: String,
    style: TextStyle = MaterialTheme.typography.body1,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                this.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
                this.maxLines = maxLines
                this.ellipsize = when (overflow) {
                    TextOverflow.Clip -> null
                    TextOverflow.Ellipsis -> TextUtils.TruncateAt.END
                    TextOverflow.Visible -> TextUtils.TruncateAt.MARQUEE
                    else -> {
                        null
                    }
                }
                this.textSize = style.fontSize.value
            }
        },
        modifier = modifier
    )
}