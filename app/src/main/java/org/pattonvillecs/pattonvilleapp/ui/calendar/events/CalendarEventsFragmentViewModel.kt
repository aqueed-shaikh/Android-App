/*
 * Copyright (C) 2017 Mitchell Skaggs, Keturah Gadson, Ethan Holtgrieve, Nathan Skelton, Pattonville School District
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.pattonvillecs.pattonvilleapp.ui.calendar.events

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import eu.davidea.flexibleadapter.livedata.FlexibleViewModel
import org.pattonvillecs.pattonvilleapp.DataSource
import org.pattonvillecs.pattonvilleapp.model.calendar.CalendarRepository
import org.pattonvillecs.pattonvilleapp.model.calendar.event.PinnableCalendarEvent
import org.pattonvillecs.pattonvilleapp.ui.calendar.DateHeader
import org.pattonvillecs.pattonvilleapp.ui.calendar.PinnableCalendarEventItem
import org.pattonvillecs.pattonvilleapp.ui.calendar.zip
import org.threeten.bp.LocalDate

/**
 * Created by Mitchell Skaggs on 11/25/2017.
 */
class CalendarEventsFragmentViewModel : FlexibleViewModel<List<PinnableCalendarEvent>, PinnableCalendarEventItem, Set<DataSource>>() {
    lateinit var calendarRepository: CalendarRepository

    private val searchText: MutableLiveData<String> = MutableLiveData<String>().apply { value = "" }

    fun setSearchText(text: String) {
        searchText.value = text
    }

    fun getSearchTextString(): String? = searchText.value

    fun getEventsAndSearch() = searchText.zip(liveItems)

    override fun getSource(identifier: Set<DataSource>): LiveData<List<PinnableCalendarEvent>> {
        return calendarRepository.getEventsByDataSource(identifier.toList())
    }

    override fun isSourceValid(source: List<PinnableCalendarEvent>?): Boolean {
        return source != null
    }

    override fun map(source: List<PinnableCalendarEvent>): MutableList<PinnableCalendarEventItem> {
        val headerMap = mutableMapOf<LocalDate, DateHeader>()

        return source.map { PinnableCalendarEventItem(it, headerMap.getOrPut(it.calendarEvent.startDate, { DateHeader(it.calendarEvent.startDate) })) }.toMutableList()
    }

    fun getSearchText(): LiveData<String> = searchText
}