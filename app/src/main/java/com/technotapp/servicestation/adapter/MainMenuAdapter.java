package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MainMenuModel;

import java.util.ArrayList;
import java.util.List;


public class MainMenuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MainMenuModel> dataSet;

    private static class ViewHolder {
        TextView title;
        ImageView icon;
        ListView items;
    }

    public MainMenuAdapter(Context mContext, ArrayList<MainMenuModel> dataModels) {
        this.dataSet = dataModels;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (rowView == null) {

            viewHolder = new ViewHolder();
            rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_main_menu, parent, false);
            viewHolder.title = (TextView) rowView.findViewById(R.id.item_grid_main_menu_iv_title);
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.item_grid_main_menu_iv_icon);
           // viewHolder.items = (ListView) rowView.findViewById(R.id.item_grid_main_menu_list_items);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        MainMenuModel dataModel = dataSet.get(position);
        viewHolder.title.setText(dataModel.title);
        int resource = dataModel.icon;
        Bitmap bitmap = BitmapFactory.decodeResource(
                mContext.getResources(), resource);
        viewHolder.icon.setImageBitmap(bitmap);
       // viewHolder.items.setAdapter(fillFakeList(mContext));

        return rowView;
    }

    private ArrayAdapter<String> fillFakeList(Context ctx) {
        List<String> items = new ArrayList<>();
        items.add("گزینه 1");
        items.add("گزینه 2");
        items.add("گزینه 3");

        return new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, items);
    }
}
