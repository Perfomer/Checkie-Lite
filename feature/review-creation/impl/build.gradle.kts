plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.reviewcreation.impl"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	implementation(project(":feature:review-creation:api"))

	implementation(project(":core:entity"))
	implementation(project(":core:datasource:local:api"))
	implementation(project(":core:navigation:voyager"))

	implementation(project(":common:tea:compose"))
	implementation(project(":common:ui"))
	implementation(project(":common:pure"))

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.koin.core)
	implementation(libs.activity.compose)
}