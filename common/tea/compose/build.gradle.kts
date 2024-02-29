plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
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

	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}