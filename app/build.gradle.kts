plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite"

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
    implementation(project(":feature:gallery:impl"))
    implementation(project(":feature:main:impl"))
    implementation(project(":feature:review-creation:impl"))
    implementation(project(":feature:review-details:impl"))
    implementation(project(":feature:search:impl"))
    implementation(project(":feature:settings:impl"))

    implementation(project(":core:datasource:local:impl"))

    implementation(project(":common:android"))
    implementation(project(":common:navigation:voyager"))
    implementation(project(":common:pure"))
    implementation(project(":common:ui"))

    implementation(libs.activity)
    implementation(libs.activity.compose)
    implementation(libs.composables.core)
    implementation(libs.compose.material3)
    implementation(libs.core.ktx)
    implementation(libs.core.ktx.splashscreen)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.lifecycle.ktx)
    implementation(libs.rustore.appupdate)
    implementation(libs.voyager.bottomsheet)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)
    implementation(platform(libs.compose.bom))

    testImplementation(libs.testJunitJupiter)
}