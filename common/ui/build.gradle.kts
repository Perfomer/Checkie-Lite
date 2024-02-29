plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.common.ui"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	implementation(project(":common:pure"))
	implementation(project(":common:tea:compose"))

	//	Compose
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3:1.1.2")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
	implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
	api("io.coil-kt:coil-compose:2.5.0")

	//	Navigation
	implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc10")
	implementation("cafe.adriel.voyager:voyager-koin:1.0.0-rc10")

	//	Shimmer
	implementation("com.valentinilk.shimmer:compose-shimmer:1.2.0")

	//	Di
	implementation("io.insert-koin:koin-android:3.5.0")
	implementation("io.insert-koin:koin-core:3.5.0")

	//	Lifecycle
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}