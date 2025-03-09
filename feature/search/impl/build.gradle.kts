plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.kotlinx.serialization)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.search"
	buildFeatures.compose = true
}

dependencies {
	api(projects.feature.search.api)
	implementation(projects.common.android)
	implementation(projects.common.navigation.api.ui)
	implementation(projects.common.pure)
	implementation(projects.common.tea.compose)
	implementation(projects.common.ui)
	implementation(projects.core.datasource.local.api)
	implementation(projects.feature.reviewDetails.api)

	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui)
	implementation(libs.koin.core)
	implementation(platform(libs.androidx.compose.bom))
}