plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	api(libs.kotlinx.coroutines)
	api(libs.kotlinx.collections.immutable)
}