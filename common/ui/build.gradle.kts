plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.ui"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))

	api(libs.coil)
	implementation(platform(libs.compose.bom))
	implementation(libs.compose.material3)
	implementation(libs.accompanist.systemuicontroller)
	implementation(libs.voyager.navigator)
	implementation(libs.voyager.koin)
	implementation(libs.appcompat)
	implementation(libs.lifecycle.ktx)
	implementation(libs.koin.core)
	debugImplementation(libs.compose.ui.tooling)
}