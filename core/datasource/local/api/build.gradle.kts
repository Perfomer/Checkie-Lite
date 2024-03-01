plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	api(project(":core:entity"))
	api(libs.kotlinx.coroutines)
}