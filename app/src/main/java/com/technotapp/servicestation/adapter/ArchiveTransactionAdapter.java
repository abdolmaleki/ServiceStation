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
import com.technotapp.servicestation.customView.CustomButton;

import java.util.ArrayList;

public class ArchiveTransactionAdapter extends RecyclerView.Adapter<ArchiveTransactionAdapter.ViewHolder> {

    ArrayList<ArchiveTransactionAdapterModel> dataSet;
    Context mContext;
    ArchiveTransactionListener mArchiveTransactionListener;

    public ArchiveTransactionAdapter(Context ctx, ArrayList<ArchiveTransactionAdapterModel> data, ArchiveTransactionListener archiveTransactionListener) {
        this.dataSet = data;
        mContext = ctx;
        mArchiveTransactionListener = archiveTransactionListener;

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
        if (dataSet.get(position).cardNumber != null) {
            holder.cardNumber.setText("شماره کارت: " + dataSet.get(position).cardNumber);
        } else {
            holder.cardNumber.setText("نوع کارت: " + "کیف پول");

        }
        holder.accountNumber.setText("شماره اکانت: " + dataSet.get(position).accountNumber);
        holder.time.setText(dataSet.get(position).time);
        if (dataSet.get(position).amount == 0) {
            holder.printBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.printBtn.setVisibility(View.VISIBLE);
            holder.printBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mArchiveTransactionListener.onPrintTransaction(dataSet.get(position));
                }
            });
        }


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
        CustomButton printBtn;
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
            printBtn = itemView.findViewById(R.id.item_list_archive_transaction_btn_print);
            rootLayout = itemView.findViewById(R.id.item_list_archive_transaction_panel_root);
        }
    }

    public interface ArchiveTransactionListener {
        void onPrintTransaction(ArchiveTransactionAdapterModel archiveTransactionAdapterModel);
    }
}
