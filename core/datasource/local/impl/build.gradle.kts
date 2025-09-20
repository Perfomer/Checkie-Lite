plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    id("checkie.android.common")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.perfomer.checkielite.core.data.datasource"
}

dependencies {
    api(projects.core.datasource.local.api)
    implementation(projects.common.pure)

    implementation(libs.androidx.core)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.compressor)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.serialization.json)
    ksp(libs.androidx.room.compiler)
}