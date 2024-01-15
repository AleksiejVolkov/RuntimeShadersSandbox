package com.offmind.ringshaders.ui.views

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.asFloatState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.offmind.ringshaders.R
import com.offmind.ringshaders.model.ShaderProperty
import com.offmind.ringshaders.presenter.ScreenState
import com.offmind.ringshaders.presenter.UserEvent
import com.offmind.ringshaders.presenter.availableShadersList
import com.offmind.ringshaders.ui.utils.ProduceDrawLoopCounter

@Composable
fun CardWithShader(
    modifier: Modifier,
    state: ScreenState,
    onUserEvent: (UserEvent) -> Unit,
    time: Float
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.padding(horizontal = 8.dp), text = state.shader?.name ?: "", fontSize = 19.sp)
            ShadersDropdown(shadersList = availableShadersList) {
                onUserEvent.invoke(UserEvent.OnSelectNewShader(it))
            }
        }
        ShaderContent(state = state, onUserEvent = onUserEvent, time = time)
        TextWithLinks(
            modifier = Modifier.padding(10.dp), text = state.shader?.description ?: ""
        )
    }
}

@Composable
fun ColumnScope.ShaderContent(
    state: ScreenState,
    time: Float,
    onUserEvent: (UserEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(horizontal = 7.dp)
            .clipToBounds(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(
                Color.Black.copy(alpha = state.backgroundDim), BlendMode.Darken
            )
        )
        state.compiledShader?.let {
            ShaderBox(
                modifier = Modifier.fillMaxSize(),
                shader = it,
                shaderProperties = state.shaderProperties,
                time = time
            )
        }
        Box(
            modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center
        ) {
            Text("Loading", color = Color.White, fontWeight = FontWeight.W400, fontSize = 20.sp)
        }
        Row(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 15.dp),
        ) {
            IconButton(onClick = {
                onUserEvent.invoke(UserEvent.OnClickExpand)
            }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = if(state.isExpanded) R.drawable.collapse_icon else R.drawable.expand_icon),
                    contentDescription = "expand")
            }
        }
    }
}

@Composable
fun ShaderBox(
    modifier: Modifier,
    shader: RuntimeShader,
    shaderProperties: List<ShaderProperty>,
    time: Float
) {
    shaderProperties.forEach {
        shader.applyProperty(it)
    }

    shader.setFloatUniform("time", time)

    Box(modifier = modifier
        .onSizeChanged { size ->
            shader.setFloatUniform(
                "resolution", size.width.toFloat(), size.height.toFloat()
            )
        }
        .graphicsLayer {
            this.renderEffect = RenderEffect
                .createRuntimeShaderEffect(
                    shader, "image"
                )
                .asComposeRenderEffect()
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        )
    }
}
