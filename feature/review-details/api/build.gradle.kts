plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinx.serialization)
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

dependencies {
	implementation(projects.common.navigation.api.core)
	api(projects.core.entity)

	testImplementation(libs.decompose)
	testImplementation(libs.test.junitJupiter)
	testRuntimeOnly(libs.test.junitPlatformLauncher)
}
