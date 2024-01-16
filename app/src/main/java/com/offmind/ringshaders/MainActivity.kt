package com.offmind.ringshaders

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.offmind.ringshaders.presenter.AppViewModel
import com.offmind.ringshaders.presenter.LoadingIndicatorsViewModel
import com.offmind.ringshaders.ui.screen.ShaderScreen
import com.offmind.ringshaders.ui.theme.RingShadersTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val appViewModel: AppViewModel = koinViewModel()
            val appState by appViewModel.screenState.collectAsState()

            RingShadersTheme {
                Scaffold { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(if (appState.isExpanded) PaddingValues(0.dp) else paddingValues),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        ShaderScreen(state = appState, appViewModel::onUserEvent)
                    }
                }
            }
        }
    }
}