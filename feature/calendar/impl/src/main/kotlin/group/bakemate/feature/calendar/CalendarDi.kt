package group.bakemate.feature.calendar

import group.bakemate.feature.calendar.navigation.CalendarScreenProvider
import group.bakemate.feature.calendar.presentation.CalendarContentScreen
import org.koin.dsl.module

val calendarModule = module {
    factory { CalendarScreenProvider(::CalendarContentScreen) }
}