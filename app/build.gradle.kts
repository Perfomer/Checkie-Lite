plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite"

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"

            manifestPlaceholders["APP_ICON"] = "@drawable/logo_brand_lite"
            manifestPlaceholders["APP_NAME"] = "@string/app_name_debug"
        }

        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            manifestPlaceholders["APP_ICON"] = "@drawable/logo_brand"
            manifestPlaceholders["APP_NAME"] = "@string/app_name"
        }
    }
}

dependencies {
    implementation(project(":feature:gallery:impl"))
    implementation(project(":feature:main:impl"))
    implementation(project(":feature:review-creation:impl"))
    implementation(project(":feature:review-details:impl"))
    implementation(project(":feature:search:impl"))

    implementation(project(":core:datasource:local:impl"))

    implementation(project(":common:android"))
    implementation(project(":common:navigation:voyager"))
    implementation(project(":common:ui"))

    implementation(libs.activity)
    implementation(libs.activity.compose)
    implementation(libs.core.ktx)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.bottomsheet)
    implementation(platform(libs.compose.bom))
}