package com.john.kmpapplication.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    defaultIcon: ImageVector? = null,
    size: Dp = 200.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    shadowElevation: Dp = 0.dp,
    backgroundColor: Color = Color.Transparent,
) {
    Surface(
        modifier = modifier
            .size(size)
            .padding(4.dp),
        shape = shape,
        shadowElevation = shadowElevation,
        color = backgroundColor
    ) {
        imageUrl?.let {
            KamelImage(
                resource = {
                    asyncPainterResource(data = imageUrl)
                },
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape),
                contentScale = if (shape == CircleShape) ContentScale.Crop else ContentScale.Fit,
                onLoading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                onFailure = { _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(shape).padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            imageVector = defaultIcon ?: Icons.Default.ShoppingBag,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                animationSpec = tween(durationMillis = 300)
            )
        }

    }
}