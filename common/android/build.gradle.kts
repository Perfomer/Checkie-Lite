plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	id("kotlin-parcelize")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.android"
}

dependencies {
	api(libs.lifecycle.ktx)
	implementation(libs.appcompat)
	implementation(libs.koin.core)
}