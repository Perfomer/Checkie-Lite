plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlinx.serialization)
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

dependencies {
	implementation(projects.common.navigation.api.core)
	implementation(projects.core.entity)

	testImplementation(libs.test.junitJupiter)
	testRuntimeOnly(libs.test.junitPlatformLauncher)
}
