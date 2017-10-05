package org.pattonvillecs.pattonvilleapp.model.calendar;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by Mitchell on 10/1/2017.
 */

@Entity(tableName = "events")
public class CalendarEvent {
    @PrimaryKey
    @ColumnInfo(name = "uid", index = true, collate = ColumnInfo.BINARY)
    @NonNull
    public final String uid;

    @ColumnInfo(name = "summary")
    @NonNull
    public final String summary;

    @ColumnInfo(name = "location")
    @NonNull
    public final String location;

    @ColumnInfo(name = "start_date")
    @NonNull
    private final Date startDate;

    @ColumnInfo(name = "end_date")
    @NonNull
    private final Date endDate;

    public CalendarEvent(@NonNull String uid, @NonNull String summary, @NonNull String location, @NonNull Date startDate, @NonNull Date endDate) {
        this.uid = uid;
        this.summary = summary;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @NonNull
    public Date getStartDate() {
        return (Date) startDate.clone();
    }

    @NonNull
    public Date getEndDate() {
        return (Date) endDate.clone();
    }
}