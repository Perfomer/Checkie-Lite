plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.ui"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
}

dependencies {
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))

	api(libs.coil)
	api(libs.fading.edges)
	implementation(libs.accompanist.systemuicontroller)
	implementation(libs.activity.compose)
	implementation(libs.appcompat)
	implementation(libs.compose.material3)
	implementation(libs.compose.ui.tooling)
	implementation(libs.google.fonts)
	implementation(libs.koin.core)
	implementation(libs.lifecycle.ktx)
	implementation(libs.voyager.koin)
	implementation(libs.voyager.navigator)
	implementation(platform(libs.compose.bom))
}