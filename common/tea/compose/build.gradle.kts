plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.tea.compose"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
}

dependencies {
	api(project(":common:tea:core"))

	implementation(libs.compose.ui)
	implementation(libs.lifecycle.ktx)
	implementation(platform(libs.compose.bom))
}