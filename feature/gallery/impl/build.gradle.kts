plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.gallery"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
}

dependencies {
	api(project(":feature:gallery:api"))

	implementation(project(":common:navigation:voyager"))
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))
	implementation(project(":common:ui"))

	implementation(libs.activity.compose)
	implementation(libs.compose.material3)
	implementation(libs.compose.ui)
	implementation(libs.koin.core)
	implementation(platform(libs.compose.bom))
}