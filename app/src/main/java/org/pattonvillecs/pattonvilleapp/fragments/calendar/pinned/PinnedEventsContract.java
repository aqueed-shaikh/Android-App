package org.pattonvillecs.pattonvilleapp.fragments.calendar.pinned;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mitchell Skaggs on 2/23/17.
 */

public final class PinnedEventsContract {
    public static final String AUTHORITY = "org.pattonvillecs.pattonvilleapp.fragments.calendar.pinned";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + PinnedEventsTable.TABLE_NAME +
            " (" + PinnedEventsTable._ID + " INTEGER PRIMARY KEY," + PinnedEventsTable.COLUMN_NAME_UID + " INTEGER)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + PinnedEventsTable.TABLE_NAME;

    private PinnedEventsContract() {
    }

    public static class PinnedEventsTable implements BaseColumns {
        public static final String CONTENT_PATH = "events";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(PinnedEventsContract.CONTENT_URI, CONTENT_PATH);
        public static final String TABLE_NAME = "pinned_events";
        public static final String COLUMN_NAME_UID = "pinned_event_uid";

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + PinnedEventsContract.AUTHORITY + "." + CONTENT_PATH; // vnd means vendor-created (Vendor == Pattonville CS) MIME type, i.e. not http, text, audio, video, etc.
    }
}