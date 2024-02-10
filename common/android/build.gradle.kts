plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.android"
}

dependencies {
	implementation("androidx.appcompat:appcompat:1.6.1")
	api("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}