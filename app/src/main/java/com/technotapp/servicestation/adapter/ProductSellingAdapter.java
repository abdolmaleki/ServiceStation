package com.technotapp.servicestation.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Converters;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.enums.ProductUnitCode;

import java.util.ArrayList;

public class ProductSellingAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<ProductFactorAdapterModel> dataSet;
    private ArrayList<ProductFactorAdapterModel> originalDataSet;
    private OnFactorChangeListener onFactorChangeListener;

    private static class ViewHolder {
        TextView txtName;
        TextView txtamount;
        TextView txtPrice;
        ImageButton btnPlus;
        ImageButton btnMinus;

        ViewHolder(View rowView) {
            txtName = rowView.findViewById(R.id.item_grid_custom_service_txtTitle);
            txtamount = rowView.findViewById(R.id.item_grid_custom_service_txtCount);
            txtPrice = rowView.findViewById(R.id.item_grid_custom_service_txt_price);
            btnPlus = rowView.findViewById(R.id.item_grid_custom_service_btn_plus);
            btnMinus = rowView.findViewById(R.id.item_grid_custom_service_btn_minus);
        }
    }

    public ProductSellingAdapter(Context mContext, ArrayList<ProductFactorAdapterModel> dataModels, OnFactorChangeListener onFactorChangeListener) {
        this.dataSet = dataModels;
        this.mContext = mContext;
        this.onFactorChangeListener = onFactorChangeListener;
    }


    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ProductFactorAdapterModel> results = new ArrayList<ProductFactorAdapterModel>();
                if (originalDataSet == null)
                    originalDataSet = dataSet;
                if (constraint != null) {
                    if (originalDataSet != null && originalDataSet.size() > 0) {
                        for (final ProductFactorAdapterModel adapterModel : originalDataSet) {
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
                dataSet = (ArrayList<ProductFactorAdapterModel>) results.values;
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
        return dataSet.get(position).getNidProduct();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        try {
            ProductSellingAdapter.ViewHolder viewHolder;
            if (rowView == null) {
                rowView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_custom_service, parent, false);
                viewHolder = new ProductSellingAdapter.ViewHolder(rowView);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ProductSellingAdapter.ViewHolder) rowView.getTag();
            }


            ProductFactorAdapterModel dataModel = dataSet.get(position);
            viewHolder.txtName.setText(dataModel.getName());
            viewHolder.txtamount.setText("0");
            viewHolder.txtPrice.setText(Converters.toPersianPrice(dataModel.getUnitPrice()) + " ریال");
            viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    viewHolder.txtamount.setText((++dataModel.amount) + " " + ProductUnitCode.valueOf(Integer.parseInt(dataModel.getUnit())));
                    onFactorChangeListener.onAddNewProduct(dataModel);
                }
            });
            viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dataModel.getAmount() > 0) {
                        if (dataModel.getAmount() == 1) {
                            viewHolder.txtamount.setText((--dataModel.amount) + "");

                        } else {
                            viewHolder.txtamount.setText((--dataModel.amount) + " " + ProductUnitCode.valueOf(Integer.parseInt(dataModel.getUnit())));

                        }
                        onFactorChangeListener.onAddNewProduct(dataModel);
                    }
                }
            });
            return rowView;
        } catch (Exception e) {
            AppMonitor.reportBug(mContext, e, "ProductSellingAdapter", "getView");
            return null;
        }
    }

    public interface OnFactorChangeListener {
        void onAddNewProduct(ProductFactorAdapterModel model);
    }


}
