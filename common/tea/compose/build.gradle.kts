plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.tea.compose"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	api(project(":common:tea:core"))

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.lifecycle.ktx)
}