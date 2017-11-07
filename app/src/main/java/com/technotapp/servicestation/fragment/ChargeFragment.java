package com.technotapp.servicestation.fragment;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;


public class ChargeFragment extends SubMenuFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    Fragment fragment;
    Context mContext;

    private ImageButton btnIrancell;
    private ImageButton btnRightel;
    private ImageButton btnHamraheAval;
    private ImageButton btnTaliya;
    private TextView txtOperatorName;
    private View topBackground;
    private LinearLayout stroke;
    private SwitchButton switchTypeCharge1;
    private SwitchButton switchTypeCharge2;

    private static final byte RIGHTEL = 0;
    private static final byte IRANCELL = 1;
    private static final byte HAMRAHEAVAL = 2;
    private static final byte TALIYA = 3;

    private int currentColor;

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
        bindView(rooView);
        return rooView;
    }

    private void bindView(View v) {
        try {
            setRetainInstance(true);
            setTitle(getString(R.string.ChargeFragment_charge_service));

            btnHamraheAval = (ImageButton) v.findViewById(R.id.fragment_charge_btnHamraheAval);
            btnIrancell = (ImageButton) v.findViewById(R.id.fragment_charge_btnIrancell);
            btnRightel = (ImageButton) v.findViewById(R.id.fragment_charge_btnRightel);
            btnTaliya = (ImageButton) v.findViewById(R.id.fragment_charge_btnTaliya);
            txtOperatorName = (TextView) v.findViewById(R.id.fragment_charge_txtOperatorType);
            topBackground = v.findViewById(R.id.fragment_charge_viewBg);
            switchTypeCharge1 = (SwitchButton) v.findViewById(R.id.fragment_charge_switchTypeCharge1);
            switchTypeCharge2 = (SwitchButton) v.findViewById(R.id.fragment_charge_switchTypeCharge2);

            btnHamraheAval.setOnClickListener(this);
            btnIrancell.setOnClickListener(this);
            btnRightel.setOnClickListener(this);
            btnTaliya.setOnClickListener(this);
            switchTypeCharge1.setOnCheckedChangeListener(this);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ChargeFragment", "bindView");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_charge_btnHamraheAval:
                currentColor = currentColor(HAMRAHEAVAL);
                initView("شارژ همراه اول");
                break;

            case R.id.fragment_charge_btnRightel:
                currentColor = currentColor(RIGHTEL);
                initView("شارژ رایتل");
                break;

            case R.id.fragment_charge_btnTaliya:
                currentColor = currentColor(TALIYA);
                initView("شارژ تالیا");
                break;

            case R.id.fragment_charge_btnIrancell:
                currentColor = currentColor(IRANCELL);
                initView("شارژ ایرانسل");

                break;

        }

    }

    private void initView(String str) {
        txtOperatorName.setText(str);
        txtOperatorName.setBackground(getResources().getDrawable(currentColor));
//        topBackground.setBackground(getResources().getDrawable(currentColor));
        switchTypeCharge1.setThumbColorRes(currentColor);
        switchTypeCharge2.setThumbColorRes(currentColor);


    }


    private int currentColor(byte currentOperator) {
        switch (currentOperator) {
            case HAMRAHEAVAL:
                return R.color.fragment_charge_hamraheAval;
            case RIGHTEL:
                return R.color.fragment_charge_rightel;
            case IRANCELL:
                return R.color.fragment_charge_irancell;
            case TALIYA:
                return R.color.fragment_charge_taliya;
        }
        return 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.fragment_charge_switchTypeCharge1:
                if (isChecked) {


                } else {


                }
                break;
            case R.id.fragment_charge_switchTypeCharge2:


                break;
        }

    }
}