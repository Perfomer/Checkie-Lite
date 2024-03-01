plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.navigation.voyager"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	api(project(":core:navigation:api"))
	implementation(project(":common:android"))

	api(libs.voyager.navigator)
	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.appcompat)
	implementation(libs.kotlinx.coroutines)
	implementation(libs.koin.core)
}