package com.perfomer.checkielite.appinfo

import android.content.Context
import android.content.pm.PackageManager
import com.perfomer.checkielite.common.pure.appInfo.MutableAppInfoHolder
import com.perfomer.checkielite.common.ui.util.isDebug

internal object AppInfoInitializer {

    fun initialize(context: Context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

            with(MutableAppInfoHolder) {
                versionCode = packageInfo.versionCode
                versionName = packageInfo.versionName!!
                isDebug = context.isDebug()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}