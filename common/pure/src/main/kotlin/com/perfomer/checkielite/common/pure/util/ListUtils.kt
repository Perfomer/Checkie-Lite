package com.perfomer.checkielite.common.pure.util

fun <T> List<T>.remove(element: T): List<T> {
	return toMutableList().apply { remove(element) }
}

fun <T> List<T>.replace(index: Int, valueProvider: (T) -> T): List<T> {
	return toMutableList().also { list ->
		list[index] = valueProvider(list[index])
	}
}