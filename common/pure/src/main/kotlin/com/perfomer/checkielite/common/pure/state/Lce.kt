package com.perfomer.checkielite.common.pure.state

sealed interface Lce<out T : Any> {

	data class Loading<T : Any>(val content: T? = null) : Lce<T>
	data class Content<T : Any>(val content: T) : Lce<T>
	data class Error(val error: Throwable? = null) : Lce<Nothing>

	companion object {
		fun initial(): Lce<Nothing> = Loading()
	}
}