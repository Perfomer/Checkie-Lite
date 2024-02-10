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

	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
}