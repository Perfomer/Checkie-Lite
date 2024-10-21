plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite.feature.reviewcreation"
    buildFeatures.compose = true
}

dependencies {
    api(project(":feature:review-creation:api"))
    implementation(project(":feature:gallery:api"))

    implementation(project(":core:datasource:local:api"))
    implementation(project(":core:entity"))

    implementation(project(":common:android"))
    implementation(project(":common:navigation:voyager"))
    implementation(project(":common:pure"))
    implementation(project(":common:tea:compose"))
    implementation(project(":common:ui"))

    implementation(libs.activity.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui)
    implementation(libs.drag.and.drop)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.compose.bom))
}