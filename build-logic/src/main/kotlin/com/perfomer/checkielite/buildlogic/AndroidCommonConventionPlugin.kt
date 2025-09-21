package com.perfomer.checkielite.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidCommonConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.findByType(ApplicationExtension::class.java)?.let { ext ->
                ext.configureCommonAndroid()
            }

            extensions.findByType(LibraryExtension::class.java)?.let { ext ->
                ext.configureCommonAndroid()
            }

            extensions.findByType(KotlinAndroidProjectExtension::class.java)?.apply {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-Xcontext-parameters",
                    )
                }
            }
        }
    }

    private fun ApplicationExtension.configureCommonAndroid() {
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
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    private fun LibraryExtension.configureCommonAndroid() {
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
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }
}
