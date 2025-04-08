package com.perfomer.checkielite.common.ui.util.app

import android.content.Context
import android.content.pm.PackageManager
import com.perfomer.checkielite.common.ui.util.isDebug

data class AppInfo(
    val versionCode: Int = 0,
    val versionName: String = "Unknown",
    val isDebug: Boolean = false,
)

object AppInfoHolder {

    private var appInfo: AppInfo = AppInfo()

    fun initialize(context: Context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

            appInfo = AppInfo(
                versionCode = packageInfo.versionCode,
                versionName = packageInfo.versionName!!,
                isDebug = context.isDebug(),
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    fun getAppInfo(): AppInfo {
        return appInfo
    }
}