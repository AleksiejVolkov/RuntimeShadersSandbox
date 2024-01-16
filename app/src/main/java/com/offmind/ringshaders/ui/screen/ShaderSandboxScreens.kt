package com.offmind.ringshaders.ui.screen

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.asFloatState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.offmind.ringshaders.presenter.data.ScreenState
import com.offmind.ringshaders.presenter.data.UserEvent
import com.offmind.ringshaders.ui.utils.ProduceDrawLoopCounter
import com.offmind.ringshaders.ui.views.CardWithShader
import com.offmind.ringshaders.ui.views.ShaderContent
import com.offmind.ringshaders.ui.views.ShaderOptions

@Composable
fun ShaderScreen(
    state: ScreenState,
    onUserEvent: (UserEvent) -> Unit
) {
    val time = ProduceDrawLoopCounter(10f).asFloatState()

    if (state.isExpanded) {
        BackHandler {
            onUserEvent.invoke(UserEvent.OnClickExpand)
        }
    }

    if (state.isExpanded) {
        Column(modifier = Modifier.fillMaxSize()) {
            ShaderContent(state = state, time = time.floatValue)
        }
    } else {
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenPortrait(
    time: Float,
    state: ScreenState,
    onUserEvent: (UserEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        val pagerState = rememberPagerState(pageCount = {
            3
        })
        HorizontalPager(
            modifier = Modifier.weight(1f),
            pageSpacing = 15.dp,
            state = pagerState
        ) { page ->
            CardWithShader(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onUserEvent = onUserEvent,
                time = time,
                content = { state, time ->
                    ShaderContent(state = state, time = time)
                }
            )
        }
        ShaderOptions(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 150.dp, max = 250.dp)
                .graphicsLayer {
                    alpha = 1 - 2 * pagerState.currentPageOffsetFraction
                },
            properties = state.shaderProperties,
            state = state,
            onUserEvent = onUserEvent
        ) { key, value ->
            onUserEvent.invoke(UserEvent.OnShaderPropertyChanged(key, value))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScreenLandscape(
    time: Float,
    state: ScreenState,
    onUserEvent: (UserEvent) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {
        10
    })

    Row(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.weight(0.5f)) {
            ShaderOptions(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = 1 - 2 * pagerState.currentPageOffsetFraction
                    }
                    .padding(horizontal = 20.dp, vertical = 5.dp)
                    .height(300.dp),
                properties = state.shaderProperties,
                state = state,
                onUserEvent = onUserEvent
            ) { key, value ->
                onUserEvent.invoke(UserEvent.OnShaderPropertyChanged(key, value))
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        VerticalPager(
            modifier = Modifier.weight(1f),
            pageSpacing = 15.dp,
            state = pagerState
        ) { page ->
            CardWithShader(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                onUserEvent = onUserEvent,
                time = time,
                content = { state, time ->
                    ShaderContent(state = state, time = time)
                })
        }
    }
}
