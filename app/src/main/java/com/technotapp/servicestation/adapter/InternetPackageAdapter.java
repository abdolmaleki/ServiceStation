package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.InternetPackageModel;
import com.technotapp.servicestation.fragment.InternetPackageListDialogFragment;

import java.util.ArrayList;


public class InternetPackageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<InternetPackageModel> dataSet;
    private InternetPackageListDialogFragment.InternetPackageListener mInternetPackageListener;

    private static class ViewHolder {
        TextView txtServiceName;
        TextView txtServicePrice;
        TextView txtServiceExpireTime;
        Button btnSelect;

        ViewHolder(View rowView) {
            txtServiceName = rowView.findViewById(R.id.item_list_internet_package_balance);
            txtServicePrice = rowView.findViewById(R.id.item_list_internet_package_amount);
            txtServiceExpireTime = rowView.findViewById(R.id.item_list_internet_package_expire);
            btnSelect = rowView.findViewById(R.id.item_list_internet_package_btn_submit);

        }
    }

    public InternetPackageAdapter(Context mContext, ArrayList<InternetPackageModel> dataModels, InternetPackageListDialogFragment.InternetPackageListener internetPackageListener) {
        this.dataSet = dataModels;
        this.mContext = mContext;
        this.mInternetPackageListener = internetPackageListener;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
        return dataSet.get(position).serviceID;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        try {
            ViewHolder viewHolder;
            if (rowView == null) {
                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_list_internet_package, parent, false);
                viewHolder = new ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }
            InternetPackageModel dataModel = dataSet.get(position);
            viewHolder.txtServiceName.setText(dataModel.serviceName);
            viewHolder.txtServiceExpireTime.setText(dataModel.profileName);
            viewHolder.txtServicePrice.setText("" + dataModel.servicePrice + " ریال");

            viewHolder.btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mInternetPackageListener.onPackageSelected(dataSet.get(position));
                }
            });


            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(mContext, e, "InternetPackageAdapter", "getView");
            return null;
        }
    }


}
