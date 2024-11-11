plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.navigation"
	buildFeatures.compose = true
}

dependencies {
	api(project(":common:navigation:api:core"))
	implementation(libs.compose.ui)
	implementation(libs.activity)
	implementation(platform(libs.compose.bom))
}