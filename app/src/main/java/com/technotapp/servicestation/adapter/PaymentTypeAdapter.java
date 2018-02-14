package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.PaymentTypeAdapterModel;

import java.util.List;


public class PaymentTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<PaymentTypeAdapterModel> dataSet;
    private PaymentTypeListener mPaymentTypeListener;

    private static class ViewHolder {
        public Button button;

    }

    public PaymentTypeAdapter(Context mContext, List<PaymentTypeAdapterModel> dataModels, PaymentTypeListener paymentTypeListener) {
        this.dataSet = dataModels;
        this.mContext = mContext;
        this.mPaymentTypeListener = paymentTypeListener;
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
            PaymentTypeAdapter.ViewHolder viewHolder;


            if (rowView == null) {

                viewHolder = new PaymentTypeAdapter.ViewHolder();

                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_list_payment_type, parent, false);

                viewHolder.button = rowView.findViewById(R.id.item_list_payment_type_btn_type);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (PaymentTypeAdapter.ViewHolder) rowView.getTag();
            }

            PaymentTypeAdapterModel currentItem = dataSet.get(position);
            viewHolder.button.setText(currentItem.title);
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPaymentTypeListener.onPaymentClick(currentItem.code);
                }
            });

            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(mContext, e, "PaymentTypeAdapter", "getView");
            return null;
        }
    }

    public interface PaymentTypeListener {
        void onPaymentClick(String code);
    }
}
