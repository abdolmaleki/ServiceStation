package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.SettingAdapterModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;


public class SettingAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SettingAdapterModel> dataSet;

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }

    public SettingAdapter(Context mContext, ArrayList<SettingAdapterModel> dataModels) {
        this.dataSet = dataModels;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.get(position).actionType;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        try {
            ViewHolder viewHolder;
            if (rowView == null) {

                viewHolder = new ViewHolder();

                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_setting, parent, false);
                viewHolder.title = rowView.findViewById(R.id.item_grid_setting_menu_iv_title);
                viewHolder.icon = rowView.findViewById(R.id.item_grid_setting_menu_iv_icon);


                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            SettingAdapterModel dataModel = dataSet.get(position);
            viewHolder.title.setText(dataModel.title);
            viewHolder.icon.setImageResource(dataModel.icon);


            return rowView;
        } catch (
                Exception e)

        {
            AppMonitor.reportBug(mContext, e, "SettingAdapter", "getView");
            return null;
        }

    }


}
