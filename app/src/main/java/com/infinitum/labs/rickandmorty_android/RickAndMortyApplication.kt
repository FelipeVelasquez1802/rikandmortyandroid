package com.infinitum.labs.rickandmorty_android

import android.app.Application
import com.infinitum.labs.data.di.dataModule
import com.infinitum.labs.data.di.networkModule
import com.infinitum.labs.rickandmorty_android.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RickAndMortyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@RickAndMortyApplication)
            modules(
                networkModule,
                dataModule,
                appModule
            )
        }
    }
}