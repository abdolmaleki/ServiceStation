package com.technotapp.servicestation.adapter.DataModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

import java.util.ArrayList;


public class ProductManagementAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ProductManagementAdapterModel> dataSet;

    private static class ViewHolder {
        TextView txtId;
        TextView txtName;
        TextView txtUnit;
        TextView txtPrice;
        Button btnEdit;
        ViewHolder(View rowView) {
            txtId = rowView.findViewById(R.id.item_list_product_management_txtId);
            txtName = rowView.findViewById(R.id.item_list_product_management_txtName);
            txtUnit = rowView.findViewById(R.id.item_list_product_management_txtUnit);
            txtPrice = rowView.findViewById(R.id.item_list_product_management_txtPrice);
            btnEdit = rowView.findViewById(R.id.item_list_product_management_btnEdit);
        }
    }

    public ProductManagementAdapter(Context mContext, ArrayList<ProductManagementAdapterModel> dataModels) {
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
        return position;
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        try {
            ViewHolder viewHolder;
            if (rowView == null) {
                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_list_product_management, parent, false);
                viewHolder = new ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }
            ProductManagementAdapterModel dataModel = dataSet.get(position);
            viewHolder.txtId.setText(dataModel.getId());
            viewHolder.txtName.setText(dataModel.getName());
            viewHolder.txtUnit.setText(dataModel.getUnit());
            viewHolder.txtPrice.setText(dataModel.getPrice());
            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductManagementAdapter", "getView");
            return null;
        }
    }

}
