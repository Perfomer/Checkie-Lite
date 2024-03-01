plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	implementation(project(":common:navigation:api"))
}