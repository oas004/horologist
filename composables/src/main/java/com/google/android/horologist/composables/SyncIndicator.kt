/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.horologist.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ProgressIndicatorDefaults

/**
 * SyncIndicator used to represent a process that is syncing without any end.
 *
 * @param modifier [Modifier] to be applied to the [SyncIndicator]
 * @param strokeWidth The stroke width for the sync indicator.
 * @param animationProgress The animation progress for the sync indicator. This just describes
 * where in the animation the composable should be rendered. The process can easily be looped.
 * @param indicatorColor The color of the sync icon.
 */
@ExperimentalHorologistComposablesApi
@Composable
public fun SyncIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    indicatorColor: Color,
    animationProgress: Float,
) {

    require(animationProgress in 0.0..1.0) {
        "Animation progress has to be between 0.0 and 1.0."
    }

    val localDensity = LocalDensity.current
    val stroke = with(localDensity) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    }

    Canvas(modifier = modifier.progressSemantics(animationProgress)) {
        drawSyncIcon(animationProgress = animationProgress, color = indicatorColor, stroke = stroke)
    }
}

private const val animationSpeed = 12

private fun DrawScope.drawSyncIcon(
    animationProgress: Float,
    color: Color,
    stroke: Stroke
) {
    val centerProgressAngle = animationAngles(
        animationProgress = animationProgress,
        type = SphereType.Center
    )

    val middleProgressAngle = animationAngles(
        animationProgress = animationProgress,
        type = SphereType.Middle
    )

    val endProgressAngle = animationAngles(
        animationProgress = animationProgress,
        type = SphereType.End
    )

    // Draw the outer circle
    drawArc(
        color = color,
        startAngle = 12f,
        sweepAngle = 245f,
        useCenter = false,
        style = stroke,
    )

    // Draw the arch closest to the center
    drawArc(
        color = color,
        startAngle = centerProgressAngle.startAngle,
        sweepAngle = centerProgressAngle.sweepAngle,
        useCenter = false,
        style = stroke,
        size = Size(this.size.height / 4, this.size.width / 4),
        topLeft = Offset(x = this.center.x / 1.6f, y = this.center.y / 1.2f),
        alpha = drawableAlpha(
            animationProgress = animationProgress,
            type = SphereType.Center
        )
    )

    // Draw the arch next closest to the center
    drawArc(
        color = color,
        startAngle = middleProgressAngle.startAngle,
        sweepAngle = middleProgressAngle.sweepAngle,
        useCenter = false,
        style = stroke,
        size = Size(this.size.height / 2, this.size.width / 2),
        topLeft = Offset(x = this.center.x / 2f, y = this.center.y / 2.2f),
        alpha = drawableAlpha(
            animationProgress = animationProgress,
            type = SphereType.Middle
        ),
    )

    // Draw the outer arch
    drawArc(
        color = color,
        startAngle = endProgressAngle.startAngle,
        sweepAngle = endProgressAngle.sweepAngle,
        useCenter = false,
        style = stroke,
        alpha = drawableAlpha(
            animationProgress = animationProgress,
            type = SphereType.End
        )
    )
}

private fun animationAngles(
    animationProgress: Float,
    type: SphereType
) = when (type) {
    SphereType.Center -> AnimationAngle(
        startAngle = maxOf(45f - 45f * animationProgress * animationSpeed, 0f),
        sweepAngle = maxOf(0f - 90f * animationProgress * animationSpeed, -90f)
    )

    SphereType.Middle -> AnimationAngle(
        startAngle = maxOf(45f - 45f * animationProgress * animationSpeed / 4, 0f),
        sweepAngle = maxOf(0f - 90f * animationProgress * animationSpeed / 4, -90f)
    )

    SphereType.End -> AnimationAngle(
        startAngle = maxOf(45f - 45f * animationProgress * animationSpeed / 6, 0f),
        sweepAngle = maxOf(0f - 90f * animationProgress * animationSpeed / 6, -90f)
    )
}

private enum class SphereType {
    Center, Middle, End
}

private fun drawableAlpha(animationProgress: Float, type: SphereType) = when (type) {
    SphereType.Center -> maxOf(1f - animationProgress * animationSpeed / 6, 0f)
    SphereType.Middle -> maxOf(1f - animationProgress * animationSpeed / 8, 0f)
    SphereType.End -> maxOf(1f - animationProgress * animationSpeed / 12, 0f)
}

private data class AnimationAngle(
    val startAngle: Float,
    val sweepAngle: Float,
)
