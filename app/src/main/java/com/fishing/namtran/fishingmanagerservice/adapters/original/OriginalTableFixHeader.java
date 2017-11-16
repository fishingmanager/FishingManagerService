package com.fishing.namtran.fishingmanagerservice.adapters.original;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.fishing.namtran.fishingmanagerservice.ManagerCustomerActivity;
import com.fishing.namtran.fishingmanagerservice.R;
import com.fishing.namtran.fishingmanagerservice.UpdateCustomerActivity;
import com.fishing.namtran.fishingmanagerservice.Utils;
import com.fishing.namtran.fishingmanagerservice.adapters.original_sortable.NexusWithImage;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Customers;
import com.fishing.namtran.fishingmanagerservice.dbconnection.FishingManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.Fishings;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishing;
import com.fishing.namtran.fishingmanagerservice.dbconnection.KeepFishingManager;
import com.fishing.namtran.fishingmanagerservice.dbconnection.LogsKeepFishing;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miguel on 12/02/2016.
 */
public class OriginalTableFixHeader {
    private Context context;
    private int totalItems;
    private int totalColumn = 10;

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
        //onLoad(adapter);
        return adapter;
    }

    private  void onLoad(final OriginalTableFixHeaderAdapter adapter)
    {
        Nexus ne = adapter.getBody().get(1);
        //Utils.Alert(context, ne.data[1]);
        //adapter.inflateBody().textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));

        TableFixHeaderAdapter.OnLoad<Nexus, OriginalCellViewGroup> load = new TableFixHeaderAdapter.OnLoad<Nexus, OriginalCellViewGroup>()
        {
            @Override
            public void onLoad(Nexus item, OriginalCellViewGroup viewGroup)
            {
                //viewGroup.textView.setTextColor(ContextCompat.getColor(context, R.color.colorBlue));
            }
        };
    }

    private void setListeners(final OriginalTableFixHeaderAdapter adapter) {
        TableFixHeaderAdapter.ClickListener<String, OriginalCellViewGroup> clickListenerHeader = new TableFixHeaderAdapter.ClickListener<String, OriginalCellViewGroup>() {
            @Override
            public void onClickItem(String s, OriginalCellViewGroup viewGroup, int row, int column) {
                Snackbar.make(viewGroup, "Click on " + s + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
            }
        };

        TableFixHeaderAdapter.ClickListener<Nexus, OriginalCellViewGroup> clickListenerBody = new TableFixHeaderAdapter.ClickListener<Nexus, OriginalCellViewGroup>() {
            @Override
            public void onClickItem(Nexus item, OriginalCellViewGroup viewGroup, int row, int column) {
                //Snackbar.make(viewGroup, "Click on " + item.data[column + 1] + " (" + row + "," + column + ")", Snackbar.LENGTH_SHORT).show();
                //viewGroup.vg_root.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));

                if(totalItems != row) {
                    final String fishingId = item.data[0];
                    final FishingManager fishings = new FishingManager(context);
                    Cursor cursors = fishings.getFishingEntryByFishingId(fishingId);

                    String dateOut = null;
                    final int status;
                    if (cursors.moveToNext()) {
                        dateOut = cursors.getString(cursors.getColumnIndexOrThrow(Fishings.Properties.DATE_OUT));
                    }

                    if (dateOut == null) {
                        if (column == 5) {
                            status = fishings.setFeedTypeStatus(fishingId);
                            viewGroup.textView.setText(status == 1 ? context.getString(R.string.yes) : context.getString(R.string.no));
                            adapter.setBody(getBody());
                        /*
                            new AlertDialog.Builder(context)
                                    .setTitle("Title")
                                    .setMessage("Do you really want to whatever?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            fishings.setFeedTypeStatus(fishingId);
                                            Intent myIntent = new Intent(context,
                                                    ManagerCustomerActivity.class);

                                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                            // Closing all the Activities
                                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            context.startActivity(myIntent);
                                        }})
                                    .setNegativeButton(android.R.string.no, null).show();*/

                        } else {
                            Utils.Redirect(context, UpdateCustomerActivity.class, "fishingId", item.data[0]);
                        }
                    }
                }
            }
        };

        TableFixHeaderAdapter.LongClickListener<Nexus, OriginalCellViewGroup> longClickListenerBody = new TableFixHeaderAdapter.LongClickListener<Nexus, OriginalCellViewGroup >() {
            @Override
            public void onLongClickItem(Nexus item, OriginalCellViewGroup viewGroup, int row, int column) {
                if (column == 6) {
                    viewGroup.vg_root.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));

                    String logsDetail = "";
                    Cursor fishings = (new FishingManager(context)).getFishingEntryByFishingId(item.data[0]);
                    if(fishings.moveToNext())
                    {
                        String custId = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.CUSTOMER_ID));
                        Cursor logsCuror = (new KeepFishingManager(context)).getLogsKeepFishing(custId);

                        while (logsCuror.moveToNext())
                        {
                            int status = logsCuror.getInt(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.STATUS));

                            if(status == 1) {
                                logsDetail = "\n*" + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.DATE_TIME)) + ": " + " - " + context.getString(R.string.buy_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.BUY_FISH))
                                        + " - " + context.getString(R.string.total_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TOTAL_FISH))
                                        + " - " + context.getString(R.string.total_money) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TOTAL_MONEY_BUY_FISH)) + "\n" + logsDetail;
                            } else {
                                logsDetail =  "\n*" + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.DATE_TIME)) + ": " + " - " + context.getString(R.string.keep_fish) + ": " +  logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.KEEP_FISH))
                                        + " - " + context.getString(R.string.take_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TAKE_FISH)) + " - " + context.getString(R.string.total_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.TOTAL_FISH))
                                        + " - " + context.getString(R.string.fee_do_fish) + ": " + logsCuror.getString(logsCuror.getColumnIndexOrThrow(LogsKeepFishing.Properties.FEE_DO_FISH)) + "\n" + logsDetail;
                            }
                        }
                        logsCuror.close();
                    }
                    fishings.close();

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage(logsDetail);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
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
        adapter.setLongClickListenerBody(longClickListenerBody);
    }

    private List<String> getHeader() {
        final String headers[] = {
                context.getString(R.string.fullname),
                context.getString(R.string.mobile),
                context.getString(R.string.date_in),
                context.getString(R.string.date_out),
                context.getString(R.string.total_hours),
                context.getString(R.string.feed_type),
                context.getString(R.string.total_fish),
                context.getString(R.string.total_money),
                context.getString(R.string.note)
        };

        return Arrays.asList(headers);
    }

    private List<Nexus> getBody() {
        List<Nexus> items = new ArrayList<>();
        //String priceFishing = null;
        //String packagePrice = null;
        //int priceFeedType = 0;
        int onlineCount = 0;
        int totalMoney = 0;
        int totalFish = 0;

        DateFormat currentDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat sqlDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date currentDate = new Date();
        String dateFishing = currentDateFormat.format(currentDate);
        items.add(new Nexus(dateFishing));

        Cursor fishings = (new FishingManager(context)).getFishingEntries(sqlDateFormat.format(currentDate));
        //Cursor settings = (new SettingsManager(context)).getSettingEntry("1");
        int totalFisher = fishings.getCount();

        /*
        while (settings.moveToNext()) {
            priceFishing = settings.getString(settings.getColumnIndexOrThrow(Settings.Properties.PRICE_FISHING));
            packagePrice = settings.getString(settings.getColumnIndexOrThrow(Settings.Properties.PACKAGE_FISHING));
            priceFeedType = settings.getInt(settings.getColumnIndexOrThrow(Settings.Properties.PRICE_FEED_TYPE));
        }
        settings.close();
        */

        while (fishings.moveToNext()) {
            String dateIn = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_IN));
            String dateOut = fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.DATE_OUT));
            int feedType = fishings.getInt(fishings.getColumnIndexOrThrow(Fishings.Properties.FEED_TYPE));
            String dateInView = "", dateOutView = "", totalHoursView = "";

            try {
                Calendar cal = Calendar.getInstance();

                //Date in & out
                cal.setTime(dateFormat.parse(dateIn));
                dateInView = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

                if(dateOut != null) {
                    cal.setTime(dateFormat.parse(dateOut));
                    dateOutView = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

                    long diff = (dateFormat.parse(dateOut).getTime() - dateFormat.parse(dateIn).getTime());
                    //long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000);
                    totalHoursView = String.format("%02d:%02d", diffHours, diffMinutes);
                }
                else onlineCount++;

            } catch (ParseException e) {
                e.printStackTrace();
            }

            totalFish += fishings.getInt(fishings.getColumnIndexOrThrow(KeepFishing.Properties.TOTAL_FISH));
            totalMoney += fishings.getInt(fishings.getColumnIndexOrThrow(Fishings.Properties.TOTAL_MONEY));

            items.add(new Nexus(
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties._ID)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.FULLNAME)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Customers.Properties.MOBILE)),
                    dateInView,
                    dateOutView,
                    totalHoursView,
                    feedType == 1 ? context.getString(R.string.yes) : context.getString(R.string.no),
                    fishings.getString(fishings.getColumnIndexOrThrow(KeepFishing.Properties.TOTAL_FISH)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.TOTAL_MONEY)),
                    fishings.getString(fishings.getColumnIndexOrThrow(Fishings.Properties.NOTE))));
        }
        items.add(new Nexus(context.getString(R.string.total_all) + ": " + onlineCount + "/" + totalFisher, "", "", "", "", "", "", totalFish + "", totalMoney + "", ""));
        totalItems = items.size() - 1;
        fishings.close();
        return items;
    }

}
