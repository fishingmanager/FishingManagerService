package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.provider.BaseColumns;

/**
 * Created by nam.tran on 10/18/2017.
 */

public final class Fishing {
    private Fishing() {}

    /* Inner class that defines the table contents */
    public static class Properties implements BaseColumns {
        public static final String TABLE_NAME = "fishing";
        public static final String CUSTOMER_ID = "customerId";
        public static final String DATE_IN = "dateIn";
        public static final String DATE_OUT = "dateOut";
        public static final String FEED_TYPE = "feedType";
        public static final String KEEP_HOURS = "keepHours";
        public static final String NO_KEEP_HOURS = "noKeepHours";
        public static final String KEEP_FISH = "keep_fish";
        public static final String TAKE_FISH = "take_fish";
        public static final String TOTAL_FISH = "total_fish";
        public static final String TOTAL_MONEY = "total_money";
        public static final String NOTE = "note";
    }
}
