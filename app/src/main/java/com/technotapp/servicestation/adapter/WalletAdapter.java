package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.WalletAdapterModel;

import java.util.List;


public class WalletAdapter extends BaseAdapter {

    private Context mContext;
    private List<WalletAdapterModel> dataSet;
    WalletClickListener onWalletClickListener;

    private static class ViewHolder {
        public Button button;

    }

    public WalletAdapter(Context mContext, List<WalletAdapterModel> dataModels, WalletClickListener onWalletClickListener) {
        this.dataSet = dataModels;
        this.mContext = mContext;
        this.onWalletClickListener = onWalletClickListener;
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
            WalletAdapter.ViewHolder viewHolder;


            if (rowView == null) {

                viewHolder = new WalletAdapter.ViewHolder();

                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_list_wallet, parent, false);

                viewHolder.button = rowView.findViewById(R.id.item_list_wallet_btn_type);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (WalletAdapter.ViewHolder) rowView.getTag();
            }

            WalletAdapterModel currentItem = dataSet.get(position);
            viewHolder.button.setText(currentItem.title);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onWalletClickListener.onWalletClick(currentItem);
                }
            });

            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(mContext, e, "PaymentTypeAdapter", "getView");
            return null;
        }
    }

    public interface WalletClickListener {
        void onWalletClick(WalletAdapterModel model);
    }
}
