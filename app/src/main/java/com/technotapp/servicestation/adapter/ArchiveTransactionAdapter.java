package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ArchiveTransactionAdapterModel;

import java.util.ArrayList;

public class ArchiveTransactionAdapter extends RecyclerView.Adapter<ArchiveTransactionAdapter.ViewHolder> {

    ArrayList<ArchiveTransactionAdapterModel> dataSet;
    Context mContext;

    public ArchiveTransactionAdapter(Context ctx, ArrayList<ArchiveTransactionAdapterModel> data) {
        this.dataSet = data;
        mContext = ctx;
    }

    public void addNewTransactions(ArrayList<ArchiveTransactionAdapterModel> datas) {
        dataSet.addAll(datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_archive_transaction, parent, false);
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
        holder.amount.setText("مبلغ: " + dataSet.get(position).amount + " ریال");
        holder.transactionId.setText("شماره تراکنش: " + dataSet.get(position).transactionId);
        holder.transactionTypeTitle.setText("نوع تراکنش: " + dataSet.get(position).transactionTypeTitle);
        holder.date.setText(dataSet.get(position).date);
        holder.cardNumber.setText("شماره کارت: " + dataSet.get(position).cardNumber);
        holder.accountNumber.setText("شماره اکانت: " + dataSet.get(position).accountNumber);
        holder.time.setText(dataSet.get(position).time);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView transactionId;
        TextView transactionTypeTitle;
        TextView date;
        TextView time;
        TextView cardNumber;
        TextView accountNumber;
        RelativeLayout rootLayout;

        ViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.item_list_archive_transaction_tv_amount);
            transactionId = itemView.findViewById(R.id.item_list_archive_transaction_tv_transactionId);
            transactionTypeTitle = itemView.findViewById(R.id.item_list_archive_transaction_tv_transactionTypeTitle);
            date = itemView.findViewById(R.id.item_list_archive_transaction_tv_date);
            time = itemView.findViewById(R.id.item_list_archive_transaction_tv_time);
            cardNumber = itemView.findViewById(R.id.item_list_archive_transaction_tv_cardNumber);
            accountNumber = itemView.findViewById(R.id.item_list_archive_transaction_tv_accountNumber);
            rootLayout = itemView.findViewById(R.id.item_list_archive_transaction_panel_root);
        }
    }
}
