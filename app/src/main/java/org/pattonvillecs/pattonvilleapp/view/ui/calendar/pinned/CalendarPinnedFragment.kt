/*
 * Copyright (C) 2017 - 2018 Mitchell Skaggs, Keturah Gadson, Ethan Holtgrieve, Nathan Skelton, Pattonville School District
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

package org.pattonvillecs.pattonvilleapp.view.ui.calendar.pinned


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import dagger.android.support.DaggerFragment
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_calendar_pinned.*
import org.pattonvillecs.pattonvilleapp.R
import org.pattonvillecs.pattonvilleapp.service.repository.calendar.CalendarRepository
import org.pattonvillecs.pattonvilleapp.view.adapter.calendar.CalendarEventFlexibleAdapter
import org.pattonvillecs.pattonvilleapp.viewmodel.calendar.pinned.CalendarPinnedFragmentViewModel
import org.pattonvillecs.pattonvilleapp.viewmodel.getViewModel
import javax.inject.Inject

/**
 * This Fragment is the third view of the calendar information. It only shows events which have been pinned.
 *
 * @author Mitchell Skaggs
 * @since 1.0.0
 */
class CalendarPinnedFragment : DaggerFragment() {

    @Inject
    lateinit var calendarRepository: CalendarRepository

    private lateinit var viewModel: CalendarPinnedFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel()
        viewModel.calendarRepository = calendarRepository
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_calendar_pinned, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        no_pinned_items_textswitcher.apply {
            setFactory { TextView(context).apply { textAlignment = TextView.TEXT_ALIGNMENT_CENTER } }
            inAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
            outAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        }

        val eventAdapter = CalendarEventFlexibleAdapter(stableIds = true, calendarRepository = calendarRepository)
        eventAdapter.setDisplayHeadersAtStartUp(true)
        eventAdapter.setStickyHeaders(true)
        event_recycler_view.layoutManager = SmoothScrollLinearLayoutManager(context!!)
        event_recycler_view.addItemDecoration(
                FlexibleItemDecoration(context!!)
                        .withDefaultDivider()
                        .withDrawDividerOnLastItem(true))
        event_recycler_view.adapter = eventAdapter

        viewModel.backgroundText.observe(this::getLifecycle) {
            no_pinned_items_textswitcher.setText(it)
        }

        viewModel.pinnedEventItems.observe(this::getLifecycle) {
            eventAdapter.updateDataSet(it)
        }
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment CalendarPinnedFragment.
         */
        @JvmStatic
        fun newInstance(): CalendarPinnedFragment = CalendarPinnedFragment()
    }
}

