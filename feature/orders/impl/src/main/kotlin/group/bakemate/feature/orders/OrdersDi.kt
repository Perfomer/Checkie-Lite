package group.bakemate.feature.orders

import group.bakemate.feature.orders.presentation.OrdersContentScreen
import group.balemate.feature.orders.navigation.OrdersScreenProvider
import org.koin.dsl.module

val ordersModule = module {
    factory { OrdersScreenProvider(::OrdersContentScreen) }
}