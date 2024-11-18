plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.gallery"
	buildFeatures.compose = true
}

dependencies {
	api(project(":feature:gallery:api"))

	implementation(project(":common:navigation:api:ui"))
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))
	implementation(project(":common:ui"))

	implementation(libs.accompanist.systemuicontroller)
	implementation(libs.activity.compose)
	implementation(libs.compose.material3)
	implementation(libs.compose.ui)
	implementation(libs.koin.core)
	implementation(libs.zoomable)
	implementation(platform(libs.compose.bom))
}