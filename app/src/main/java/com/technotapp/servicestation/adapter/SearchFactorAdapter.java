package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.SearchFactorAdapterModel;

import java.util.ArrayList;

public class SearchFactorAdapter extends RecyclerView.Adapter<SearchFactorAdapter.ViewHolder> {

    ArrayList<SearchFactorAdapterModel> dataSet;
    Context mContext;

    public SearchFactorAdapter(Context ctx, ArrayList<SearchFactorAdapterModel> data) {
        this.dataSet = data;
        mContext = ctx;
    }

    public void addNewFactor(ArrayList<SearchFactorAdapterModel> datas) {
        dataSet.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_search_factor, parent, false);
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 != 0) {
            holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray_very_light));

        } else {
            holder.rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.sormeyi_very_light));
        }
        if (dataSet.get(position).discountPrice > 0) {
            holder.totalPrice.setVisibility(View.VISIBLE);
            holder.discountPrice.setVisibility(View.VISIBLE);
            holder.totalPrice.setText("مبلغ کل: " + dataSet.get(position).totalPrice + " ریال");
            holder.discountPrice.setText("تخفیف: " + dataSet.get(position).discountPrice + " ریال");
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 5, 7, 0);
            holder.finalPrice.setLayoutParams(params);
        }
        holder.factorId.setText("شماره فاکتور: " + dataSet.get(position).factorId);
        holder.terminalCode.setText("سریال ترمینال: " + dataSet.get(position).terminalCode);
        holder.finalPrice.setText("مبلغ قابل پرداخت: " + dataSet.get(position).finalPrice + " ریال");
        holder.date.setText(dataSet.get(position).date);

        holder.statusTitle.setText("وضعیت: " + dataSet.get(position).statusTitle);
        holder.time.setText(dataSet.get(position).time);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView factorId;
        TextView terminalCode;
        TextView date;
        TextView time;
        TextView finalPrice;
        TextView totalPrice;
        TextView discountPrice;
        TextView statusTitle;
        RelativeLayout rootLayout;

        ViewHolder(View itemView) {
            super(itemView);
            factorId = itemView.findViewById(R.id.item_list_search_factor_tv_factor_id);
            terminalCode = itemView.findViewById(R.id.item_list_search_factor_tv_terminalCode);
            finalPrice = itemView.findViewById(R.id.item_list_search_factor_tv_finalPrice);
            date = itemView.findViewById(R.id.item_list_search_factor_tv_date);
            time = itemView.findViewById(R.id.item_list_search_factor_tv_time);
            totalPrice = itemView.findViewById(R.id.item_list_search_factor_tv_totalPrice);
            discountPrice = itemView.findViewById(R.id.item_list_search_factor_tv_discountPrice);
            statusTitle = itemView.findViewById(R.id.item_list_search_factor_tv_statusTitle);
            rootLayout = itemView.findViewById(R.id.item_list_search_factor_panel_root);
        }
    }
}
