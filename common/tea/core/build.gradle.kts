plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	api(libs.voyager.screenmodel)
	implementation(libs.kotlinx.coroutines)
}