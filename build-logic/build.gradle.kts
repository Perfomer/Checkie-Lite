plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:9.1.1")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.20")
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
