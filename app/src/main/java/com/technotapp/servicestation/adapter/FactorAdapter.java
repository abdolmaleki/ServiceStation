package com.technotapp.servicestation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;

import java.util.List;

public class FactorAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProductFactorAdapterModel> dataSet;

    private static class ViewHolder {
        TextView txtProductCode;
        TextView txtProductName;
        TextView txtProductUnitPrice;
        TextView txtProductAmount;
        TextView txtProductSumPrice;

        ViewHolder(View rowView) {
            txtProductCode = rowView.findViewById(R.id.item_list_factor_product_txtId);
            txtProductName = rowView.findViewById(R.id.item_list_factor_product_txtName);
            txtProductUnitPrice = rowView.findViewById(R.id.item_list_factor_product_txtUnitprice);
            txtProductAmount = rowView.findViewById(R.id.item_list_factor_product_amount);
            txtProductSumPrice = rowView.findViewById(R.id.item_list_factor_product_sumprice);

        }
    }

    public FactorAdapter(Context mContext, List<ProductFactorAdapterModel> dataModels) {
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
        return dataSet.get(position).getNidProduct();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        try {
            FactorAdapter.ViewHolder viewHolder;
            if (rowView == null) {
                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_list_factor_product, parent, false);
                viewHolder = new FactorAdapter.ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (FactorAdapter.ViewHolder) rowView.getTag();
            }


            ProductFactorAdapterModel dataModel = dataSet.get(position);
            viewHolder.txtProductCode.setText(dataModel.getNidProduct() + "");
            viewHolder.txtProductAmount.setText(dataModel.getAmount() + "");
            viewHolder.txtProductName.setText(dataModel.getName());
            viewHolder.txtProductUnitPrice.setText(dataModel.getUnitPrice());
            viewHolder.txtProductSumPrice.setText(String.valueOf(dataModel.getAmount() * Long.parseLong(dataModel.getUnitPrice())));


            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "FactorAdapter", "getView");
            return null;
        }
    }

}
