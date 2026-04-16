package com.perfomer.checkielite.common.ui.util.resource.text

import android.content.Context
import androidx.annotation.StringRes

internal data class ResourceString(
	@StringRes val resourceId: Int,
	val args: List<Text>,
) : Text {

	override fun resolve(context: Context): CharSequence {
		return context.getString(resourceId, *getArgValues(context, args))
	}
}