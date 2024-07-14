package com.perfomer.checkielite.appinfo

import com.perfomer.checkielite.BuildConfig
import com.perfomer.checkielite.common.pure.appInfo.AppInfo
import com.perfomer.checkielite.common.pure.appInfo.AppInfoProvider

class AppInfoProviderImpl : AppInfoProvider {

    override fun getAppInfo(): AppInfo {
        return AppInfo(
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME,
        )
    }
}