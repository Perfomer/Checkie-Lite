plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite"

    androidResources {
        generateLocaleConfig = true
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"

            manifestPlaceholders["APP_ICON"] = "@drawable/logo_brand_lite"
            manifestPlaceholders["APP_NAME"] = "@string/app_name_debug"
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

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

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(projects.common.android)
    implementation(projects.common.navigation.decompose)
    implementation(projects.common.pure)
    implementation(projects.common.ui)
    implementation(projects.common.update.rustore)
    implementation(projects.core.datasource.local.impl)
    implementation(projects.feature.gallery.impl)
    implementation(projects.feature.main.impl)
    implementation(projects.feature.reviewCreation.impl)
    implementation(projects.feature.reviewDetails.impl)
    implementation(projects.feature.search.impl)
    implementation(projects.feature.settings.impl)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.ktx.splashscreen)
    implementation(libs.androidx.lifecycle.ktx)
    implementation(libs.composables.core)
    implementation(libs.decompose)
    implementation(libs.decompose.extensions.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(platform(libs.androidx.compose.bom))

    testImplementation(libs.test.junitJupiter)
}