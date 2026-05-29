plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    id("checkie.android.common")
}

android {
    namespace = "com.perfomer.checkielite.feature.appfunctions"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.androidx.appfunctions)
    implementation(libs.androidx.appfunctions.service) // @AppFunction lives in :service in alpha09
    implementation(libs.kotlinx.coroutines)
    implementation(projects.core.datasource.local.api)
    implementation(projects.core.entity)

    ksp(libs.androidx.appfunctions.compiler)

    testImplementation(libs.test.junitJupiter)
    testRuntimeOnly(libs.test.junitPlatformLauncher)
}
