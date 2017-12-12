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
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MenuAdapterModel> dataSet;
    private boolean mIsNewUpdate;
    private Session mSession;

    private static class ViewHolder {
        TextView title;
        ImageView icon;
    }

    public MenuAdapter(Context mContext, ArrayList<MenuAdapterModel> dataModels) {
        this.dataSet = dataModels;
        this.mContext = mContext;
        mSession = Session.getInstance(mContext);
        mIsNewUpdate = mSession.IsNewMenu();
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
                if (dataSet.get(position).parentMenuID == -1) {
                    rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_main_menu, parent, false);
                    viewHolder.title = rowView.findViewById(R.id.item_grid_main_menu_iv_title);
                    viewHolder.icon = rowView.findViewById(R.id.item_grid_main_menu_iv_icon);
                } else {
                    rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_sub_menu, parent, false);
                    viewHolder.title = rowView.findViewById(R.id.item_grid_card_service_iv_title);
                    viewHolder.icon = rowView.findViewById(R.id.item_grid_card_service_iv_icon);
                }

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            MenuAdapterModel dataModel = dataSet.get(position);
            viewHolder.title.setText(dataModel.title);
            if (mIsNewUpdate) {
                Glide.with(mContext)
                        .load(Constant.Pax.PICTURE_BASE_URL + dataModel.icon + ".png")
                        .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(viewHolder.icon);
            } else {
                Glide.with(mContext)
                        .load(Constant.Pax.PICTURE_BASE_URL + dataModel.icon + ".png")
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(viewHolder.icon);
            }

            if (position == dataSet.size() - 1) {
                mSession.setIsNewMenu(false);
            }


            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MenuAdapter", "getView");
            return null;
        }

    }


}
