plugins {
	alias(libs.plugins.android.library)
	id("checkie.android.common")
}

android {
	namespace = "com.perfomer.checkielite.common.update.rustore"
}

dependencies {
	api(projects.common.update.api)
	implementation(projects.common.pure)

	implementation(libs.koin.core)
	implementation(libs.rustore.appupdate)
}
