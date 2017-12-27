package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.technotapp.servicestation.R;

import butterknife.ButterKnife;

public class KhalafiFragment extends SubMenuFragment implements View.OnClickListener {

    public static KhalafiFragment newInstance() {
        KhalafiFragment fragment = new KhalafiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_khalafi, container, false);
        initView(rooView);
        initAdapter();

        return rooView;
    }

    private void initAdapter() {

    }

    private void initView(View rooView) {
        ButterKnife.bind(rooView);
        setTitle("خلافی خودرو");
    }

    @Override
    public void onClick(View view) {

    }
}
