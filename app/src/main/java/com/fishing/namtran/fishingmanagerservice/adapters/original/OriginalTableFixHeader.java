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
        String feedType = null;
        String totalHours = null;

        while (settings.moveToNext()) {
            feedType = settings.getString(settings.getColumnIndexOrThrow(Settings.Properties.PRICE_FEED_TYPE));
        }
        settings.close();

        while (fishings.moveToNext()) {
            String dateIn = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_IN));
            String dateOut = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_OUT));

            try {
                long time = (dateFormat.parse(dateOut).getTime() - dateFormat.parse(dateIn).getTime());
                Date restDate = new Date(time);
                totalHours = dateFormat.format(restDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            items.add(new Nexus(
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties._ID)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.FULLNAME)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.MOBILE)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_IN)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_OUT)),
                    feedType,
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.KEEP_HOURS)),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.NO_KEEP_HOURS)),
                    totalHours,
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.KEEP_FISH)),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.TAKE_FISH)),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.TOTAL_FISH)),
                    "300.000",
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.NOTE))));
        }
        fishings.close();
        return items;
    }

}
