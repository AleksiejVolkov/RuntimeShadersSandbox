package com.offmind.ringshaders

import android.app.Application
import com.offmind.ringshaders.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class RingShaderApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@RingShaderApplication)
            modules(appModule)
        }
    }
}