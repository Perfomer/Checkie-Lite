plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinx.serialization)
}

dependencies {
	implementation(libs.kotlinx.serialization.json)
	implementation(project(":common:navigation:api:core"))
	implementation(project(":core:entity"))
}