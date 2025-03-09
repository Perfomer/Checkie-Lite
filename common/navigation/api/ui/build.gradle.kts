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
	api(projects.common.navigation.api.core)

	implementation(libs.androidx.activity)
	implementation(libs.androidx.compose.ui)
	implementation(platform(libs.androidx.compose.bom))
}