package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.technotapp.servicestation.R;

/**
 * Created by Kourosh on 10/16/17.
 */

public class MainMenuAdapter extends BaseAdapter {
    Context mContext;

    public MainMenuAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 9;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_main_menu,parent,false);
        return rowView;
    }
}
