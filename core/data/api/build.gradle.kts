plugins {
	id("org.jetbrains.kotlin.jvm")
}

dependencies {
	api(project(":core:entity"))
	api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}