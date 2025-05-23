package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.ignoreResult
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationCommand.WarmUpCurrencies
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.reviewcreation.tea.core.ReviewCreationEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest

internal class WarmUpCurrenciesActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<ReviewCreationCommand, ReviewCreationEvent> {

    override fun act(commands: Flow<ReviewCreationCommand>): Flow<ReviewCreationEvent> {
        return commands.filterIsInstance<WarmUpCurrencies>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: WarmUpCurrencies): Flow<ReviewCreationEvent> {
        return flowBy { localDataSource.getAllCurrenciesCodes() }
            .onCatchLog(TAG, "Failed to warm up currencies", rethrow = false)
            .ignoreResult()
    }

    private companion object {
        private const val TAG = "WarmUpCurrenciesActor"
    }
}