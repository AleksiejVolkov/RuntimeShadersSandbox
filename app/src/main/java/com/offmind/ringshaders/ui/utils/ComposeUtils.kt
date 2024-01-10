package com.offmind.ringshaders.ui.utils

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState

@Composable
fun ProduceDrawLoopCounter(speed: Float = 1f): State<Float> {
    return produceState(0f) {
        var firstFrame = -1L
        while (true) {
            withInfiniteAnimationFrameMillis {
                if (firstFrame < 0) {
                    firstFrame = it
                }

                if (it - firstFrame > speed) {
                    firstFrame = it
                    value++
                }
            }
        }
    }
}
