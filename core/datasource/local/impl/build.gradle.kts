plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite.core.data.datasource"
}

dependencies {
    api(project(":core:datasource:local:api"))
    implementation(project(":common:pure"))

    implementation(libs.androidx.core)
    implementation(libs.compressor)
    implementation(libs.koin.android)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
}