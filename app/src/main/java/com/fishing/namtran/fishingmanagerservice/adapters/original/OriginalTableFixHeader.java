package com.fishing.namtran.fishingmanagerservice.adapters.original;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.fishing.namtran.fishingmanagerservice.R;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;
import com.fishing.namtran.fishingmanagerservice.adapters.TableFixHeaderAdapter;

import java.util.ArrayList;
import java.util.Arrays;
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

        adapter.setFirstHeader(context.getString(R.string.fullname));
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

        adapter.setClickListenerFirstHeader(clickListenerHeader);
        adapter.setClickListenerHeader(clickListenerHeader);
        adapter.setClickListenerFirstBody(clickListenerBody);
        adapter.setClickListenerBody(clickListenerBody);
        adapter.setClickListenerSection(clickListenerSection);
    }

    private List<String> getHeader() {
        final String headers[] = {
                context.getString(R.string.mobile),
                context.getString(R.string.date_in),
                context.getString(R.string.date_out),
                context.getString(R.string.feed_type),
                context.getString(R.string.keep_hours),
                context.getString(R.string.no_keep_hours),
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
        items.add(new Nexus("Ngay:"));
        items.add(new Nexus("Tran Van A", "0909686767", "20017/11/01 17:25:59", "20017/11/01 21:25:59", "2", "02", "01", "08", "06", "02", "300.000", "No"));
        items.add(new Nexus("Tran Van B", "0909686767", "20017/11/01 17:25:59", "20017/11/01 21:25:59", "2", "02", "01", "08", "06", "02", "300.000", "No"));
        items.add(new Nexus("Tran Van C", "0909686767", "20017/11/01 17:25:59", "20017/11/01 21:25:59", "2", "02", "01", "08", "06", "02", "300.000", "No"));
        items.add(new Nexus("Tran Van D", "0909686767", "20017/11/01 17:25:59", "20017/11/01 21:25:59", "2", "02", "01", "08", "06", "02", "300.000", "No"));
        items.add(new Nexus("Tran Van E", "0909686767", "20017/11/01 17:25:59", "20017/11/01 21:25:59", "2", "02", "01", "08", "06", "02", "300.000", "No"));

        return items;
    }

}
