plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.main.impl"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	implementation(project(":feature:main:api"))
	implementation(project(":feature:review-creation:api"))
	implementation(project(":feature:review-details:api"))

	implementation(project(":core:entity"))
	implementation(project(":core:datasource:local:api"))
	implementation(project(":core:navigation:voyager"))

	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))
	implementation(project(":common:ui"))

	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.compose.material3)
	implementation(libs.koin.core)
}