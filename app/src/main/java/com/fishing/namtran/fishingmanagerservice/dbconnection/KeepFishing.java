package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.provider.BaseColumns;

/**
 * Created by nam.tran on 10/18/2017.
 */

public final class KeepFishing {
    private KeepFishing() {}

    /* Inner class that defines the table contents */
    public static class Properties implements BaseColumns {
        public static final String TABLE_NAME = "keepfishings";
        public static final String CUSTOMER_ID = "customerId";
        public static final String KEEP_HOURS = "keepHours";
        public static final String NO_KEEP_HOURS = "noKeepHours";
        public static final String KEEP_FISH = "keep_fish";
        public static final String TAKE_FISH = "take_fish";
        public static final String TOTAL_FISH = "total_fish";
        public static final String NOTE = "note";
    }
}
