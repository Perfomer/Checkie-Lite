plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.ui"
	buildFeatures.compose = true
}

dependencies {
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))

	api(libs.coil.compose)
	api(libs.coil.network.okhttp)
	api(libs.fading.edges)
	implementation(libs.accompanist.systemuicontroller)
	implementation(libs.activity.compose)
	implementation(libs.appcompat)
	implementation(libs.compose.material3)
	implementation(libs.compose.ui.tooling)
	implementation(libs.google.fonts)
	implementation(libs.koin.core)
	implementation(libs.lifecycle.ktx)
	implementation(platform(libs.compose.bom))
}