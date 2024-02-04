package com.perfomer.checkielite.common.pure.util

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

private val emptyPersistentList = persistentListOf<Any>()

@Suppress("UNCHECKED_CAST")
fun <T> emptyPersistentList(): PersistentList<T> {
    return emptyPersistentList as PersistentList<T>
}

fun <T> List<T>.remove(element: T): List<T> {
    return toMutableList().apply { remove(element) }
}

fun <T> List<T>.replace(index: Int, valueProvider: (T) -> T): List<T> {
    return toMutableList().also { list ->
        list[index] = valueProvider(list[index])
    }
}