package com.john.kmpapplication.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun ProductImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(200.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        imageUrl?.let {
            KamelImage(
                resource = {
                    asyncPainterResource(data = imageUrl)
                },
                contentDescription = null,
                modifier = Modifier.matchParentSize().aspectRatio(1f, true),
                contentScale = ContentScale.Fit,
                onLoading = {
                    CircularProgressIndicator()
                },
                onFailure = { exception ->
                    Text(text = "Failed to load image, $exception")
                },
                animationSpec = tween(durationMillis = 300)
            )
        }

    }
}