plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	implementation(libs.voyager.navigator)
	implementation(libs.kotlinx.coroutines)
}