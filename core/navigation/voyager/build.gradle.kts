plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
	namespace = "com.perfomer.checkielite.navigation.voyager"

	buildFeatures.compose = true
	composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
	api(project(":core:navigation:api"))
	implementation(project(":common:android"))

	//	Compose
	implementation(platform("androidx.compose:compose-bom:2023.03.00"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	//	Android
	implementation("androidx.appcompat:appcompat:1.6.1")

	//  Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

	//	Navigation
	implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc10")
	implementation("cafe.adriel.voyager:voyager-koin:1.0.0-rc10")

	//	Di
	implementation("io.insert-koin:koin-android:3.5.0")
	implementation("io.insert-koin:koin-core:3.5.0")
}