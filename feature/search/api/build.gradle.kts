plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinx.serialization)
}

dependencies {
	implementation(projects.common.navigation.api.core)
	implementation(projects.core.entity)
}