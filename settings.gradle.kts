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
    ":feature:splash:api",
    ":feature:splash:impl",
    ":feature:welcome:api",
    ":feature:welcome:impl",
    ":feature:registration:api",
    ":feature:registration:impl",
    ":feature:main:api",
    ":feature:main:impl",
    ":feature:calendar:api",
    ":feature:calendar:impl",
    ":feature:orders:api",
    ":feature:orders:impl",
    ":feature:production:api",
    ":feature:production:impl",
    ":feature:settings:api",
    ":feature:settings:impl",
)

//Core
include(
    ":core:entity",
    ":core:navigation:api",
    ":core:navigation:voyager",
    ":core:local:api",
    ":core:local:preferences",
    ":core:storage:api",
    ":core:storage:impl",
)
