package com.perfomer.checkielite.feature.orders

import com.perfomer.checkielite.feature.orders.presentation.OrdersContentScreen
import com.perfomer.checkielite.feature.orders.navigation.OrdersScreenProvider
import org.koin.dsl.module

val ordersModule = module {
    factory { OrdersScreenProvider(::OrdersContentScreen) }
}