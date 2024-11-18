pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven { url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven") }
    }
}

rootProject.name = "Checkie Lite"
include(":app")

include(
    ":common:android",
    ":common:navigation:api:core",
    ":common:navigation:api:ui",
    ":common:navigation:decompose",
    ":common:pure",
    ":common:tea:android",
    ":common:tea:compose",
    ":common:tea:core",
    ":common:ui",
    ":common:update:api",
    ":common:update:rustore",
)

include(
    ":feature:gallery:api",
    ":feature:gallery:impl",
    ":feature:main:api",
    ":feature:main:impl",
    ":feature:review-creation:api",
    ":feature:review-creation:impl",
    ":feature:review-details:api",
    ":feature:review-details:impl",
    ":feature:search:api",
    ":feature:search:impl",
    ":feature:settings:api",
    ":feature:settings:impl",
)

include(
    ":core:datasource:local:api",
    ":core:datasource:local:impl",
    ":core:entity",
)
