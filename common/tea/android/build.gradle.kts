plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.tea.android"
}

dependencies {
	api(project(":common:tea:core"))

	implementation(libs.androidx.lifecycle.ktx)
	implementation(libs.kotlinx.coroutines)
}