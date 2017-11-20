package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.application.Constant;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MenuAdapterModel> dataSet;

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }

    public MenuAdapter(Context mContext, ArrayList<MenuAdapterModel> dataModels) {
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

                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_main_menu, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.title = rowView.findViewById(R.id.item_grid_main_menu_iv_title);
                viewHolder.icon = rowView.findViewById(R.id.item_grid_main_menu_iv_icon);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            MenuAdapterModel dataModel = dataSet.get(position);
            viewHolder.title.setText(dataModel.title);
            Glide.with(mContext)
                    .load(Constant.Pax.PICTURE_BASE_URL + dataModel.icon + ".png")
                    .into(viewHolder.icon);
            //Bitmap bitmap = BitmapFactory.decodeResource(
            //mContext.getResources(), resource);
            //viewHolder.icon.setImageBitmap(bitmap);

            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MenuAdapter", "getView");
            return null;
        }

    }


}
