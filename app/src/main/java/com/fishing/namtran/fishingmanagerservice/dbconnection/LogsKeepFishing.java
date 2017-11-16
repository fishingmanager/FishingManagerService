package com.fishing.namtran.fishingmanagerservice.dbconnection;

import android.provider.BaseColumns;

/**
 * Created by nam.tran on 10/18/2017.
 */

public final class LogsKeepFishing {
    private LogsKeepFishing() {}

    /* Inner class that defines the table contents */
    public static class Properties implements BaseColumns {
        public static final String TABLE_NAME = "logskeepfishings";
        public static final String CUSTOMER_ID = "customerId";
        public static final String KEEP_FISH = "keep_fish";
        public static final String TAKE_FISH = "take_fish";
        public static final String TOTAL_FISH = "total_fish";
        public static final String FEE_DO_FISH = "fee_do_fish";
        public static final String DATE_TIME = "date_time";
        public static final String BUY_FISH = "buy_fish";
        public static final String TOTAL_MONEY_BUY_FISH = "total_money_buy_fish";
        public static final String STATUS = "status"; //0: take fish, 1: buy fish
        public static final String NOTE = "note";
    }
}
