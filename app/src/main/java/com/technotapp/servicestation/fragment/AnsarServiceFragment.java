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
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.adapter.MenuAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;

import io.realm.RealmResults;

public class AnsarServiceFragment extends SubMenuFragment implements AdapterView.OnItemClickListener {

    GridView gridView;
    Bundle args;
    private int mMenuId;
    private MenuModel mCurrenMenu;
    private Session mSession;

    public static AnsarServiceFragment newInstance() {
        AnsarServiceFragment fragment = new AnsarServiceFragment();
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
        View rooView = inflater.inflate(R.layout.fragment_ansar_services, container, false);

        initView(rooView);
        initAdapter();

        return rooView;
    }

    private void initView(View v) {
        try {
            gridView = v.findViewById(R.id.fragment_ansar_services_grdList);
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

                    break;
                case Constant.MenuAction.BUY:

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

        startMagCard();
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
