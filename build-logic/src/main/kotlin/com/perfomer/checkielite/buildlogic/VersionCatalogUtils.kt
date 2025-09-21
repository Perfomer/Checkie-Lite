package com.perfomer.checkielite.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

internal val Project.libs: VersionCatalog
    get() = getVersionCatalog("libs")

internal fun VersionCatalog.getVersion(alias: String): String {
    return findVersion(alias).get().requiredVersion
}

private fun Project.getVersionCatalog(name: String): VersionCatalog {
    return extensions.getByType(VersionCatalogsExtension::class.java).named(name)
}