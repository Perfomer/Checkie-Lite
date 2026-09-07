plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinx.serialization)
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

dependencies {
	api(libs.kotlinx.serialization.json)

	testImplementation(libs.test.junitJupiter)
	testRuntimeOnly(libs.test.junitPlatformLauncher)
}
