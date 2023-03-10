package com.lai.sticker

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sApp = this
    }


    companion object {
        private var sApp: Application? = null

        fun getApp(): Application {
            return sApp!!
        }
    }
}