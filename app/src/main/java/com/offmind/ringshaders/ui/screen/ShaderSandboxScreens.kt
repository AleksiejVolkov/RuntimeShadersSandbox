package com.offmind.ringshaders.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.asFloatState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.offmind.ringshaders.presenter.ScreenState
import com.offmind.ringshaders.presenter.UserEvent
import com.offmind.ringshaders.ui.utils.ProduceDrawLoopCounter
import com.offmind.ringshaders.ui.views.BackgroundDimSlider
import com.offmind.ringshaders.ui.views.CardWithShader
import com.offmind.ringshaders.ui.views.ShaderContent
import com.offmind.ringshaders.ui.views.ShaderOptions

@Composable
fun ShaderScreen(
    state: ScreenState,
    onUserEvent: (UserEvent) -> Unit
) {
    val time = ProduceDrawLoopCounter(10f).asFloatState()

    Surface(modifier = Modifier.fillMaxSize()) {
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ScreenPortrait(
                time = time.floatValue,
                state = state,
                onUserEvent = onUserEvent,
            )
        } else {
            ScreenLandscape(
                time = time.floatValue,
                state = state,
                onUserEvent = onUserEvent,
            )
        }
    }
}

@Composable
fun ScreenPortrait(
    time: Float,
    state: ScreenState,
    onUserEvent: (UserEvent) -> Unit
) {
    if (state.isExpanded) {
        Column(modifier = Modifier.fillMaxSize()) {
            ShaderContent(state = state, onUserEvent = onUserEvent, time = time)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            BackgroundDimSlider(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.backgroundDim,
                changeBackgroundDim = {
                    onUserEvent.invoke(UserEvent.OnChangeBackgroundDim(it))
                }
            )
            CardWithShader(
                modifier = Modifier.weight(1f),
                state = state,
                onUserEvent = onUserEvent,
                time = time
            )
            ShaderOptions(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp, max = 250.dp),
                state.shaderProperties
            ) { key, value ->
                onUserEvent.invoke(UserEvent.OnShaderPropertyChanged(key, value))
            }
        }
    }
}

@Composable
fun ScreenLandscape(
    time: Float,
    state: ScreenState,
    onUserEvent: (UserEvent) -> Unit
) {
    if (state.isExpanded) {
        Column(modifier = Modifier.fillMaxSize()) {
            ShaderContent(state = state, onUserEvent = onUserEvent, time = time)
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.weight(0.5f)) {
                BackgroundDimSlider(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    value = state.backgroundDim,
                    changeBackgroundDim = {
                        onUserEvent.invoke(UserEvent.OnChangeBackgroundDim(it))
                    }
                )
                Divider()
                ShaderOptions(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                        .height(300.dp),
                    state.shaderProperties
                ) { key, value ->
                    onUserEvent.invoke(UserEvent.OnShaderPropertyChanged(key, value))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            CardWithShader(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                state = state,
                onUserEvent = onUserEvent,
                time = time
            )
        }
    }
}