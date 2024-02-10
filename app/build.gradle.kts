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
    implementation(project(":feature:main:api"))
    implementation(project(":feature:main:impl"))
    implementation(project(":feature:review-creation:impl"))
    implementation(project(":feature:review-details:impl"))

    //  Common
    implementation(project(":common:ui"))
    implementation(project(":common:android"))

    //  Core
    implementation(project(":core:navigation:api"))
    implementation(project(":core:navigation:voyager"))
    implementation(project(":core:datasource:local:impl"))
}