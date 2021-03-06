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

package org.pattonvillecs.pattonvilleapp.view.ui.calendar

import android.support.v7.widget.RecyclerView
import eu.davidea.flexibleadapter.items.IFlexible
import org.pattonvillecs.pattonvilleapp.service.model.calendar.event.HasStartDate

/**
 * This interface serves to provide a concrete type of something that implements [IFlexible] and [HasStartDate]
 *
 * @author Mitchell Skaggs
 * @since 1.2.0
 */
interface IFlexibleHasStartDate<VH : RecyclerView.ViewHolder> : IFlexible<VH>, HasStartDate