package com.perfomer.checkielite.common.pure.util

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.util.Collections

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

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return if (this is ArrayList<T>) this
    else ArrayList<T>().apply { addAll(this@toArrayList) }
}

fun <T> List<T>.swap(from: Int, to: Int): List<T> {
    val resultList = this.toMutableList()
    Collections.swap(resultList, from, to)
    return resultList
}

fun <T> List<T>.move(item: T, toPosition: Int): List<T> {
    if (!contains(item)) return this

    return toMutableList().apply {
        remove(item)
        add(toPosition, item)
    }
}