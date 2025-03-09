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
	api(projects.feature.gallery.api)
	implementation(projects.common.navigation.api.ui)
	implementation(projects.common.pure)
	implementation(projects.common.tea.compose)
	implementation(projects.common.ui)

	implementation(libs.accompanist.systemuicontroller)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui)
	implementation(libs.koin.core)
	implementation(libs.zoomable)
	implementation(platform(libs.androidx.compose.bom))
}