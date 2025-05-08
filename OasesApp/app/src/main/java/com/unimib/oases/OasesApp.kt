package com.unimib.oases

import android.app.Application
import android.content.Context
import com.unimib.oases.util.PermissionHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OasesApp: Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
        PermissionHelper.init(this)
    }

    companion object {
        private lateinit var instance: OasesApp
        fun getAppContext(): Context = instance.applicationContext
    }
}