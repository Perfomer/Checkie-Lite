plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	api(libs.decompose)
	implementation(libs.kotlinx.coroutines)
	implementation(projects.common.pure)
}