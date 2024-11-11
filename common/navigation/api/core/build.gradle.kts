plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinx.serialization)
}

dependencies {
	api(libs.kotlinx.serialization.json)
}