package com.perfomer.checkielite.common.pure.util

inline fun <reified T : Enum<T>> T.next(): T? {
	val values = enumValues<T>()
	val currentIndex = values.indexOf(this)

	return values.getOrNull(currentIndex + 1)
}

inline fun <reified T : Enum<T>> T.previous(): T? {
	val values = enumValues<T>()
	val currentIndex = values.indexOf(this)

	return values.getOrNull(currentIndex - 1)
}