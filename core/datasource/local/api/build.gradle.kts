plugins {
	id("org.jetbrains.kotlin.jvm")
}

dependencies {
	api(project(":core:entity"))
	api(libs.kotlinx.coroutines)
}