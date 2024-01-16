package com.offmind.ringshaders.presenter.data

sealed class UserEvent {
    data object OnClickExpand : UserEvent()
}