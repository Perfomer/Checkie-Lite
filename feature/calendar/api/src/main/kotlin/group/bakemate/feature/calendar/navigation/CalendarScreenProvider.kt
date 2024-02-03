package group.bakemate.feature.calendar.navigation

import com.perfomer.checkielite.core.navigation.api.CheckieScreen

fun interface CalendarScreenProvider {

    operator fun invoke(): CheckieScreen
}