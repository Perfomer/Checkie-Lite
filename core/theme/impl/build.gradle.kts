plugins {
    alias(libs.plugins.android.library)
    id("checkie.android.common")
}

android {
    namespace = "com.perfomer.checkielite.core.theme"
}

dependencies {
    api(projects.core.datasource.local.api)
    implementation(projects.core.theme.api)
    implementation(libs.koin.android)

    testImplementation(libs.test.junitJupiter)
    testImplementation(libs.test.kotlinx.coroutines)
    testRuntimeOnly(libs.test.junitPlatformLauncher)
}
