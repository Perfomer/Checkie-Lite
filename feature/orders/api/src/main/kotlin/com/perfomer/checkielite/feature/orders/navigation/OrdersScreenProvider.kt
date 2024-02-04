package com.perfomer.checkielite.feature.orders.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface OrdersScreenProvider {

    operator fun invoke(): CheckieScreen
}