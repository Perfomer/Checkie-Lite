plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinx.serialization)
}

dependencies {
	implementation(project(":common:navigation:api"))
	implementation(project(":common:navigation:api:core"))
}