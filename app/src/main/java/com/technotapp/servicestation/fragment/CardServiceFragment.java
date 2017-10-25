package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.SubMenuModel;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.adapter.SubMenuAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.socket.ISocketCallback;
import com.technotapp.servicestation.connection.socket.SocketEngine;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.pax.printer.PrintFactory;
import com.technotapp.servicestation.pax.printer.Printable;
import com.technotapp.servicestation.pax.printer.PrinterHelper;

import java.util.ArrayList;

public class CardServiceFragment extends SubMenuFragment implements AdapterView.OnItemClickListener {

    GridView gridView;

    public static CardServiceFragment newInstance() {
        CardServiceFragment fragment = new CardServiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_card_services, container, false);
        gridView = (GridView) rooView.findViewById(R.id.fragment_card_services_grdList);

        initView();
        initAdapter();

        return rooView;
    }

    private void initView() {
        setRetainInstance(true);
        setTitle(getString(R.string.CardServiceFragment_title));
        gridView.setOnItemClickListener(this);
    }

    private void initAdapter() {
        ArrayList<SubMenuModel> subMenuModels = new ArrayList<>();

        subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_cardToCard), R.drawable.ic_card_to_card));
        subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_Deposit), R.drawable.ic_deposit));
        subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_Buy), R.drawable.ic_buy_card));
        subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_Balance), R.drawable.ic_balance));

        SubMenuAdapter menuAdapter = new SubMenuAdapter(mActivity, subMenuModels);
        gridView.setAdapter(menuAdapter);
    }

    private void loadData() {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            // TODO ad card to card
//            case 0:
//                startActivity();
//                break;
            case 1:
                submitFragment(CardServiceDepositFragment.newInstance());
                break;
            case 2:
                submitFragment(CardServiceBuyFragment.newInstance());
                break;
            case 3:
//                submitFragment(CardServiceBalanceFragment.newInstance());
                balanceCard();
                break;

        }
    }

    TransactionDataModel transactionDataModel;

    private void balanceCard() {

        startMagCard();

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
                    sendRequest(Constant.RequestMode.BALANCE);
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

    private void sendRequest(int mode) {

        try {

            SocketEngine socketEngine = new SocketEngine(Constant.Pax.SERVER_IP, Constant.Pax.SERVER_PORT, transactionDataModel);
            socketEngine.sendData(TransactionHelper.getPacker(getActivity(),transactionDataModel, mode,"0" ), new ISocketCallback() {
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
                        printerHelper.startPrint(printable.getContent(getActivity(), "فروشگاه اکبر فرهادی", "77695885", "1475478589", "12:22:15", "1396/08/02", dataModel.getBackTransactionID(), dataModel.getTerminalID(), dataModel.getPanNumber(), dataModel.getAmount()));
                    }
                }
            });

        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceBalanceFragment", "sendRequest");
        }
    }
}
