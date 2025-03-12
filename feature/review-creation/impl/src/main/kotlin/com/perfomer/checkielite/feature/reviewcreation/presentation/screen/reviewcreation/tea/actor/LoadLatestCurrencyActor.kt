package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.repository.CurrencyRepository
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.LoadLatestCurrency
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent.LatestCurrencyLoading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class LoadLatestCurrencyActor(
    private val currencyRepository: CurrencyRepository,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<LoadLatestCurrency>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: LoadLatestCurrency): Flow<ReviewCreationEvent> {
        return flowBy { currencyRepository.getLatestCurrency() }
            .map(LatestCurrencyLoading::Succeed)
            .onCatchLog(TAG, "Failed to load latest currency")
            .onCatchReturn(LatestCurrencyLoading::Failed)
            .startWith(LatestCurrencyLoading.Started)
    }

    private companion object {
        private const val TAG = "LoadLatestCurrencyActor"
    }
}