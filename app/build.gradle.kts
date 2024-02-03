plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.perfomer.checkielite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.perfomer.checkielite"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    //	Koin
    implementation("io.insert-koin:koin-android:3.5.0")
    implementation("io.insert-koin:koin-core:3.5.0")

    //  Navigation
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc10")
    implementation("cafe.adriel.voyager:voyager-transitions:1.0.0-rc10")

    //  Feature
    implementation(project(":feature:splash:api"))
    implementation(project(":feature:splash:impl"))
    implementation(project(":feature:welcome:impl"))
    implementation(project(":feature:registration:impl"))
    implementation(project(":feature:main:api"))
    implementation(project(":feature:main:impl"))
    implementation(project(":feature:orders:impl"))
    implementation(project(":feature:calendar:impl"))
    implementation(project(":feature:production:impl"))
    implementation(project(":feature:settings:impl"))

    //  Common
    implementation(project(":common:ui"))
    implementation(project(":common:android"))

    //  Core
    implementation(project(":core:navigation:api"))
    implementation(project(":core:navigation:voyager"))
    implementation(project(":core:local:preferences"))
    implementation(project(":core:storage:impl"))
}