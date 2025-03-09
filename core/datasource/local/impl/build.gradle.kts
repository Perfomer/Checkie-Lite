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
    api(projects.core.datasource.local.api)
    implementation(projects.common.pure)

    implementation(libs.androidx.core)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.compressor)
    implementation(libs.koin.android)
    ksp(libs.androidx.room.compiler)
}