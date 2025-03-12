plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite.feature.reviewcreation"
    buildFeatures.compose = true
}

dependencies {
    api(projects.feature.reviewCreation.api)
    implementation(projects.feature.gallery.api)
    implementation(projects.feature.gallery.api)
    implementation(projects.core.datasource.local.api)
    implementation(projects.core.entity)
    implementation(projects.common.android)
    implementation(projects.common.navigation.api.ui)
    implementation(projects.common.pure)
    implementation(projects.common.tea.compose)
    implementation(projects.common.ui)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.dragAndDrop)
    implementation(libs.koin.core)
    implementation(platform(libs.androidx.compose.bom))
}