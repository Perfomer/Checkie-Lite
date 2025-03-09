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
	api(project(":common:navigation:api:ui"))
	implementation(project(":common:android"))

	api(libs.decompose)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui)
	implementation(libs.decompose.extensions.compose)
	implementation(libs.koin.core)
	implementation(libs.kotlinx.coroutines)
	implementation(platform(libs.androidx.compose.bom))
}