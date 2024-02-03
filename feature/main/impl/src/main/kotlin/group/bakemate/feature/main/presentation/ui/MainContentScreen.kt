package group.bakemate.feature.main.presentation.ui

import androidx.compose.runtime.Composable
import group.bakemate.common.ui.tea.TeaComposable
import group.bakemate.common.ui.tea.store
import group.bakemate.core.navigation.voyager.BaseScreen
import group.bakemate.feature.calendar.navigation.CalendarScreenProvider
import group.bakemate.feature.main.presentation.tea.MainStore
import group.bakemate.feature.production.navigation.ProductionScreenProvider
import group.bakemate.feature.settings.navigation.SettingsScreenProvider
import group.balemate.feature.orders.navigation.OrdersScreenProvider

internal class MainContentScreen(
    private val ordersScreenProvider: OrdersScreenProvider,
    private val calendarScreenProvider: CalendarScreenProvider,
    private val productionScreenProvider: ProductionScreenProvider,
    private val settingsScreenProvider: SettingsScreenProvider,
) : BaseScreen() {

    @Composable
    override fun Screen() = TeaComposable(store<MainStore>()) { state ->
        MainScreen(
            state = mockUiState,
            onReviewClick = {}
        )
    }
}