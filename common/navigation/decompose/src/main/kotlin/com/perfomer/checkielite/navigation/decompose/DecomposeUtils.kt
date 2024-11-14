package com.perfomer.checkielite.navigation.decompose

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.perfomer.checkielite.core.navigation.Destination

internal val Value<ChildStack<Destination, *>>.actual: Destination
    get() = value.active.configuration

internal val Value<ChildSlot<Destination, *>>.actual: Destination?
    get() = value.child?.configuration

internal val Value<ChildSlot<Destination, *>>.isVisible: Boolean
    get() = actual != null