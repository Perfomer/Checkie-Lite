plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        create("androidCommonConvention") {
            id = "checkie.android.common"
            implementationClass = "com.perfomer.checkielite.buildlogic.AndroidCommonConventionPlugin"
            displayName = "Checkie Android Common Convention Plugin"
            description = "Configures common Android and Kotlin options across modules"
        }
    }
}

tasks.withType<ProcessResources>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
