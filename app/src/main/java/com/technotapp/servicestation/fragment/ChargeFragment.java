package com.technotapp.servicestation.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.adapter.CardChargeAdapter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

import com.technotapp.servicestation.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ChargeFragment extends SubMenuFragment implements View.OnClickListener {

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

    @BindView(R.id.fragment_charge_btnAdi)
    Button btnAdi;
    @BindView(R.id.fragment_charge_btnMostaghim)
    Button btnMostaghim;
    @BindView(R.id.fragment_charge_btnShegeftAngiz)
    Button btnShegeftAngiz;
    @BindView(R.id.fragment_charge_btnCodeCharge)
    Button btnCodeCharge;
    @BindView(R.id.fragment_charge_edtPhoneNumber)
    EditText edtPhoneNumber;

    @BindView(R.id.fragment_charge_rclCharges)
    DiscreteScrollView rclCardCharge;

    private RecyclerView.Adapter adapterCardCharge;
    private ArrayList<String> data;
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
        unbinder = ButterKnife.bind(this, rooView);
        data = new ArrayList<>();
        data.add("1,000");
        data.add("2,000");
        data.add("5,000");
        data.add("10,000");
        data.add("20,000");
        data.add("50,000");

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

            btnAdi.setOnClickListener(this);
            btnMostaghim.setOnClickListener(this);
            btnShegeftAngiz.setOnClickListener(this);
            btnCodeCharge.setOnClickListener(this);

            btnIrancell.performClick();
            btnMostaghim.performClick();
            btnAdi.performClick();

            adapterCardCharge = new CardChargeAdapter(getContext(), data);
            rclCardCharge.setAdapter(adapterCardCharge);
            rclCardCharge.scrollToPosition(data.size()/2);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ChargeFragment", "bindView");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fragment_charge_btnHamraheAval:
                resetUi();

                btnHamraheAval.setImageResource(R.drawable.ic_hamrah_aval_selected);
                break;

            case R.id.fragment_charge_btnRightel:
                resetUi();
                btnRightel.setImageResource(R.drawable.ic_rightel_selected);
                break;

            case R.id.fragment_charge_btnTaliya:
                resetUi();
                btnTaliya.setImageResource(R.drawable.ic_taliya_selected);
                break;

            case R.id.fragment_charge_btnIrancell:
                resetUi();
                btnIrancell.setImageResource(R.drawable.ic_irancell_selected);
                break;

            case R.id.fragment_charge_btnMostaghim:
                changeTypeChargeUI(btnMostaghim, btnCodeCharge, true);

                break;
            case R.id.fragment_charge_btnCodeCharge:
                changeTypeChargeUI(btnCodeCharge,btnMostaghim,false);
                break;
            case R.id.fragment_charge_btnAdi:

                btnAdi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                btnAdi.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
                btnShegeftAngiz.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                btnShegeftAngiz.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));

                break;
            case R.id.fragment_charge_btnShegeftAngiz:

                btnShegeftAngiz.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
                btnShegeftAngiz.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
                btnAdi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                btnAdi.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));
                break;
        }


    }

    private void changeTypeChargeUI(Button btnClicked, Button btnsecond, boolean isEnabled) {

        btnClicked.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
        btnClicked.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
        btnsecond.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        btnsecond.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));

        btnShegeftAngiz.setEnabled(isEnabled);
        btnAdi.setEnabled(isEnabled);
        if (!isEnabled) {
            edtPhoneNumber.setEnabled(false);
            btnAdi.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));
            btnAdi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            btnShegeftAngiz.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));
            btnShegeftAngiz.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            btnAdi.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
            btnShegeftAngiz.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
            btnAdi.performClick();
        }
    }

    private void resetUi() {
        btnHamraheAval.setImageResource(R.drawable.ic_hamrah_aval);
        btnIrancell.setImageResource(R.drawable.ic_irancell);
        btnTaliya.setImageResource(R.drawable.ic_taliya);
        btnRightel.setImageResource(R.drawable.ic_rightel);
        btnMostaghim.performClick();
        rclCardCharge.scrollToPosition(data.size()/2);
        btnAdi.performClick();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}