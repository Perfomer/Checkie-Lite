plugins {
	alias(libs.plugins.android.library)
	id("checkie.android.common")
}

android {
	namespace = "com.perfomer.checkielite.common.tea.android"
}

dependencies {
	api(projects.common.tea.core)

	implementation(libs.androidx.lifecycle.ktx)
	implementation(libs.kotlinx.coroutines)
}
