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
import com.technotapp.servicestation.adapter.DataModel.SubMenuModel;

import java.util.ArrayList;
import java.util.List;


public class SubMenuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SubMenuModel> dataSet;

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }

    public SubMenuAdapter(Context mContext, ArrayList<SubMenuModel> dataModels) {
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
            rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_card_service, parent, false);
            viewHolder.title = (TextView) rowView.findViewById(R.id.item_grid_card_service_iv_title);
            viewHolder.icon = (ImageView) rowView.findViewById(R.id.item_grid_card_service_iv_icon);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        SubMenuModel dataModel = dataSet.get(position);
        viewHolder.title.setText(dataModel.title);
        int resource = dataModel.icon;
        Bitmap bitmap = BitmapFactory.decodeResource(
                mContext.getResources(), resource);
        viewHolder.icon.setImageBitmap(bitmap);
        return rowView;
    }
}
