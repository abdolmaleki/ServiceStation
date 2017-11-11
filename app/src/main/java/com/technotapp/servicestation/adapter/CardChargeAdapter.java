package com.technotapp.servicestation.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

import java.util.ArrayList;

public class CardChargeAdapter extends RecyclerView.Adapter<CardChargeAdapter.ViewHolder> {
    ArrayList<String> data;
    byte currentOperator;
    Context mContext;
    private int currentPosition=0;

    public CardChargeAdapter(ArrayList<String> data, byte currentOperator, Context ctx) {
        this.data = data;
        this.currentOperator = currentOperator;
        mContext = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcl_fragment_charge, parent, false);
        ViewHolder vh = new ViewHolder(v);
        switch (currentOperator) {
            case Constant.Operator.HAMRAHEAVAL:
                vh.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_card_charge_hamraheaval));
                vh.view.setBackground(mContext.getResources().getDrawable(R.color.white));
                vh.type.setTextColor(mContext.getResources().getColor(R.color.white));
                vh.amount.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case Constant.Operator.RIGHTEL:
                vh.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_card_charge_rightel));
                vh.view.setBackground(mContext.getResources().getDrawable(R.color.white));
                vh.type.setTextColor(mContext.getResources().getColor(R.color.white));
                vh.amount.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
            case Constant.Operator.IRANCELL:
                vh.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_card_charge_irancell));
                vh.view.setBackground(mContext.getResources().getDrawable(R.color.item_rcl_fragment_charge_irancell_color2));
                vh.type.setTextColor(mContext.getResources().getColor(R.color.item_rcl_fragment_charge_irancell_color2));
                vh.amount.setTextColor(mContext.getResources().getColor(R.color.item_rcl_fragment_charge_irancell_color2));
                break;
            case Constant.Operator.TALIYA:
                vh.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_card_charge_taliya));
                vh.view.setBackground(mContext.getResources().getDrawable(R.color.white));
                vh.type.setTextColor(mContext.getResources().getColor(R.color.white));
                vh.amount.setTextColor(mContext.getResources().getColor(R.color.white));
                break;
        }

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
        TextView type;
        RelativeLayout layout;
        View view;
        View background;

        public ViewHolder(View itemView) {
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.item_rcl_fragment_charge_txtAmount);
            layout = (RelativeLayout) itemView.findViewById(R.id.item_rcl_fragment_charge_lyr);
            type = (TextView) itemView.findViewById(R.id.item_rcl_fragment_charge_txtType);
            view = itemView.findViewById(R.id.item_rcl_fragment_charge_view);
            background = itemView.findViewById(R.id.item_rcl_fragment_charge_background);
  }


    }

}
