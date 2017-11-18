package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MainMenuAdapterModel;
import com.technotapp.servicestation.application.Constant;

import java.util.ArrayList;


public class MainMenuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MainMenuAdapterModel> dataSet;

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }

    public MainMenuAdapter(Context mContext, ArrayList<MainMenuAdapterModel> dataModels) {
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
        return dataSet.get(position).id;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        try {
            ViewHolder viewHolder;

            if (rowView == null) {

                viewHolder = new ViewHolder();
                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_main_menu, parent, false);
                viewHolder.title = rowView.findViewById(R.id.item_grid_main_menu_iv_title);
                viewHolder.icon = rowView.findViewById(R.id.item_grid_main_menu_iv_icon);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            MainMenuAdapterModel dataModel = dataSet.get(position);
            viewHolder.title.setText(dataModel.title);
            Glide.with(mContext)
                    .load(Constant.Pax.PICTURE_BASE_URL + dataModel.icon + ".png")
                    .into(viewHolder.icon);
            //Bitmap bitmap = BitmapFactory.decodeResource(
            //mContext.getResources(), resource);
            //viewHolder.icon.setImageBitmap(bitmap);

            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MainMenuAdapter", "getView");
            return null;
        }

    }


}
