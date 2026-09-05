plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    id("checkie.android.common")
}

android {
    namespace = "com.perfomer.checkielite.feature.changelog"
    buildFeatures.compose = true

    sourceSets {
        getByName("debug") {
            assets.directories += rootProject.file("changelog").path
        }
    }
}

dependencies {
    api(projects.feature.changelog.api)
    implementation(projects.common.android)
    implementation(projects.common.navigation.api.ui)
    implementation(projects.common.pure)
    implementation(projects.common.tea.compose)
    implementation(projects.common.ui)
    implementation(projects.core.datasource.local.api)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core)
    implementation(libs.compose.markdown)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.ktor.client.android) {
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-slf4j")
    }
    implementation(platform(libs.androidx.compose.bom))

    testImplementation(libs.test.junitJupiter)
    testRuntimeOnly(libs.test.junitPlatformLauncher)
}
