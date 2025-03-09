plugins {
	alias(libs.plugins.kotlin.jvm)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

dependencies {
	api(libs.kotlinx.collections.immutable)
	api(libs.kotlinx.coroutines)

	testImplementation(libs.test.junitJupiter)
}