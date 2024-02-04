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
    ":common:tea",
    ":common:ui",
)

//Feature
include(
    ":feature:main:api",
    ":feature:main:impl",
    ":feature:orders:api",
    ":feature:orders:impl",
    ":feature:production:api",
    ":feature:production:impl",
)

//Core
include(
    ":core:entity",
    ":core:navigation:api",
    ":core:navigation:voyager",
    ":core:storage:api",
    ":core:storage:impl",
)
