package com.e.vidihub.application

import android.app.Application
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VidiHubApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
//
//        // init AppMetrics and Activate for Crashlytics
//        val config = YandexMetricaConfig.newConfigBuilder("89810e30-3a94-4d75-adef-92314107133e")
//            .withNativeCrashReporting(true)
//            .withLocationTracking(false)
//            .withAppVersion("1.0.0")
//            .build()
//
//        YandexMetrica.activate(this, config)
//        YandexMetrica.enableActivityAutoTracking(this)
//    }
}