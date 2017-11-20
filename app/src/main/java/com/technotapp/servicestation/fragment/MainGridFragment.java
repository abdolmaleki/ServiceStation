package com.technotapp.servicestation.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.MenuAdapter;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class MainGridFragment extends Fragment {

    private GridView mGridView;
    private MenuAdapter mGridAdapter;
    private ArrayList<MenuAdapterModel> dataSet;

    private Activity mActivity;
    private final String mClassName = getClass().getSimpleName();


    @SuppressLint("ValidFragment")
    public MainGridFragment(ArrayList<MenuAdapterModel> dataSet, Activity activity) {
        this.dataSet = dataSet;
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_grid_view, container, false);
        mGridView = view.findViewById(R.id.fragment_main_grid_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {


            if (mActivity != null) {
                mGridAdapter = new MenuAdapter(mActivity, dataSet);
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
            AppMonitor.reportBug(e, mClassName, "onActivityCreated");
        }
    }

    public void onGridItemClick(GridView g, View v, int pos, long id) {
        try {
            MenuModel selectedMenuItem = Db.Menu.getMenuById(id);
            Helper.lunchActivity(mActivity, selectedMenuItem.controller, (int) id);
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "onGridItemClick");
        }

    }

}
