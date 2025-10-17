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
    namespace = "com.perfomer.checkielite.core.theme"
}

dependencies {
    api(projects.core.datasource.local.api)
    implementation(projects.core.theme.api)
    implementation(libs.koin.android)
}