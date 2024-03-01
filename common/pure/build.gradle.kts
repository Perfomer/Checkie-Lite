plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	api(libs.kotlinx.collections.immutable)
	api(libs.kotlinx.coroutines)
}