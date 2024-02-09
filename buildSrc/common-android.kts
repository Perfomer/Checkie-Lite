android {
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    packagingOptions.exclude("META-INF/*.kotlin_module")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs += "-Xcontext-receivers"
    }

    buildTypes {
        debug {
            setMinifyEnabled(false)
        }

        release {
            setMinifyEnabled(false)
        }
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}
