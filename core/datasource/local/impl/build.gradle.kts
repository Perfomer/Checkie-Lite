plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.ksp)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite.core.storage"
}

dependencies {
    api(project(":core:datasource:local:api"))
    implementation(project(":common:pure"))

    implementation(libs.koin.android)
    implementation(libs.compressor)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}