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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.horologist.compose.tools.WearPreview
import kotlinx.coroutines.delay

private enum class PreviewAnimationStates(val target: Float) {
    Start(0f), End(1f)
}

@OptIn(ExperimentalHorologistComposablesApi::class)
@WearPreview
@Composable
fun SyncAnimationPreview() {
    var progressState by remember { mutableStateOf(PreviewAnimationStates.Start) }

    val transition = updateTransition(
        targetState = progressState,
        label = "Sync indicator progress"
    )

    val progress by transition.animateFloat(
        label = "Progress",
        targetValueByState = { it.target },
        transitionSpec = {
            tween(durationMillis = 3000, easing = LinearEasing)
        }
    )
    SyncIndicator(
        modifier = Modifier
            .padding(20.dp)
            .size(80.dp)
            .clickable {
                progressState = if (progressState == PreviewAnimationStates.Start) {
                    PreviewAnimationStates.End
                } else {
                    PreviewAnimationStates.Start
                }
            },
        animationProgress = progress,
        indicatorColor = Color.LightGray
    )

    LaunchedEffect(Unit) {
        while (true) {
            progressState = PreviewAnimationStates.End
            delay(1000)
            progressState = PreviewAnimationStates.Start
        }
    }
}
