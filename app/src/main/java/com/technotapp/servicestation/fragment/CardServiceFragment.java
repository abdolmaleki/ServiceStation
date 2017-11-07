package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;

import java.util.ArrayList;

public class CardServiceFragment extends SubMenuFragment implements AdapterView.OnItemClickListener {

    GridView gridView;
    Bundle args;
    Fragment fragment;

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

        initView(rooView);
        initAdapter();

        return rooView;
    }

    private void initView(View v) {
        try {
            gridView = (GridView) v.findViewById(R.id.fragment_card_services_grdList);
            fragment= CardServiceDepositAndBuyFragment.newInstance();
            setRetainInstance(true);
            setTitle(getString(R.string.CardServiceFragment_title));
            gridView.setOnItemClickListener(this);
        }catch (Exception e){
            AppMonitor.reportBug(e,"CardServiceFragment","initView");
        }
    }

    private void initAdapter() {
        try {

            ArrayList<SubMenuModel> subMenuModels = new ArrayList<>();
            subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_cardToCard), R.drawable.ic_card_to_card));
            subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_Deposit), R.drawable.ic_deposit));
            subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_Buy), R.drawable.ic_buy_card));
            subMenuModels.add(new SubMenuModel(getString(R.string.CardServiceFragment_Menu_Balance), R.drawable.ic_balance));
            SubMenuAdapter menuAdapter = new SubMenuAdapter(mActivity, subMenuModels);
            gridView.setAdapter(menuAdapter);
        }catch (Exception e){
            AppMonitor.reportBug(e,"CardServiceFragment","initAdapter");
        }
    }

    private void loadData() {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            args = new Bundle();
            switch (position) {
                // TODO add card to card
                case 0:
                    break;
                case 1:
                    args.putBoolean("isDeposit", true);
                    fragment.setArguments(args);
                    submitFragment(fragment);
                    break;
                case 2:
                    args.putBoolean("isDeposit", false);
                    fragment.setArguments(args);
                    submitFragment(fragment);
                    break;
                case 3:
                    balanceCard();
                    break;

            }
        }catch (Exception e){
            AppMonitor.reportBug(e,"CardServiceFragment","onItemClick");
        }

    }

    TransactionDataModel transactionDataModel;

    private void balanceCard() {

        startMagCard();
    }

    @Override
    public void onPinEnteredSuccessfully() {
        super.onPinEnteredSuccessfully();
        try {
            TransactionHelper.sendRequest(getActivity(), Constant.RequestMode.BALANCE, transactionDataModel, "0");
        }catch (Exception e){
            AppMonitor.reportBug(e,"CardServiceFragment","onPinEnteredSuccessfully");
        }

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
            AppMonitor.reportBug(e, "CardServiceFragment", "startMagCard");
        }
    }

    private void initTransactionModel() {
        transactionDataModel = new TransactionDataModel();
        //TODO setTerminalID
        //TODO setPanNumber
        transactionDataModel.setTerminalID("23801101741");
        transactionDataModel.setPanNumber("6037997293714508");
    }

}
