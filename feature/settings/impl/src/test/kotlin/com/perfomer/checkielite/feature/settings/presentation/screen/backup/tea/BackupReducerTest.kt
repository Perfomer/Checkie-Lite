package com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea

import com.perfomer.checkielite.common.ui.cui.widget.toast.ToastStyle
import com.perfomer.checkielite.common.ui.util.resource.text.Text
import com.perfomer.checkielite.core.domain.entity.backup.BackupException
import com.perfomer.checkielite.core.domain.entity.backup.BackupMode
import com.perfomer.checkielite.core.domain.entity.backup.BackupProgress
import com.perfomer.checkielite.feature.settings.R
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupCommand
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEffect.ShowToast
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupEvent
import com.perfomer.checkielite.feature.settings.presentation.screen.backup.tea.core.BackupState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class BackupReducerTest {

    @Test
    fun `export succeeds`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.EXPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Completed),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_success_export), toast.text)
        assertEquals(ToastStyle.SUCCESS, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
    }

    @Test
    fun `export cancelled`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.EXPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Cancelled),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_cancel_export), toast.text)
        assertEquals(ToastStyle.NEUTRAL, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
    }

    @Test
    fun `import cancelled`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.IMPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Cancelled),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_cancel_import), toast.text)
        assertEquals(ToastStyle.NEUTRAL, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
    }

    @Test
    fun `export fails`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.EXPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Failure(IllegalStateException())),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_failure_export), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
    }

    @Test
    fun `import fails`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.IMPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Failure(IllegalStateException())),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_failure_import), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
    }

    @Test
    fun `export out of space`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.EXPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Failure(IllegalStateException("ENOSPC (No space left on device)"))),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_failure_common_no_space), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
    }

    @Test
    fun `import out of space`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.IMPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Failure(IllegalStateException("ENOSPC (No space left on device)"))),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_failure_common_no_space), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
    }

    @Test
    fun `import needs update`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.IMPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Failure(BackupException.DatabaseVersionNotSupported(1, 2))),
        )
        val toast = update.effects.single() as ShowToast

        assertEquals(Text.resource(R.string.settings_backup_failure_import_need_update), toast.text)
        assertEquals(ToastStyle.ERROR, toast.style)
        assertEquals(BackupCommand.Await.Reason.OPEN_MAIN, (update.commands.first() as BackupCommand.Await).reason)
        assertTrue(BackupCommand.LaunchAppUpdate in update.commands)
    }

    @Test
    fun `successful import waits for restart without toast`() {
        val update = BackupReducer().reduce(
            currentState = BackupState(mode = BackupMode.IMPORT),
            event = BackupEvent.BackupProgressUpdated(BackupProgress.Completed),
        )

        assertTrue(update.effects.isEmpty())
        assertEquals(BackupCommand.Await.Reason.RESTART, (update.commands.single() as BackupCommand.Await).reason)
    }
}
