package group.bakemate.common.pure.state

import group.bakemate.common.pure.state.Lce.Content
import group.bakemate.common.pure.state.Lce.Error
import group.bakemate.common.pure.state.Lce.Loading

val <T : Any> Lce<T>.content: T?
	get() {
		val contentState = this as? Content<T>
		return contentState?.content
	}

val <T : Any> Lce<T>.loadingContentAware: Lce<T>
	get() {
		return if (this is Content) this
		else Loading()
	}

val <T : Any> Lce<T>.isError: Boolean
	get() = this is Error

val <T : Any> Lce<T>.isLoading: Boolean
	get() = this is Loading

val <T : Any> Lce<T>.isContent: Boolean
	get() = this is Content

fun <T : Any> Lce<T>.requireContent(): T = requireNotNull(content)