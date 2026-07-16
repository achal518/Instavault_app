package com.instavault.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val InstagramIcon: ImageVector
    get() = ImageVector.Builder(
        name = "Instagram",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color.White),
            fillAlpha = 1.0f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = androidx.compose.ui.graphics.StrokeCap.Butt,
            strokeLineJoin = androidx.compose.ui.graphics.StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12f, 2.163c3.204f, 0f, 3.584f, 0.012f, 4.85f, 0.07f)
            c1.366f, 0.062f, 2.633f, 0.344f, 3.608f, 1.319f
            c0.975f, 0.975f, 1.257f, 2.242f, 1.319f, 3.608f
            c0.058f, 1.266f, 0.07f, 1.646f, 0.07f, 4.85f
            s-0.012f, 3.584f, -0.07f, 4.85f
            c-0.062f, 1.366f, -0.344f, 2.633f, -1.319f, 3.608f
            c-0.975f, 0.975f, -2.242f, 1.257f, -3.608f, 1.319f
            c-1.266f, 0.058f, -1.646f, 0.07f, -4.85f, 0.07f
            s-3.584f, -0.012f, -4.85f, -0.07f
            c-1.366f, -0.062f, -2.633f, -0.344f, -3.608f, -1.319f
            c-0.975f, -0.975f, -1.257f, -2.242f, -1.319f, -3.608f
            c-0.058f, -1.266f, -0.07f, -1.646f, -0.07f, -4.85f
            s0.012f, -3.584f, 0.07f, -4.85f
            c0.062f, -1.366f, 0.344f, -2.633f, 1.319f, -3.608f
            c0.975f, -0.975f, 2.242f, -1.257f, 3.608f, -1.319f
            c1.266f, -0.058f, 1.646f, -0.07f, 4.85f, -0.07f
            moveTo(12f, 0f
            )
            c-3.259f, 0f, -3.667f, 0.014f, -4.947f, 0.072f
            c-1.72f, 0.079f, -3.279f, 0.54f, -4.516f, 1.777f
            c-1.237f, 1.237f, -1.698f, 2.796f, -1.777f, 4.516f
            C0.014f, 8.333f, 0f, 8.741f, 0f, 12f
            s0.014f, 3.667f, 0.072f, 4.947f
            c0.079f, 1.72f, 0.54f, 3.279f, 1.777f, 4.516f
            c1.237f, 1.237f, 2.796f, 1.698f, 4.516f, 1.777f
            c1.28f, 0.058f, 1.688f, 0.072f, 4.947f, 0.072f
            s3.667f, -0.014f, 4.947f, -0.072f
            c1.72f, -0.079f, 3.279f, -0.54f, 4.516f, -1.777f
            c1.237f, -1.237f, 1.698f, -2.796f, 1.777f, -4.516f
            c0.058f, -1.28f, 0.072f, -1.688f, 0.072f, -4.947f
            s-0.014f, -3.667f, -0.072f, -4.947f
            c-0.079f, -1.72f, -0.54f, -3.279f, -1.777f, -4.516f
            c-1.237f, -1.237f, -2.796f, -1.698f, -4.516f, -1.777f
            C15.667f, 0.014f, 15.259f, 0f, 12f, 0f
            close()
            )
            moveTo(12f, 5.838f
            )
            c-3.403f, 0f, -6.162f, 2.759f, -6.162f, 6.162f
            s2.759f, 6.162f, 6.162f, 6.162f
            s6.162f, -2.759f, 6.162f, -6.162f
            S15.403f, 5.838f, 12f, 5.838f
            close()
            moveTo(12f, 16f
            )
            c-2.209f, 0f, -4f, -1.791f, -4f, -4f
            s1.791f, -4f, 4f, -4f
            s4f, 1.791f, 4f, 4f
            S14.209f, 16f, 12f, 16f
            close()
            moveTo(19.853f, 5.587f
            )
            c0f, 0.795f, -0.645f, 1.44f, -1.44f, 1.44f
            s-1.44f, -0.645f, -1.44f, -1.44f
            s0.645f, -1.44f, 1.44f, -1.44f
            S19.853f, 4.792f, 19.853f, 5.587f
            close()
        }
    }.build()
