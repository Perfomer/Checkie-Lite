package com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.actor

import com.perfomer.checkielite.common.android.util.onCatchLog
import com.perfomer.checkielite.common.pure.util.flowBy
import com.perfomer.checkielite.common.pure.util.onCatchReturn
import com.perfomer.checkielite.common.pure.util.startWith
import com.perfomer.checkielite.common.tea.component.Actor
import com.perfomer.checkielite.core.data.datasource.CheckieLocalDataSource
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationCommand.DeleteTag
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent
import com.perfomer.checkielite.feature.reviewcreation.presentation.screen.tagcreation.tea.core.TagCreationEvent.TagDeletion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

internal class DeleteTagActor(
    private val localDataSource: CheckieLocalDataSource,
) : Actor<TagCreationCommand, TagCreationEvent> {

    override fun act(commands: Flow<TagCreationCommand>): Flow<TagCreationEvent> {
        return commands.filterIsInstance<DeleteTag>()
            .flatMapLatest(::handleCommand)
    }

    private fun handleCommand(command: DeleteTag): Flow<TagDeletion> {
        return flowBy { localDataSource.deleteTag(command.id) }
            .map { TagDeletion.Succeed }
            .onCatchLog(TAG, "Failed to delete tag")
            .onCatchReturn(TagDeletion::Failed)
            .startWith(TagDeletion.Started)
    }

    private companion object {
        private const val TAG = "DeleteTagActor"
    }
}