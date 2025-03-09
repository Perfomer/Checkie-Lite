plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.update.rustore"
}

dependencies {
	api(projects.common.update.api)
	implementation(projects.common.pure)

	implementation(libs.koin.core)
	implementation(libs.rustore.appupdate)
}