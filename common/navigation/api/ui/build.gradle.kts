plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	id("checkie.android.common")
}

android {
	namespace = "com.perfomer.checkielite.core.navigation"
	buildFeatures.compose = true
}

dependencies {
	api(projects.common.navigation.api.core)
	api(libs.androidx.compose.animation)
	api(libs.androidx.compose.ui)

	implementation(libs.androidx.activity)
	implementation(platform(libs.androidx.compose.bom))
}
