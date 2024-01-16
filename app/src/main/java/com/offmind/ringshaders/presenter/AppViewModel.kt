package com.offmind.ringshaders.presenter

import androidx.lifecycle.ViewModel
import com.offmind.ringshaders.presenter.data.ScreenState
import com.offmind.ringshaders.presenter.data.UserEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppViewModel : ViewModel() {

    private val _screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState

    fun onLoaded() {
        _screenState.value = _screenState.value.copy(isLoading = false)
    }

    fun onUserEvent(userEvent: UserEvent) {
        when (userEvent) {
            UserEvent.OnClickExpand -> changeExpandState()
        }
    }

    private fun changeExpandState() {
        _screenState.value = _screenState.value.copy(isExpanded = !_screenState.value.isExpanded)
    }
}