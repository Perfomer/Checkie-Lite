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
	api(projects.feature.settings.api)
	implementation(projects.common.android)
	implementation(projects.common.navigation.api.ui)
	implementation(projects.common.pure)
	implementation(projects.common.tea.compose)
	implementation(projects.common.ui)
	implementation(projects.common.update.api)
	implementation(projects.core.datasource.local.api)
	implementation(projects.feature.main.api)

	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui)
	implementation(libs.koin.core)
	implementation(platform(libs.androidx.compose.bom))
}