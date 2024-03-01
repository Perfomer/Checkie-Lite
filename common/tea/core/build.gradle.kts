plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	implementation(libs.kotlinx.coroutines)
	implementation(libs.voyager.navigator)
}