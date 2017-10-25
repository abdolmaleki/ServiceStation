package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class CardServiceDepositFragment extends SubMenuFragment implements View.OnClickListener {
    TransactionDataModel transactionDataModel;
    TextView tvAmount;
    Button button;

    public static CardServiceDepositFragment newInstance() {
        CardServiceDepositFragment fragment = new CardServiceDepositFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_service_deposit, container, false);
        initView(rootView);
        submitKeypadFragment(KeypadFragment.newInstance(tvAmount), R.id.fragment_card_service_deposit_frame);
        return rootView;
    }

    private void initView(View v) {
        tvAmount = (TextView) v.findViewById(R.id.fragment_card_service_deposit_txtAmount);
        button = (Button) v.findViewById(R.id.fragment_card_service_deposit_btn);
        setTitle("واریز");
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
                    sendRequest(Constant.RequestMode.DEPOSIT);
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositFragment", "startMagCard");
        }
    }

    private void sendRequest(int mode) {

        try {

            SocketEngine socketEngine = new SocketEngine(Constant.Pax.SERVER_IP, Constant.Pax.SERVER_PORT, transactionDataModel);
            socketEngine.sendData(TransactionHelper.getPacker(getActivity(),transactionDataModel, mode, tvAmount.getText().toString()), new ISocketCallback() {
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


                    Printable printable = PrintFactory.getPrintContent(Printable.DEPOSIT);
                    PrinterHelper printerHelper = PrinterHelper.getInstance();
                    if (printable != null) {
                        printerHelper.startPrint(printable.getContent(getActivity(), "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15", "1396/08/02", dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                    }
                }
            });

        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositFragment", "sendRequest");
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
        if (id == R.id.fragment_card_service_deposit_btn) {
            startMagCard();
        }
    }


}
