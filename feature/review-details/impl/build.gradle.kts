plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.feature.reviewdetails.impl"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	implementation(project(":feature:review-creation:api"))
	implementation(project(":feature:review-details:api"))

	implementation(project(":core:entity"))
	implementation(project(":core:datasource:local:api"))

	implementation(project(":common:tea:compose"))
	implementation(project(":common:ui"))
	implementation(project(":common:pure"))

	//	Navigation
	implementation(project(":core:navigation:api"))
	implementation(project(":core:navigation:voyager"))
	implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc10")

	//	Compose
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
	implementation("androidx.activity:activity-compose:1.8.1")

	//	Di
	implementation("io.insert-koin:koin-android:3.5.0")
	implementation("io.insert-koin:koin-core:3.5.0")
}