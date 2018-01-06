package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

import java.util.List;

import static com.technotapp.servicestation.Infrastructure.NetworkHelper.getWifiSignalQuality;

public class WifiAdapter extends BaseAdapter {

    private Context mContext;
    private List<ScanResult> dataSet;
    private String mCurrentSSID;

    private static class ViewHolder {
        TextView ssid;
        TextView signalState;
    }

    public WifiAdapter(Context mContext, List<ScanResult> dataModels, String currentSSID) {
        this.dataSet = dataModels;
        this.mContext = mContext;
        mCurrentSSID = currentSSID;
    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int i) {
        return dataSet.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        try {
            WifiAdapter.ViewHolder viewHolder;


            if (rowView == null) {

                viewHolder = new WifiAdapter.ViewHolder();
                if (mCurrentSSID != null && dataSet.get(position).SSID.equals(mCurrentSSID)) {
                    rowView = LayoutInflater.from(mContext).inflate(R.layout.item_list_wifi_connected, parent, false);

                } else {
                    rowView = LayoutInflater.from(mContext).inflate(R.layout.item_list_wifi, parent, false);

                }
                viewHolder.ssid = rowView.findViewById(R.id.item_list_wifi_txt_ssid);
                viewHolder.signalState = rowView.findViewById(R.id.item_list_wifi_txt_signal_state);


                rowView.setTag(viewHolder);
            } else {
                viewHolder = (WifiAdapter.ViewHolder) rowView.getTag();
            }

            ScanResult scanResult = dataSet.get(position);
            viewHolder.ssid.setText(scanResult.SSID);
            viewHolder.signalState.setText("کیفیت سیگنال: " + getWifiSignalQuality(scanResult.level));


            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(mContext,e, "WifiAdapter", "getView");
            return null;
        }
    }
}
