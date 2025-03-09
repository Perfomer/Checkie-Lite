plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	api(projects.core.entity)

	api(libs.kotlinx.coroutines)
}