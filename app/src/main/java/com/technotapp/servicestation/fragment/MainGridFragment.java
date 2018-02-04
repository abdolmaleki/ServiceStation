package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.UrlActivity;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.MenuAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;

import java.util.ArrayList;

public class MainGridFragment extends Fragment {

    private GridView mGridView;
    private ArrayList<MenuAdapterModel> dataSet;
    private Context mContext;
    private final String mClassName = getClass().getSimpleName();

    public MainGridFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            dataSet = bundle.getParcelableArrayList(Constant.Key.MENU_ADAPTER_MODEL);
        }
    }

    public static MainGridFragment newInstance(ArrayList<MenuAdapterModel> dataSet) {
        MainGridFragment fragment = new MainGridFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constant.Key.MENU_ADAPTER_MODEL, dataSet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_grid_view, container, false);
        mGridView = view.findViewById(R.id.fragment_main_grid_view);
        initView();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    private void initView() {
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            if (mContext != null) {
                MenuAdapter mGridAdapter = new MenuAdapter(mContext, dataSet);
                if (mGridView != null) {
                    mGridView.setAdapter(mGridAdapter);
                }
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        onGridItemClick((GridView) parent, view, pos, id);
                    }
                });
            }
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, mClassName, "onActivityCreated");
        }
    }

    public void onGridItemClick(GridView g, View v, int pos, long menuId) {
        try {
            MenuModel selectedMenuItem = Db.Menu.getMenuById(menuId);
            String url_action = selectedMenuItem.url;

            if (url_action != null && !TextUtils.isEmpty(url_action)) {
                Intent urlIntent = new Intent(mContext, UrlActivity.class);
                urlIntent.putExtra(Constant.Key.ACTION_URL, url_action);
                urlIntent.putExtra(Constant.Key.MENU_ID, menuId);
                mContext.startActivity(urlIntent);
            } else if (selectedMenuItem.controller != null && !TextUtils.isEmpty(selectedMenuItem.controller)) {
                Helper.lunchActivity(mContext, selectedMenuItem.controller, (int) menuId);
            } else {
                Helper.alert(mContext, "این گزینه فعال نیست", Constant.AlertType.Information);
            }
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, mClassName, "onGridItemClick");
        }

    }

}
