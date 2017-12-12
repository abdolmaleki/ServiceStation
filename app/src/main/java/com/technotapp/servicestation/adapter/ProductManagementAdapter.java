package com.technotapp.servicestation.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ProductAdapterModel;
import com.technotapp.servicestation.enums.ProductUnitCode;
import com.technotapp.servicestation.fragment.ProductAddEditDialogFragment;

import java.util.ArrayList;


public class ProductManagementAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<ProductAdapterModel> dataSet;
    private ArrayList<ProductAdapterModel> originalDataSet;
    private ProductAddEditDialogFragment.ChangeProductsListener mChangeProductsListener;

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
            btnEdit = rowView.findViewById(R.id.item_list_product_management_btn_Edit);
        }
    }

    public ProductManagementAdapter(Context mContext, ArrayList<ProductAdapterModel> dataModels) {
        this.dataSet = dataModels;
        this.mContext = mContext;
        if (mContext instanceof ProductAddEditDialogFragment.ChangeProductsListener) {
            mChangeProductsListener = (ProductAddEditDialogFragment.ChangeProductsListener) mContext;
        }
    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ProductAdapterModel> results = new ArrayList<ProductAdapterModel>();
                if (originalDataSet == null)
                    originalDataSet = dataSet;
                if (constraint != null) {
                    if (originalDataSet != null && originalDataSet.size() > 0) {
                        for (final ProductAdapterModel adapterModel : originalDataSet) {
                            if (adapterModel.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(adapterModel);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                dataSet = (ArrayList<ProductAdapterModel>) results.values;
                notifyDataSetChanged();
            }
        };
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
        return dataSet.get(position).getId();
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
            ProductAdapterModel dataModel = dataSet.get(position);
            viewHolder.txtId.setText(dataModel.getId() + "");
            viewHolder.txtName.setText(dataModel.getName());
            viewHolder.txtUnit.setText(ProductUnitCode.valueOf(Integer.parseInt(dataModel.getUnit())));
            viewHolder.txtPrice.setText(dataModel.getPrice());

            viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mChangeProductsListener.onUpdateRequest(dataSet.get(position).getId());
                }
            });


            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductManagementAdapter", "getView");
            return null;
        }
    }

}
