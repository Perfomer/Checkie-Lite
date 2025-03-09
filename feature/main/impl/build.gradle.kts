plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite.feature.main"
    buildFeatures.compose = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    api(project(":feature:main:api"))
    implementation(project(":feature:review-creation:api"))
    implementation(project(":feature:review-details:api"))
    implementation(project(":feature:search:api"))
    implementation(project(":feature:settings:api"))

    implementation(project(":core:datasource:local:api"))
    implementation(project(":core:entity"))

    implementation(project(":common:android"))
    implementation(project(":common:navigation:api:ui"))
    implementation(project(":common:pure"))
    implementation(project(":common:tea:compose"))
    implementation(project(":common:ui"))

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.koin.core)
    implementation(platform(libs.androidx.compose.bom))

    testImplementation(libs.test.junitJupiter)
}