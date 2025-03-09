plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.compose.compiler)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.reviewdetails"
	buildFeatures.compose = true
}

dependencies {
	api(projects.feature.reviewDetails.api)
	implementation(projects.common.android)
	implementation(projects.common.navigation.api.ui)
	implementation(projects.common.pure)
	implementation(projects.common.tea.compose)
	implementation(projects.common.ui)
	implementation(projects.core.datasource.local.api)
	implementation(projects.core.entity)
	implementation(projects.feature.gallery.api)
	implementation(projects.feature.reviewCreation.api)
	implementation(projects.feature.search.api)

	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui)
	implementation(libs.koin.core)
	implementation(platform(libs.androidx.compose.bom))
}