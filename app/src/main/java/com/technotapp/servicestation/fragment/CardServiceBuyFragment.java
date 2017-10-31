package com.technotapp.servicestation.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.socket.ISocketCallback;
import com.technotapp.servicestation.connection.socket.SocketEngine;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.pax.printer.PrintFactory;
import com.technotapp.servicestation.pax.printer.Printable;
import com.technotapp.servicestation.pax.printer.PrinterHelper;

public class CardServiceBuyFragment extends SubMenuFragment implements View.OnClickListener {
    TransactionDataModel transactionDataModel;
    TextView tvAmount;
    Button button;

    public static CardServiceBuyFragment newInstance() {
        CardServiceBuyFragment fragment = new CardServiceBuyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_service_buy, container, false);
        initView(rootView);
        submitKeypadFragment(KeypadFragment.newInstance(tvAmount), R.id.fragment_card_service_buy_frame);
        return rootView;
    }

    private void initView(View v) {
        tvAmount = (TextView) v.findViewById(R.id.fragment_card_service_buy_txtAmount);
        button = (Button) v.findViewById(R.id.fragment_card_service_buy_btn);
        setTitle("خرید");
        button.setOnClickListener(this);
    }


    private void startMagCard() {
        try {

            MagCard magCard = MagCard.getInstance();
            magCard.start(getActivity(), new IMagCardCallback() {
                @Override
                public void onFail() {

                }

                @Override
                public void onSuccessful(String track1, String track2, String track3) {

                    initTransactionModel();

                    if (track1 != null && !track1.equals("")) {
                        transactionDataModel.setPanNumber(track1.substring(1, 17));
                    } else if (track2 != null && !track2.equals("")) {
                        transactionDataModel.setPanNumber(track2.substring(0, 16));
                    }
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceBuyFragment", "startMagCard");
        }
    }

    private void initTransactionModel() {
        transactionDataModel = new TransactionDataModel();
        //TODO setTerminalID
        //TODO setPanNumber
        transactionDataModel.setTerminalID("23801101741");
        transactionDataModel.setPanNumber("6037997293714508");
    }

    @Override
    public void onPinEnteredSuccessfully() {
        super.onPinEnteredSuccessfully();
        TransactionHelper.sendRequest(getActivity(), Constant.RequestMode.BUY, transactionDataModel, tvAmount.getText().toString().replaceAll(",",""));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fragment_card_service_buy_btn) {
            startMagCard();
        }
    }
}
