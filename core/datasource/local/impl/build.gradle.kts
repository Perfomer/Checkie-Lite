plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

applyCommonAndroid()

android {
    namespace = "com.perfomer.checkielite.core.storage"
}

dependencies {
    api(project(":core:datasource:local:api"))
    implementation(project(":common:pure"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.insert-koin:koin-android:3.5.0")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("id.zelory:compressor:3.0.1")
}