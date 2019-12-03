package com.moyck.recoder251.base

import android.app.Application

class MyApplication: Application() {

    companion object{
        lateinit var context : Application
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}