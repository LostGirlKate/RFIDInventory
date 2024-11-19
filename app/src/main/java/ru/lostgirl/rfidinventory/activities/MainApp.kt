package ru.lostgirl.rfidinventory.activities

import android.app.Application
import ru.lostgirl.rfidinventory.di.dataModule
import ru.lostgirl.rfidinventory.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.lostgirl.rfidinventory.di.appModule
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        plant(DebugTree())
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApp)
            modules(listOf(appModule, domainModule, dataModule))
        }
    }
}
