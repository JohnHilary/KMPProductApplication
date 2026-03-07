package com.john.kmpapplication.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .size(200.dp),
        contentAlignment = Alignment.Center
    ) {
        imageUrl?.let {
            KamelImage(
                resource = {
                    asyncPainterResource(data = imageUrl)
                },
                contentDescription = null,
                modifier = Modifier.aspectRatio(1f, true).padding(12.dp),
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