plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.navigation.voyager"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
}

dependencies {
	api(project(":common:navigation:api"))
	implementation(project(":common:android"))

	api(libs.voyager.navigator)
	implementation(libs.appcompat)
	implementation(libs.compose.ui)
	implementation(libs.koin.core)
	implementation(libs.kotlinx.coroutines)
	implementation(libs.voyager.bottomsheet)
	implementation(platform(libs.compose.bom))
}