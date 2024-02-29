plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite"

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.5.2"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))

    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(libs.voyager.navigator)
    implementation(libs.voyager.transitions)

    implementation(project(":feature:main:api"))
    implementation(project(":feature:main:impl"))
    implementation(project(":feature:review-creation:impl"))
    implementation(project(":feature:review-details:impl"))

    implementation(project(":common:ui"))
    implementation(project(":common:android"))

    implementation(project(":core:navigation:api"))
    implementation(project(":core:navigation:voyager"))
    implementation(project(":core:datasource:local:impl"))
}