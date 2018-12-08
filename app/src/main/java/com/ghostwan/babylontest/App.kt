package com.ghostwan.babylontest

import android.app.Application
import com.ghostwan.babylontest.di.repositoryModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(repositoryModule))

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant()
        }
        Timber.tag("BabylonTest")
    }
}



