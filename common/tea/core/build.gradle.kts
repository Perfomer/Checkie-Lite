plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	api(libs.decompose)
	api(libs.voyager.screenmodel)
	implementation(libs.kotlinx.coroutines)
}