package com.technotapp.servicestation.fragment;

import android.support.annotation.Nullable;
import android.os.Bundle;
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

public class CardServiceBalanceFragment extends SubMenuFragment implements View.OnClickListener {
    TransactionDataModel transactionDataModel;
    TextView textView;
    Button button;

    public static CardServiceBalanceFragment newInstance() {
        CardServiceBalanceFragment fragment = new CardServiceBalanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_service_balance, container, false);
        initView(rootView);

        return rootView;
    }

    private void initView(View v) {
        textView = (TextView) v.findViewById(R.id.test_text);
        button = (Button) v.findViewById(R.id.fragment_card_service_balance_testBtn);
        setTitle(getString(R.string.CardServiceBalanceFragment_title));
        button.setOnClickListener(this);
    }

    private void sendRequest(int mode) {

        try {

            SocketEngine socketEngine = new SocketEngine(Constant.Pax.SERVER_IP, Constant.Pax.SERVER_PORT, transactionDataModel);
            socketEngine.sendData(TransactionHelper.getPacker(getActivity(),transactionDataModel, mode, "0"), new ISocketCallback() {
                @Override
                public void onFail() {

                }

                @Override
                public void onReceiveData(TransactionDataModel dataModel) {
                    AppMonitor.Log(dataModel.getPanNumber());

                    //TODO set parameters to tvAmount
//                    Log.i("aa -------->", dataModel.getPanNumber() + "\n" +
//                            dataModel.getBackTransactionID() + "\n" +
//                            dataModel.getAmount() + "\n" +
//                            dataModel.getDateTimeShaparak() + "\n" +
//                            dataModel.getTerminalID() + "\n" +
//                            dataModel.getResponseCode() + "\n" +
//                            dataModel.getMAC() + "\n");

                    Printable printable = PrintFactory.getPrintContent(Printable.BALANCE);
                    PrinterHelper printerHelper = PrinterHelper.getInstance();
                    if (printable != null) {
                        printerHelper.startPrint(printable.getContent(getActivity(), "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15","1396/08/02", dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                    }
                }
            });

        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceBalanceFragment", "sendRequest");
        }
    }

    @Override
    public void onPinEnteredSuccessfully() {
        super.onPinEnteredSuccessfully();
        sendRequest(Constant.RequestMode.BALANCE);


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
            AppMonitor.reportBug(e, "CardServiceBalanceFragment", "startMagCard");
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fragment_card_service_balance_testBtn) {
            startMagCard();
        }
    }
}
