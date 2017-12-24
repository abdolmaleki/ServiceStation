package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KhalafiFragment extends SubMenuFragment implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

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
        txtTitle.setText("استعلام خلافی خودرو");
    }

    @Override
    public void onClick(View view) {

    }
}
