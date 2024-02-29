plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.tea.android"
}

dependencies {
	api(project(":common:tea:core"))
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation(libs.kotlinx.coroutines)
}