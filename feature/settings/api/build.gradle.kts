plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.common.navigation.api.core)
    implementation(projects.core.entity)
}