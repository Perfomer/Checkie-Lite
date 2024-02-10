plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.tea"
}

dependencies {
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
	implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc10")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
}