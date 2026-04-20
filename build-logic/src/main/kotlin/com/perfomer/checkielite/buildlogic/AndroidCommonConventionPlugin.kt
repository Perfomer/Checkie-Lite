package com.perfomer.checkielite.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidCommonConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val javaVersion = libs.getVersion("java").toInt()

            extensions.findByType<ApplicationExtension>()
                ?.configureCommonAndroid(javaVersion)

            extensions.findByType<LibraryExtension>()
                ?.configureCommonAndroid(javaVersion)

            extensions.findByType<KotlinAndroidProjectExtension>()?.apply {
                compilerOptions {
                    jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-Xcontext-parameters",
                        "-XXLanguage:+PropertyParamAnnotationDefaultTargetMode"
                    )
                }
            }
        }
    }

    private fun ApplicationExtension.configureCommonAndroid(javaVersion: Int) {
        compileSdk = 36

        defaultConfig {
            targetSdk = 36
            minSdk = 24
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
            resourceConfigurations.addAll(listOf("en", "ru"))
        }

        packaging.resources.excludes.add("META-INF/*.kotlin_module")

        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(javaVersion)
            targetCompatibility = JavaVersion.toVersion(javaVersion)
        }
    }

    private fun LibraryExtension.configureCommonAndroid(javaVersion: Int) {
        compileSdk = 36

        defaultConfig {
            minSdk = 24
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            vectorDrawables.useSupportLibrary = true
            resourceConfigurations.addAll(listOf("en", "ru"))
        }

        packaging.resources.excludes.add("META-INF/*.kotlin_module")

        compileOptions {
            sourceCompatibility = JavaVersion.toVersion(javaVersion)
            targetCompatibility = JavaVersion.toVersion(javaVersion)
        }
    }
}
