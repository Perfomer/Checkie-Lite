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

	implementation(libs.kotlinx.coroutines)
	implementation(libs.lifecycle.ktx)
}