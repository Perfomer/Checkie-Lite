package com.perfomer.checkielite.common.pure.appInfo

interface AppInfo {

    val versionCode: Int
    val versionName: String
    val isDebug: Boolean

    companion object : AppInfo by MutableAppInfoHolder
}

object MutableAppInfoHolder : AppInfo {
    override var versionCode: Int = 0
    override var versionName: String = "Unknown"
    override var isDebug: Boolean = false
}