package com.markjin.artmall

import android.app.Application
import android.util.DisplayMetrics
import cn.jpush.android.api.JPushInterface


/**
 * application
 */
class AppContext : Application() {
    companion object {
        lateinit var instance: AppContext
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        WbSdk.install(this, AuthInfo(this, Contants.SINA_APPKEY, Contants.SINA_REDIRECT_URL, Contants.SINA_SCOPE))
        JPushInterface.init(this)
        JPushInterface.setDebugMode(true)
    }

      fun getDisplayMetrics(): DisplayMetrics {
        return applicationContext.resources.displayMetrics
    }

      fun getVersionCode(): Int {
        return this.packageManager.getPackageInfo(this.packageName, 0).versionCode
    }

      fun getVersionName(): String {
        return this.packageManager.getPackageInfo(this.packageName, 0).versionName
    }
}