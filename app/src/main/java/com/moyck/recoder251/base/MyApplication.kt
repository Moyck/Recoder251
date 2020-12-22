package com.moyck.recoder251.base

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.moyck.recoder251.utils.AppOpenManager





class MyApplication: Application() {

    companion object{
        lateinit var context : Application
        lateinit var appOpenManager: AppOpenManager
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        MobileAds.initialize(
            this
        ) { }
        appOpenManager = AppOpenManager(this);
    }

}