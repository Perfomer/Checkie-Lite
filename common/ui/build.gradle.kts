plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

android {
	namespace = "com.perfomer.checkielite.common.ui"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.2"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
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
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
}