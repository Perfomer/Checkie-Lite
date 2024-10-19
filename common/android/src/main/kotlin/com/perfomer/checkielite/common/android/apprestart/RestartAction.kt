package com.perfomer.checkielite.common.android.apprestart

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface RestartAction : Parcelable {

    @Parcelize
    data object ShowSuccessBackupImportToast : RestartAction
}