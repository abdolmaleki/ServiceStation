package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


public class CardServiceFragment extends Fragment implements AdapterView.OnItemClickListener {

    GridView gridView;
    private Context mActivity;


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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;

    }

    private void initView() {
        setRetainInstance(true);
        gridView.setOnItemClickListener(this);
    }

    private void initAdapter() {
        ArrayList<SubMenuModel> subMenuModels = new ArrayList<>();

        subMenuModels.add(new SubMenuModel("کارت به کارت", R.drawable.ic_card_to_card));
        subMenuModels.add(new SubMenuModel("واریز به حساب", R.drawable.ic_deposit));
        subMenuModels.add(new SubMenuModel("برداشت از حساب", R.drawable.ic_buy_card));
        subMenuModels.add(new SubMenuModel("موجودی حساب", R.drawable.ic_balance));

        SubMenuAdapter menuAdapter = new SubMenuAdapter(mActivity, subMenuModels);
        gridView.setAdapter(menuAdapter);
    }

    private void loadData() {

    }
    private void submitFragment() {
        try {


            CardServiceBalanceFragment fragment = CardServiceBalanceFragment.newInstance();
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_card_service_frame, fragment).commit();
        }catch (Exception e){
            AppMonitor.reportBug(e,"CardServiceActivity","submitFragment");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 3:
                submitFragment();
                break;
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
