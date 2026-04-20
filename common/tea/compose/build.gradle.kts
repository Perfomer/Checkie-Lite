plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	id("checkie.android.common")
}

android {
	namespace = "com.perfomer.checkielite.common.tea.compose"
	buildFeatures.compose = true
}

dependencies {
	api(projects.common.tea.core)

	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.lifecycle.ktx)
	implementation(platform(libs.androidx.compose.bom))
}
