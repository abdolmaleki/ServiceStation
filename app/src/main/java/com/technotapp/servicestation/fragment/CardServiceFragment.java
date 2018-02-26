package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.FactorActivity;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.adapter.MenuAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.BaseDto;
import com.technotapp.servicestation.connection.restapi.dto.TerminalTransactionDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.pax.printer.PrintMaker;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;

import io.realm.RealmResults;

public class CardServiceFragment extends SubMenuFragment implements AdapterView.OnItemClickListener {

    GridView gridView;
    Bundle args;
    Fragment fragment;
    private int mMenuId;
    private MenuModel mCurrenMenu;
    private Session mSession;

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
        initDb();

    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMenuId = bundle.getInt(Constant.Key.MENU_ID, -1);
            mCurrenMenu = Db.Menu.getMenuById(mMenuId);
        }
    }

    private void initDb() {
        Db.init(getActivity());
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
            gridView = v.findViewById(R.id.fragment_card_services_grdList);
            fragment = CardServiceDepositAndBuyFragment.newInstance();
            setRetainInstance(true);
            setTitle(mCurrenMenu.title);
            gridView.setOnItemClickListener(this);
            mSession = Session.getInstance(getActivity());
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "CardServiceFragment", "initView");
        }
    }

    private void initAdapter() {

        try {
            ArrayList<MenuAdapterModel> mainMenuAdapterModels = new ArrayList<>();
            RealmResults<MenuModel> models = Db.Menu.getSubMenu(mMenuId);
            for (MenuModel menuModel : models) {
                mainMenuAdapterModels.add(new MenuAdapterModel(menuModel));
            }
            //TODO set main menu item title and icons
            MenuAdapter menuAdapter = new MenuAdapter(mActivity, mainMenuAdapterModels);
            gridView.setAdapter(menuAdapter);
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "CardServiceFragment", "initAdapter");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MenuModel model = Db.Menu.getMenuById(id);
        try {
            args = new Bundle();
            switch (model.action) {
                // TODO add card to card
                case Constant.MenuAction.CARD_TO_CARD:
                    break;
                case Constant.MenuAction.DEPOSIT:
                    args.putBoolean("isDeposit", true);
                    fragment.setArguments(args);
                    // submitFragment(fragment);
                    break;
                case Constant.MenuAction.BUY:
                    TransactionHelper.startServiceTransaction(getActivity(), ServiceType.TRANSACTION_BUY, new BaseDto(), new PaymentListFragment.PaymentResultListener() {
                        @Override
                        public void onSuccessfullPayment(String message, BaseTransactionSto response) {

                        }

                        @Override
                        public void onFailedPayment(String message, BaseTransactionSto baseTransactionSto) {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    break;
                case Constant.MenuAction.BALANCE:
                    balanceCard();
                    break;

            }
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "CardServiceFragment", "onItemClick");
        }

    }

    TransactionDataModel transactionDataModel;

    private void balanceCard() {
        TransactionHelper.startServiceTransaction(getActivity(), ServiceType.TRANSACTION_BALANCE, createTransactionDto(Constant.MenuAction.BALANCE), new PaymentListFragment.PaymentResultListener() {
            @Override
            public void onSuccessfullPayment(String message, BaseTransactionSto response) {
                PrintMaker.startPrint(getActivity(), Constant.RequestMode.BALANCE, response);
            }

            @Override
            public void onFailedPayment(String message, BaseTransactionSto baseTransactionSto) {
                Helper.alert(getActivity(), message, Constant.AlertType.Error);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    private TerminalTransactionDto createTransactionDto(String action) {
        TerminalTransactionDto dto = new TerminalTransactionDto();

        switch (action) {
            case Constant.MenuAction.BALANCE:
                dto.transactionModel.codeTransactionType = Constant.TransactionType.BALANCE;
                dto.transactionModel.amountOfTransaction = 0;
                break;

        }
        return dto;
    }

    @Override
    public void onPinEnteredSuccessfully() {
        super.onPinEnteredSuccessfully();

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
            AppMonitor.reportBug(getActivity(), e, "CardServiceFragment", "startMagCard");
        }
    }

    private void initTransactionModel() {
        transactionDataModel = new TransactionDataModel();
        //TODO setTerminalID
        transactionDataModel.setTerminalID(mSession.getTerminalId());
    }

}
