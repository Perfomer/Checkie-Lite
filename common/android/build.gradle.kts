plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	id("kotlin-parcelize")
	id("checkie.android.common")
}

android {
	namespace = "com.perfomer.checkielite.common.android"
}

dependencies {
	api(libs.androidx.lifecycle.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.koin.core)
}