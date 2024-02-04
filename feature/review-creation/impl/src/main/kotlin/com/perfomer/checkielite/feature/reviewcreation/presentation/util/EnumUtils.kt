package com.perfomer.checkielite.feature.reviewcreation.presentation.util

internal inline fun <reified T : Enum<T>> T.next(): T? {
	val values = enumValues<T>()
	val currentIndex = values.indexOf(this)

	return values.getOrNull(currentIndex + 1)
}

internal inline fun <reified T : Enum<T>> T.previous(): T? {
	val values = enumValues<T>()
	val currentIndex = values.indexOf(this)

	return values.getOrNull(currentIndex - 1)
}