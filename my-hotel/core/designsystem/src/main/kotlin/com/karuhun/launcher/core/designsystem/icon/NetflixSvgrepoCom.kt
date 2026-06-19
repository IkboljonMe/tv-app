package com.karuhun.launcher.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val NetflixSvgrepoCom: ImageVector
    get() {
        if (_NetflixSvgrepoCom != null) {
            return _NetflixSvgrepoCom!!
        }
        _NetflixSvgrepoCom = ImageVector.Builder(
            name = "NetflixSvgrepoCom",
            defaultWidth = 800.dp,
            defaultHeight = 800.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(29f, 25.75f)
                verticalLineTo(5f)
                lineToRelative(8.4f, -0.5f)
                verticalLineToRelative(39f)
                lineTo(29f, 43f)
                moveTo(19f, 22.25f)
                verticalLineTo(43f)
                lineToRelative(-8.4f, 0.5f)
                verticalLineTo(4.5f)
                horizontalLineToRelative(0f)
                lineTo(19f, 5f)
            }
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18.98f, 5f)
                lineTo(37.42f, 43.5f)
            }
            path(
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(10.58f, 4.5f)
                lineTo(29.02f, 43f)
            }
        }.build()

        return _NetflixSvgrepoCom!!
    }

@Suppress("ObjectPropertyName")
private var _NetflixSvgrepoCom: ImageVector? = null
