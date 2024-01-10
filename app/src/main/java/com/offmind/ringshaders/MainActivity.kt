package com.offmind.ringshaders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.offmind.ringshaders.presenter.ShaderViewModel
import com.offmind.ringshaders.ui.screen.ShaderScreen
import com.offmind.ringshaders.ui.theme.RingShadersTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val ringViewModel: ShaderViewModel = koinViewModel()
            val state by ringViewModel.screenState.collectAsState()

            RingShadersTheme {
                Scaffold { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        ShaderScreen(state = state) {
                            ringViewModel.onUserEvent(it)
                        }
                    }
                }
            }
        }
    }
}