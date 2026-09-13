plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	id("checkie.android.common")
}

android {
	namespace = "com.perfomer.checkielite.common.ui"
	buildFeatures.compose = true
}

dependencies {
	implementation(projects.common.navigation.api.ui)
	implementation(projects.common.pure)
	implementation(projects.common.tea.compose)

	api(libs.backdrop)
	api(libs.coil.compose)
	api(libs.coil.network.okhttp)
	api(libs.fadingEdges)
	api(libs.androidx.compose.animation)
	implementation(libs.accompanist.systemuicontroller)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.navigationevent.compose)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.lifecycle.ktx)
	implementation(libs.composables.core)
	implementation(libs.googleFonts)
	implementation(libs.koin.core)
	implementation(libs.kotlinx.coroutines)
	implementation(platform(libs.androidx.compose.bom))

	testImplementation(libs.test.junitJupiter)
	testRuntimeOnly(libs.test.junitPlatformLauncher)
}
