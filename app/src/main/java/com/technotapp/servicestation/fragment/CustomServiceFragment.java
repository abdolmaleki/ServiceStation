package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

public class CustomServiceFragment extends Fragment {

    public static CustomServiceFragment newInstance() {
        CustomServiceFragment fragment = new CustomServiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private void loadData() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_custom_services, container, false);

        loadData();

        initView(rooView);

        initAdapter();

        return rooView;
    }

    private void initView(View v) {
        try {
            setRetainInstance(true);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CustomServiceFragment", "initView");
        }
    }

    private void initAdapter() {
        try {

        } catch (Exception e) {
            AppMonitor.reportBug(e, "CustomServiceFragment", "initAdapter");
        }
    }

}
