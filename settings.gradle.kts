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
    }
}

rootProject.name = "Checkie Lite"
include(":app")

//Common
include(
    ":common:android",
    ":common:pure",
    ":common:tea:core",
    ":common:tea:compose",
    ":common:ui",
)

//Feature
include(
    ":feature:main:api",
    ":feature:main:impl",
    ":feature:review-creation:api",
    ":feature:review-creation:impl",
    ":feature:review-details:api",
    ":feature:review-details:impl",
)

//Core
include(
    ":core:entity",
    ":core:navigation:api",
    ":core:navigation:voyager",
    ":core:data:api",
    ":core:data:impl",
)
