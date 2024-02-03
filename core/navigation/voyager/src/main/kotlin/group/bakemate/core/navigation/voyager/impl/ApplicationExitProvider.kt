package group.bakemate.core.navigation.voyager.impl

import group.bakemate.common.android.SingleActivityHolder

internal interface ApplicationExitProvider {

	fun exit()
}

internal class AndroidApplicationExitProvider(
	private val activityHolder: SingleActivityHolder,
) : ApplicationExitProvider {

	override fun exit() {
		activityHolder.activity.moveTaskToBack(true)
	}
}