package com.offmind.ringshaders.di

import com.offmind.ringshaders.domain.LoadShaderCodeUseCase
import com.offmind.ringshaders.domain.LoadShadersListUseCase
import com.offmind.ringshaders.presenter.ShaderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    factory { LoadShadersListUseCase(androidContext()) }
    factory { LoadShaderCodeUseCase(androidContext()) }
    viewModel { ShaderViewModel(get(), get()) }
}