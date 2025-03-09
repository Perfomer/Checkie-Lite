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

	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.lifecycle.ktx)
	implementation(platform(libs.androidx.compose.bom))
}