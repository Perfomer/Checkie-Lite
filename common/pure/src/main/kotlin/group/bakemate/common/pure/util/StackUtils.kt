package group.bakemate.common.pure.util

import java.util.Stack

/**
 * Looks at the object at the top of this stack and returns it.
 * If the object at the top of this stack isn't the last also removes it from the stack.
 */
fun <T> Stack<T>.safePop(): T {
	return if (size == 1) peek()
	else pop()
}