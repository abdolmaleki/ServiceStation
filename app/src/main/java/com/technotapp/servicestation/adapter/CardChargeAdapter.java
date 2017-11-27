package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

import java.util.ArrayList;

public class CardChargeAdapter extends RecyclerView.Adapter<CardChargeAdapter.ViewHolder> {
    ArrayList<String> data;
    Context mContext;

    public CardChargeAdapter(Context ctx,ArrayList<String> data) {
        this.data = data;
        mContext = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcl_fragment_charge, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.amount.setText(data.get(position));

    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount;

        public ViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.item_rcl_fragment_charge_txtAmount);
        }


    }

}
