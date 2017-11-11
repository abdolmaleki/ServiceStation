package com.technotapp.servicestation.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.adapter.CardChargeAdapter;
import com.technotapp.servicestation.application.Constant;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import com.technotapp.servicestation.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ChargeFragment extends SubMenuFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.fragment_charge_linDirectCharge)
    LinearLayout linDirectCharge;
    @BindView(R.id.fragment_charge_btnIrancell)
    ImageButton btnIrancell;
    @BindView(R.id.fragment_charge_btnRightel)
    ImageButton btnRightel;
    @BindView(R.id.fragment_charge_btnTaliya)
    ImageButton btnTaliya;
    @BindView(R.id.fragment_charge_btnHamraheAval)
    ImageButton btnHamraheAval;

    @BindView(R.id.fragment_charge_btnConfirm)
    Button btnConfirm;

    @BindView(R.id.fragment_charge_txtOperatorType)
    TextView txtOperatorName;
    @BindView(R.id.fragment_charge_txtAdi)
    TextView txtAdi;
    @BindView(R.id.fragment_charge_txtMostaghim)
    TextView txtMostaghim;
    @BindView(R.id.fragment_charge_txtShegeftAngiz)
    TextView txtShegeftAngiz;
    @BindView(R.id.fragment_charge_txtCodeCharge)
    TextView txtCodeCharge;

    @BindView(R.id.fragment_charge_switchTypeCharge1)
    SwitchButton switchTypeCharge1;
    @BindView(R.id.fragment_charge_switchTypeCharge2)
    SwitchButton switchTypeCharge2;

    @BindView(R.id.fragment_charge_rclCharges)
    DiscreteScrollView rclCardCharge;

    private RecyclerView.LayoutManager layoutManagerCardCharge;
    private RecyclerView.Adapter adapterCardCharge;
    private ArrayList<String> data;
    private byte currentOperator;
    private Unbinder unbinder;


    public static ChargeFragment newInstance() {
        ChargeFragment fragment = new ChargeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_charge, container, false);
        ButterKnife.bind(this, rooView);
        unbinder = ButterKnife.bind(this, rooView);
        data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("5");
        data.add("10");
        data.add("20");
        data.add("50");

        initView();

        return rooView;
    }

    private void initView() {
        try {
            setRetainInstance(true);

            setTitle(getString(R.string.ChargeFragment_charge_service));

            btnHamraheAval.setOnClickListener(this);
            btnIrancell.setOnClickListener(this);
            btnRightel.setOnClickListener(this);
            btnTaliya.setOnClickListener(this);
            switchTypeCharge1.setOnCheckedChangeListener(this);
            switchTypeCharge2.setOnCheckedChangeListener(this);

            btnHamraheAval.performClick();

        } catch (Exception e) {
            AppMonitor.reportBug(e, "ChargeFragment", "bindView");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_charge_btnHamraheAval:
                currentOperator = Constant.Operator.HAMRAHEAVAL;
                changeView("شارژ همراه اول", getCurrentColor(currentOperator), R.drawable.bg_fragment_charge_back_switch_hamraheaval, R.drawable.bg_fragment_charge_thumb_switch_hamraheaval,R.drawable.bg_fragment_charge_btn_confirm_hamraheaval);
                break;

            case R.id.fragment_charge_btnRightel:
                currentOperator = Constant.Operator.RIGHTEL;
                changeView("شارژ رایتل", getCurrentColor(currentOperator), R.drawable.bg_fragment_charge_back_switch_rightel, R.drawable.bg_fragment_charge_thumb_switch_rightel,R.drawable.bg_fragment_charge_btn_confirm_rightel);
                break;

            case R.id.fragment_charge_btnTaliya:
                currentOperator = Constant.Operator.TALIYA;
                changeView("شارژ تالیا", getCurrentColor(currentOperator), R.drawable.bg_fragment_charge_back_switch_taliya, R.drawable.bg_fragment_charge_thumb_switch_taliya,R.drawable.bg_fragment_charge_btn_confirm_taliya);
                break;

            case R.id.fragment_charge_btnIrancell:
                currentOperator = Constant.Operator.IRANCELL;
                changeView("شارژ ایرانسل", getCurrentColor(currentOperator), R.drawable.bg_fragment_charge_back_switch_irancell, R.drawable.bg_fragment_charge_thumb_switch_irancell,R.drawable.bg_fragment_charge_btn_confirm_irancell);

                break;

        }

        adapterCardCharge = new CardChargeAdapter(data, currentOperator, getContext());
        rclCardCharge.setAdapter(adapterCardCharge);

    }

    private void changeView(String str, int color, int backSwitch, int thumbSwitch,int btnBackground) {
        switchTypeCharge1.setChecked(false);
        switchTypeCharge2.setChecked(false);
        switchTypeCharge1.setChecked(true);
        switchTypeCharge2.setChecked(true);

        switchTypeCharge1.setBackDrawable(getResources().getDrawable(backSwitch));
        switchTypeCharge1.setThumbDrawable(getResources().getDrawable(thumbSwitch));
        switchTypeCharge2.setBackDrawable(getResources().getDrawable(backSwitch));
        switchTypeCharge2.setThumbDrawable(getResources().getDrawable(thumbSwitch));

        txtOperatorName.setText(str);
        txtOperatorName.setBackground(getResources().getDrawable(color));

        btnConfirm.setBackground(getResources().getDrawable(btnBackground));
    }


    private int getCurrentColor(byte Operator) {
        switch (Operator) {
            case Constant.Operator.HAMRAHEAVAL:
                return R.color.fragment_charge_hamraheAval;
            case Constant.Operator.RIGHTEL:
                return R.color.fragment_charge_rightel;
            case Constant.Operator.IRANCELL:
                return R.color.fragment_charge_irancell;
            case Constant.Operator.TALIYA:
                return R.color.fragment_charge_taliya;
        }
        return 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.fragment_charge_switchTypeCharge1:
                if (isChecked) {
                    linDirectCharge.setVisibility(View.VISIBLE);

                    txtMostaghim.setTextSize(16.0f);
                    txtMostaghim.setTextColor(getResources().getColor(getCurrentColor(currentOperator)));

                    txtCodeCharge.setTextSize(14.0f);
                    txtCodeCharge.setTextColor(getResources().getColor(R.color.fragment_charge_switch_text_color));

                } else {
                    linDirectCharge.setVisibility(View.INVISIBLE);

                    txtMostaghim.setTextSize(14.0f);
                    txtMostaghim.setTextColor(getResources().getColor(R.color.fragment_charge_switch_text_color));


                    txtCodeCharge.setTextSize(16.0f);
                    txtCodeCharge.setTextColor(getResources().getColor(getCurrentColor(currentOperator)));
                }
                break;
            case R.id.fragment_charge_switchTypeCharge2:
                if (isChecked) {

                    txtAdi.setTextSize(16.0f);
                    txtAdi.setTextColor(getResources().getColor(getCurrentColor(currentOperator)));

                    txtShegeftAngiz.setTextSize(14.0f);
                    txtShegeftAngiz.setTextColor(getResources().getColor(R.color.fragment_charge_switch_text_color));

                } else {
                    txtAdi.setTextSize(14.0f);
                    txtAdi.setTextColor(getResources().getColor(R.color.fragment_charge_switch_text_color));

                    txtShegeftAngiz.setTextSize(16.0f);
                    txtShegeftAngiz.setTextColor(getResources().getColor(getCurrentColor(currentOperator)));
                }

                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}