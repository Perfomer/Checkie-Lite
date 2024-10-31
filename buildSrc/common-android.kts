android {
    compileSdk = 35

    defaultConfig {
        targetSdk = 35
        minSdk = 24

        versionCode = 8
        versionName = "1.5.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    packagingOptions.exclude("META-INF/*.kotlin_module")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs += "-Xcontext-receivers"
    }

    buildTypes {
        debug {
            setMinifyEnabled(false)
        }

        release {
            // After AGP 8.4.0 minification should be enabled only for `:app` module.
            // https://stackoverflow.com/a/78794247/5328992
            setMinifyEnabled(false)
        }
    }
}
