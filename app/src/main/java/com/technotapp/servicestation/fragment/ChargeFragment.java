package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Converters;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.adapter.CardChargeAdapter;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.ChargeServiceDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.connection.restapi.sto.TransactionChargeResultSto;
import com.technotapp.servicestation.enums.ChargeType;
import com.technotapp.servicestation.enums.OperatorType;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.pax.printer.PrintMaker;
import com.technotapp.servicestation.setting.Session;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.UUID;

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
    @BindView(R.id.fragment_charge_btn_submit)
    Button btnSubmit;
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
    @BindView(R.id.fragment_charge_tv_charge_amount)
    TextView tvChargeAmount;
    @BindView(R.id.fragment_charge_rclCharges)
    DiscreteScrollView rclCardCharge;


    private RecyclerView.Adapter adapterCardCharge;
    private ArrayList<String> mChargeAmountArray;
    private Unbinder unbinder;
    private OperatorType mOperatorType;
    private int mShChargeType;
    private long mChargeAmount;
    private Session mSession;


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


        initView();
        initAdapter();

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

            btnSubmit.setOnClickListener(this);

            btnIrancell.performClick();
            btnMostaghim.performClick();
            btnAdi.performClick();

            mSession = Session.getInstance(getActivity());


        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ChargeFragment", "bindView");
        }
    }

    private void initAdapter() {

        mChargeAmountArray = new ArrayList<>();
        mChargeAmountArray.add("10,000");
        mChargeAmountArray.add("20,000");
        mChargeAmountArray.add("50,000");
        mChargeAmountArray.add("100,000");
        mChargeAmountArray.add("200,000");
        mChargeAmountArray.add("500,000");

        adapterCardCharge = new CardChargeAdapter(getContext(), mChargeAmountArray);
        rclCardCharge.setAdapter(adapterCardCharge);
        rclCardCharge.scrollToPosition(mChargeAmountArray.size() / 2);
        rclCardCharge.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
            @Override
            public void onScrollStart(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
            }

            @Override
            public void onScrollEnd(@NonNull RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
                String chargeAmount = mChargeAmountArray.get(adapterPosition);
                tvChargeAmount.setText("مبلغ شارژ: " + Converters.convertEnDigitToPersian(chargeAmount) + " ریال");
                mChargeAmount = Long.parseLong(chargeAmount.replace(",", ""));
            }

            @Override
            public void onScroll(float scrollPosition, int currentPosition, int newPosition, @Nullable RecyclerView.ViewHolder currentHolder, @Nullable RecyclerView.ViewHolder newCurrent) {
            }
        });

        ////////////////////////////////////
        // for first run intialize
        ///////////////////////////////////

        resetOperatorTypeUi();
        btnIrancell.setImageResource(R.drawable.ic_irancell_selected);
        mOperatorType = OperatorType.IRANCELL;
        mShChargeType = ChargeType.IrancellCharge.MOSTAGHIM;
        String chargeAmount = mChargeAmountArray.get(rclCardCharge.getCurrentItem());
        tvChargeAmount.setText("مبلغ شارژ: " + Converters.convertEnDigitToPersian(chargeAmount) + " ریال");
        mChargeAmount = Long.parseLong(chargeAmount.replace(",", ""));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////// Operator type
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////

            case R.id.fragment_charge_btnHamraheAval:
                resetOperatorTypeUi();
                btnHamraheAval.setImageResource(R.drawable.ic_hamrah_aval_selected);
                mOperatorType = OperatorType.HAMRAHE_AVAL;
                break;

            case R.id.fragment_charge_btnRightel:
                resetOperatorTypeUi();
                btnRightel.setImageResource(R.drawable.ic_rightel_selected);
                mOperatorType = OperatorType.RIGHTEL;
                break;

            case R.id.fragment_charge_btnTaliya:
                resetOperatorTypeUi();
                btnTaliya.setImageResource(R.drawable.ic_taliya_selected);
                mOperatorType = OperatorType.TALYIA;
                break;

            case R.id.fragment_charge_btnIrancell:
                resetOperatorTypeUi();
                btnIrancell.setImageResource(R.drawable.ic_irancell_selected);
                mOperatorType = OperatorType.IRANCELL;
                break;

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////// Charge type
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////

            case R.id.fragment_charge_btnMostaghim:
                //changeTypeChargeUI(btnMostaghim, btnCodeCharge, true);

                break;
            case R.id.fragment_charge_btnCodeCharge:
                // changeTypeChargeUI(btnCodeCharge, btnMostaghim, false);
                break;
            case R.id.fragment_charge_btnAdi:

//                btnAdi.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
//                btnAdi.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
//                btnShegeftAngiz.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                btnShegeftAngiz.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));

                break;
            case R.id.fragment_charge_btnShegeftAngiz:

//                btnShegeftAngiz.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
//                btnShegeftAngiz.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
//                btnAdi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                btnAdi.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));
                break;

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////// Payment
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////

            case R.id.fragment_charge_btn_submit:
                if (validation()) {
                    TransactionHelper.startServiceTransaction(mActivity, ServiceType.CHARGE, createDto(), new PaymentListFragment.PaymentResultListener() {

                        @Override
                        public void onSuccessfullPayment(String message, BaseTransactionSto response) {
                            PrintMaker.startFactorPrint(getActivity(), response);

                        }

                        @Override
                        public void onFailedPayment(String message, BaseTransactionSto baseTransactionSto) {
                            TransactionChargeResultSto sto = (TransactionChargeResultSto) baseTransactionSto;
                            if (sto != null) {
                                if (sto.errorCode.equals(String.valueOf(Constant.Api.ErrorCode.Successfull)) || sto.errorCode.equals(Constant.Api.TransactionErrorCode.Successfull)) { //successfulTransaction
                                   // PrintMaker.failOperationPrint(getActivity(), baseTransactionSto);
                                }
                            }

                            Helper.alert(getActivity(), message, Constant.AlertType.Error);

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                break;

        }
    }

    private boolean validation() {
        return true;
    }

    private void changeTypeChargeUI(Button btnClicked, Button btnsecond, boolean isEnabled) {

        btnClicked.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick, 0);
        btnClicked.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
        btnsecond.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        btnsecond.setBackground(getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));

        btnShegeftAngiz.setEnabled(isEnabled);
        btnAdi.setEnabled(isEnabled);
        if (!isEnabled) {
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

    private void resetOperatorTypeUi() {
        btnHamraheAval.setImageResource(R.drawable.ic_hamrah_aval);
        btnIrancell.setImageResource(R.drawable.ic_irancell);
        btnTaliya.setImageResource(R.drawable.ic_taliya);
        btnRightel.setImageResource(R.drawable.ic_rightel);
        btnMostaghim.performClick();
        rclCardCharge.scrollToPosition(mChargeAmountArray.size() / 2);
        btnAdi.performClick();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPinEnteredSuccessfully() {
    }

//    private ChargeDto createDto() {
//        ChargeDto dto = new ChargeDto();
//        dto.amount = (int) mChargeAmount;
//        dto.mobileNumber = edtPhoneNumber.getText().toString();
//        dto.param = "0";
//        dto.service = 1;
//        dto.provider = "";
//        dto.svrUerName = "";
//        dto.svrPassword = "";
//        dto.userOrderId = UUID.randomUUID().toString();
//        return dto;
//    }

    private ChargeServiceDto createDto() {
        ChargeServiceDto dto = new ChargeServiceDto();
        dto.chargeModel.amount = (int) mChargeAmount;
        dto.chargeModel.mobileNumber = edtPhoneNumber.getText().toString();
        dto.chargeModel.param = "0";
        dto.chargeModel.service = 1;
        dto.chargeModel.provider = "";
        dto.chargeModel.userOrderId = UUID.randomUUID().toString();
        return dto;
    }


}