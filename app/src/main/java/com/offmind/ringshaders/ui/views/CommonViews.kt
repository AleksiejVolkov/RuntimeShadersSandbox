package com.offmind.ringshaders.ui.views

import android.content.Intent
import android.content.res.Configuration
import android.graphics.RuntimeShader
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.offmind.ringshaders.R
import com.offmind.ringshaders.domain.LoadedShader
import com.offmind.ringshaders.model.ShaderProperty


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BackgroundDimSlider(
    modifier: Modifier, value: Float, changeBackgroundDim: (newValue: Float) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Background Dim:", maxLines = 1, modifier = Modifier
                .widthIn(max = 80.dp)
                .basicMarquee()
        )
        Slider(modifier = Modifier.fillMaxWidth(), value = value, onValueChange = {
            changeBackgroundDim.invoke(it)
        })
    }
}

@Composable
fun ShaderOptions(
    modifier: Modifier, properties: List<ShaderProperty>, onPropertyChanged: (String, List<Float>) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        properties.forEach {
            item {
                if (it is ShaderProperty.FloatProperty) {
                    FloatPropertySlider(
                        displayName = it.displayName,
                        propertyName = it.name,
                        currentValue = it.value,
                        min = 0f,
                        max = 1f
                    ) { key, value ->
                        onPropertyChanged.invoke(key, listOf(value))
                    }
                }
                if (it is ShaderProperty.ColorProperty) {
                    ColorPropertyChooser(displayName = it.displayName,
                        propertyName = it.name,
                        currentValue = listOf(it.valueA, it.valueB, it.valueC),
                        onValueChanged = { key, value ->
                            onPropertyChanged.invoke(key, value)
                        })
                }
            }
        }
    }
}

@Composable
fun ShadersDropdown(
    shadersList: List<LoadedShader>, onNewShaderSelected: (LoadedShader) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Text("Select a shader", fontSize = 18.sp, modifier = Modifier.padding(10.dp))
            Divider()
            shadersList.forEachIndexed { index, shader ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    onNewShaderSelected(shader)
                }, text = {
                    Text(
                        text = shader.name,
                        color =
                        if (index == selectedIndex)
                            MaterialTheme.colorScheme.onSurface
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                })
            }
        }
    }
}

fun RuntimeShader.applyProperty(shaderProperty: ShaderProperty) {
    when (shaderProperty) {
        is ShaderProperty.FloatProperty -> {
            this.setFloatUniform(shaderProperty.name, shaderProperty.value)
        }

        is ShaderProperty.Float2Property -> {
            this.setFloatUniform(shaderProperty.name, shaderProperty.valueA, shaderProperty.valueB)
        }

        is ShaderProperty.Float3Property -> {
            this.setFloatUniform(
                shaderProperty.name, shaderProperty.valueA, shaderProperty.valueB, shaderProperty.valueC
            )
        }

        is ShaderProperty.ColorProperty -> {
            this.setFloatUniform(
                shaderProperty.name, shaderProperty.valueA, shaderProperty.valueB, shaderProperty.valueC
            )
        }

        is ShaderProperty.Float4Property -> {
            this.setFloatUniform(
                shaderProperty.name,
                shaderProperty.valueA,
                shaderProperty.valueB,
                shaderProperty.valueC,
                shaderProperty.valueD
            )
        }

        is ShaderProperty.ImageProperty -> {
            this.setInputShader(shaderProperty.name, ImageShader(shaderProperty.image, TileMode.Decal, TileMode.Decal))
            this.setFloatUniform(
                "${shaderProperty.name}_resolution",
                shaderProperty.image.width.toFloat(),
                shaderProperty.image.height.toFloat()
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FloatPropertySlider(
    displayName: String,
    propertyName: String,
    min: Float,
    max: Float,
    currentValue: Float,
    onValueChanged: (String, Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "${displayName}:", maxLines = 1, modifier = Modifier
                .width(90.dp)
                .basicMarquee()
        )
        Slider(modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
            value = currentValue,
            valueRange = min..max,
            onValueChange = {
                onValueChanged.invoke(propertyName, it)
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPropertyChooser(
    displayName: String, propertyName: String, currentValue: List<Float>, onValueChanged: (String, List<Float>) -> Unit
) {
    val color = Color(currentValue[0], currentValue[1], currentValue[2])
    var showColorPicker by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${displayName}:", maxLines = 1, modifier = Modifier
                .width(90.dp)
                .basicMarquee()
        )
        Box(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(5.dp)
            )
            .padding(1.dp)
            .background(color = color, shape = RoundedCornerShape(5.dp))
            .width(60.dp)
            .height(30.dp)
            .clickable {
                showColorPicker = true
            })
    }
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    var newColor: Color by remember { mutableStateOf(color) }
    if (showColorPicker) {
        Dialog(onDismissRequest = { showColorPicker = false }, content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier
                    .weight(if (isPortrait) 1f else 0.2f)
                    .clickable {
                        showColorPicker = false
                    })
                HsvColorPicker(modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .padding(10.dp),
                    controller = rememberColorPickerController(),
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        if (colorEnvelope.fromUser) {
                            newColor = colorEnvelope.color
                        }
                    })
                Button(colors = ButtonDefaults.buttonColors(containerColor = newColor), content = {
                    Text("Select")
                }, onClick = {
                    showColorPicker = false
                    onValueChanged.invoke(propertyName, listOf(newColor.red, newColor.green, newColor.blue))
                })
                Spacer(modifier = Modifier
                    .weight(if (isPortrait) 1f else 0.2f)
                    .clickable { showColorPicker = false })
            }
        })
    }
}

@Composable
fun TextWithLinks(
    modifier: Modifier, text: String
) {
    val linkStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onSurface, textDecoration = TextDecoration.Underline
    )

    val normalStyle = SpanStyle(
        color = MaterialTheme.colorScheme.onSurface
    )

    val annotatedString = buildAnnotatedString {
        val parts = text.split(" ")
        for (part in parts) {
            if (part.startsWith("https://")) {
                val start = length
                append(part)
                val end = length
                addStyle(linkStyle, start, end)
                addStringAnnotation(
                    tag = "URL", annotation = part, start = start, end = end
                )
            } else {
                val start = length
                append("$part ")
                val end = length
                addStyle(normalStyle, start, end)
            }
        }
    }

    // Get the current context
    val context = LocalContext.current

    // Create the ClickableText with the annotations
    ClickableText(modifier = modifier, text = annotatedString, maxLines = 3, onClick = { offset ->
        val annotation = annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
        annotation.firstOrNull()?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
            context.startActivity(intent)
        }
    })
}
