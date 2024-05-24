plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.tea.compose"
	buildFeatures.compose = true
}

dependencies {
	api(project(":common:tea:core"))

	implementation(libs.compose.ui)
	implementation(libs.lifecycle.ktx)
	implementation(platform(libs.compose.bom))
}