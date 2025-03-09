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
	api(libs.fadingEdges)
	implementation(libs.accompanist.systemuicontroller)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.lifecycle.ktx)
	implementation(libs.googleFonts)
	implementation(libs.koin.core)
	implementation(platform(libs.androidx.compose.bom))
}