package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.SubMenuModel;
import com.technotapp.servicestation.adapter.SubMenuAdapter;

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
            case 3:
                submitFragment(CardServiceBalanceFragment.newInstance());
                break;
            //TODO add other menus
//            case 1:
//                startActivity(new Intent(CardServiceActivity.this, .class));
//                break;
//            case 2:
//                startActivity(new Intent(CardServiceActivity.this, .class));
//                break;
//            case 3:
//                startActivity(new Intent(CardServiceActivity.this, .class));
//                break;
        }
    }
}
