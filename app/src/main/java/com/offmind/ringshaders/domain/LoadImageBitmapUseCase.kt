package com.offmind.ringshaders.domain

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.coroutineScope
import java.io.IOException

class LoadImageBitmapUseCase(private val context: Context) {

    suspend fun execute(fileName: String): ImageBitmap? = coroutineScope {
        return@coroutineScope try {
            context.assets.open(fileName).use { stream ->
                BitmapFactory.decodeStream(stream).asImageBitmap()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
