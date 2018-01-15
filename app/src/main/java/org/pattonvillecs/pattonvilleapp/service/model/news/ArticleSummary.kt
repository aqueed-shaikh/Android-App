/*
 * Copyright (C) 2018 Mitchell Skaggs, Keturah Gadson, Ethan Holtgrieve, Nathan Skelton, Pattonville School District
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

package org.pattonvillecs.pattonvilleapp.service.model.news

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import com.github.magneticflux.rss.namespaces.standard.elements.Item
import com.google.errorprone.annotations.Immutable
import org.threeten.bp.Instant

/**
 * Created by Mitchell Skaggs on 1/15/2018.
 *
 * @since 1.4.0
 */
@Immutable
@Entity(tableName = "news_articles", primaryKeys = ["guid"])
data class ArticleSummary(
        @ColumnInfo(name = "title")
        override val title: String,
        @ColumnInfo(name = "link")
        override val link: String,
        @ColumnInfo(name = "pub_date", index = true, collate = ColumnInfo.BINARY)
        override val pubDate: Instant,
        @field:ColumnInfo(name = "guid", index = true, collate = ColumnInfo.BINARY)
        override val guid: String
) : IArticleSummary {
    constructor(item: Item) : this(item.title!!, item.link!!.toString(), item.pubDate!!.toInstant(), item.guid!!.text)
}