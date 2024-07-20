plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.settings"
	buildFeatures.compose = true
}

dependencies {
	api(project(":feature:settings:api"))

	implementation(project(":feature:main:api"))

	implementation(project(":common:android"))
	implementation(project(":common:navigation:voyager"))
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))
	implementation(project(":common:ui"))

	implementation(project(":core:datasource:local:api"))

	implementation(libs.activity.compose)
	implementation(libs.compose.material3)
	implementation(libs.compose.ui)
	implementation(libs.koin.core)
	implementation(platform(libs.compose.bom))
}