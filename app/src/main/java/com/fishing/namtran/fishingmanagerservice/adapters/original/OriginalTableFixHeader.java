package com.fishing.namtran.fishingmanagerservice.adapters.original;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;

import com.fishing.namtran.fishingmanagerservice.ManagerCustomerActivity;
import com.fishing.namtran.fishingmanagerservice.R;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Customers;
import com.fishing.namtran.fishingmanagerservice.dbconnection.FishingManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Fishings;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishing;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Settings;
import com.fishing.namtran.fishingmanagerservice.dbconnection.SettingsManager;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.fishing.namtran.fishingmanagerservice.adapters.TableFixHeaderAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by miguel on 12/02/2016.
 */
public class OriginalTableFixHeader {
    private Context context;

    public OriginalTableFixHeader(Context context) {
        this.context = context;
    }

    public BaseTableAdapter getInstance() {
        OriginalTableFixHeaderAdapter adapter = new OriginalTableFixHeaderAdapter(context);
        List<Nexus> body = getBody();

        adapter.setFirstHeader(context.getString(R.string.number_fishing));
        adapter.setHeader(getHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        setListeners(adapter);

        return adapter;
    }

    private void setListeners(OriginalTableFixHeaderAdapter adapter) {
        TableFixHeaderAdapter.ClickListener<String, OriginalCellViewGroup> clickListenerHeader = new TableFixHeaderAdapter.ClickListener<String, OriginalCellViewGroup>() {
            @Override
            public void onClickItem(String s, OriginalCellViewGroup viewGroup, int row, int column) {
                Snackbar.make(viewGroup, "Click on " + s + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            }
        };

        TableFixHeaderAdapter.ClickListener<Nexus, OriginalCellViewGroup> clickListenerBody = new TableFixHeaderAdapter.ClickListener<Nexus, OriginalCellViewGroup>() {
            @Override
            public void onClickItem(Nexus item, OriginalCellViewGroup viewGroup, int row, int column) {
                Snackbar.make(viewGroup, "Click on " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            }
        };

        TableFixHeaderAdapter.ClickListener<Nexus, OriginalCellViewGroup> clickListenerSection = new TableFixHeaderAdapter.ClickListener<Nexus, OriginalCellViewGroup>() {
            @Override
            public void onClickItem(Nexus item, OriginalCellViewGroup viewGroup, int row, int column) {
                Snackbar.make(viewGroup, "Click on " + item.type + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            }
        };

        //adapter.setClickListenerFirstHeader(clickListenerHeader);
        //adapter.setClickListenerHeader(clickListenerHeader);
        adapter.setClickListenerFirstBody(clickListenerBody);
        adapter.setClickListenerBody(clickListenerBody);
        //adapter.setClickListenerSection(clickListenerSection);
    }

    private List<String> getHeader() {
        final String headers[] = {
                context.getString(R.string.fullname),
                context.getString(R.string.mobile),
                context.getString(R.string.date_in),
                context.getString(R.string.date_out),
                context.getString(R.string.feed_type),
                context.getString(R.string.keep_hours),
                context.getString(R.string.no_keep_hours),
                context.getString(R.string.total_hours),
                context.getString(R.string.keep_fish),
                context.getString(R.string.take_fish),
                context.getString(R.string.total_fish),
                context.getString(R.string.total_money),
                context.getString(R.string.note)
        };

        return Arrays.asList(headers);
    }

    private List<Nexus> getBody() {
        List<Nexus> items = new ArrayList<>();
        DateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Date currentDate = new Date();
        items.add(new Nexus("Ngay: " + currentDateFormat.format(currentDate)));

        Cursor fishings = (new FishingManager(context)).getFishingEntries();
        Cursor settings = (new SettingsManager(context)).getSettingEntry("1");
        String totalHours = null;
        String totalMoney = null;
        String priceFishing = null;
        String packagePrice = null;
        int priceFeedType = 0;

        while (settings.moveToNext()) {
            priceFishing = settings.getString(settings.getColumnIndexOrThrow(Settings.Properties.PRICE_FISHING));
            packagePrice = settings.getString(settings.getColumnIndexOrThrow(Settings.Properties.PACKAGE_FISHING));
            priceFeedType = settings.getInt(settings.getColumnIndexOrThrow(Settings.Properties.PRICE_FEED_TYPE));
        }
        settings.close();

        while (fishings.moveToNext()) {
            String dateIn = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_IN));
            String dateOut = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_OUT));
            int feedType = fishings.getInt(fishings.getColumnIndexOrThrow(Fishings.Properties.FEED_TYPE));

            try {
                if(dateOut != null) {
                    long diff = (dateFormat.parse(dateOut).getTime() - dateFormat.parse(dateIn).getTime());
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000);
                    totalHours = diffHours + ":" + diffMinutes;
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFormat.parse(dateIn));
                dateIn = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            totalMoney = ((feedType == 1 ? priceFeedType : 0) + Integer.parseInt(priceFishing)) + "";

            items.add(new Nexus(
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties._ID)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.FULLNAME)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.MOBILE)),
                    dateIn,
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_OUT)),
                    feedType == 1 ? context.getString(R.string.yes) : context.getString(R.string.no),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.KEEP_HOURS)),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.NO_KEEP_HOURS)),
                    "0",
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.KEEP_FISH)),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.TAKE_FISH)),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.TOTAL_FISH)),
                    totalMoney,
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.NOTE))));
        }

        fishings.close();
        return items;
    }

}
