plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.android"
}

dependencies {
	implementation(libs.appcompat)
	api(libs.lifecycle.ktx)
}