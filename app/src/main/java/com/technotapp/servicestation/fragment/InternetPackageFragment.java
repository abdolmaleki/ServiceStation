package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.InternetPackageModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.dto.BuyInternetPackageDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.connection.restapi.sto.BuyInternetPackaeResultSto;
import com.technotapp.servicestation.connection.restapi.sto.TransactionChargeResultSto;
import com.technotapp.servicestation.customView.CustomButton;
import com.technotapp.servicestation.enums.SimcardType;
import com.technotapp.servicestation.enums.OperatorType;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.pax.printer.PrintMaker;
import com.technotapp.servicestation.setting.Session;

import java.util.UUID;

public class InternetPackageFragment extends SubMenuFragment implements View.OnClickListener {

    private int mOperatorType;
    private int mSimcardType;
    private Session mSession;
    private InternetPackageModel mInternetPackageModel;

    private ImageButton mBTN_Irancell;
    private ImageButton mBTN_Hamrahemraheaval;
    private CustomButton mBTN_Type_etebari;
    private CustomButton mBTN_Type_daemi;
    private CustomButton mBTN_Type_tdlte;
    private CustomButton mBTN_Internet_packages;
    private CustomButton mBTN_Submit;
    private EditText mET_Mobile;

    public static InternetPackageFragment newInstance() {
        InternetPackageFragment fragment = new InternetPackageFragment();
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
        View rooView = inflater.inflate(R.layout.fragment_internet_package, container, false);
        initView(rooView);
        return rooView;
    }

    private void initView(View rooView) {
        try {
            setRetainInstance(true);
            setTitle("بسته های اینترنتی");
            mSession = Session.getInstance(getActivity());

            mBTN_Irancell = rooView.findViewById(R.id.fragment_internet_package_btn_irancell);
            mBTN_Hamrahemraheaval = rooView.findViewById(R.id.fragment_internet_package_btn_hamraheAval);
            mBTN_Type_etebari = rooView.findViewById(R.id.fragment_internet_package_btn_etebari);
            mBTN_Type_daemi = rooView.findViewById(R.id.fragment_internet_package_btn_daemi);
            mBTN_Type_tdlte = rooView.findViewById(R.id.fragment_internet_package_btn_tdlte);
            mBTN_Internet_packages = rooView.findViewById(R.id.fragment_internet_package_btn_packages);
            mBTN_Submit = rooView.findViewById(R.id.fragment_internet_package_btn_submit);
            mET_Mobile = rooView.findViewById(R.id.fragment_internet_package_et_mobile);

            mBTN_Irancell.setOnClickListener(this);
            mBTN_Hamrahemraheaval.setOnClickListener(this);
            mBTN_Type_etebari.setOnClickListener(this);
            mBTN_Type_daemi.setOnClickListener(this);
            mBTN_Type_tdlte.setOnClickListener(this);
            mBTN_Internet_packages.setOnClickListener(this);
            mBTN_Submit.setOnClickListener(this);


            mBTN_Irancell.setImageResource(R.drawable.ic_irancell_selected);
            mOperatorType = OperatorType.IRANCELL;
            mBTN_Type_etebari.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
            mBTN_Type_etebari.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick, 0, 0, 0);
            mSimcardType = SimcardType.IRANCELL_ETEBARI;


        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "InternetPackageFragment", "bindView");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////// Operator type
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////

            case R.id.fragment_internet_package_btn_hamraheAval:
                resetOperatorTypeUi();
                resetSimcardTypeUi();
                mBTN_Hamrahemraheaval.setImageResource(R.drawable.ic_hamrah_aval_selected);
                mOperatorType = OperatorType.HAMRAHE_AVAL;
                mBTN_Type_etebari.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
                mBTN_Type_etebari.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick, 0, 0, 0);
                mSimcardType = SimcardType.HAMRAHEAVAL_ETEBARI;
                mBTN_Type_tdlte.setVisibility(View.GONE);
                break;

            case R.id.fragment_internet_package_btn_irancell:
                resetOperatorTypeUi();
                resetSimcardTypeUi();
                mBTN_Irancell.setImageResource(R.drawable.ic_irancell_selected);
                mOperatorType = OperatorType.IRANCELL;
                mBTN_Type_tdlte.setVisibility(View.VISIBLE);
                mBTN_Type_etebari.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
                mBTN_Type_etebari.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick, 0, 0, 0);
                mSimcardType = SimcardType.IRANCELL_ETEBARI;
                break;

            case R.id.fragment_internet_package_btn_daemi:
                resetSimcardTypeUi();
                mBTN_Type_daemi.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
                mBTN_Type_daemi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick, 0, 0, 0);
                switch (mOperatorType) {
                    case OperatorType.IRANCELL:
                        mSimcardType = SimcardType.IRANCELL_DAEMI;
                        break;

                    case OperatorType.HAMRAHE_AVAL:
                        mSimcardType = SimcardType.HAMRAHEAVAL_DAEMI;
                        break;
                }
                break;

            case R.id.fragment_internet_package_btn_etebari:
                resetSimcardTypeUi();
                mBTN_Type_etebari.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
                mBTN_Type_etebari.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick, 0, 0, 0);
                switch (mOperatorType) {
                    case OperatorType.IRANCELL:
                        mSimcardType = SimcardType.IRANCELL_ETEBARI;
                        break;

                    case OperatorType.HAMRAHE_AVAL:
                        mSimcardType = SimcardType.HAMRAHEAVAL_ETEBARI;
                        break;
                }
                break;

            case R.id.fragment_internet_package_btn_tdlte:
                resetSimcardTypeUi();
                mBTN_Type_tdlte.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge_selected));
                mBTN_Type_tdlte.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tick, 0, 0, 0);
                switch (mOperatorType) {
                    case OperatorType.IRANCELL:
                        mSimcardType = SimcardType.IRANCELL_TED_LTE;
                        break;
                }
                break;

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////// Payment
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////

            case R.id.fragment_internet_package_btn_submit:
                if (validation()) {
                    TransactionHelper.startServiceTransaction(mActivity, ServiceType.BUY_ITERNET_PACKAGE, createDto(), new PaymentListFragment.PaymentResultListener() {

                        @Override
                        public void onSuccessfullPayment(String message, BaseTransactionSto response) {
                            PrintMaker.startFactorPrint(getActivity(), response);

                        }

                        @Override
                        public void onFailedPayment(String message, BaseTransactionSto baseTransactionSto) {
                            BuyInternetPackaeResultSto sto = (BuyInternetPackaeResultSto) baseTransactionSto;
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

            case R.id.fragment_internet_package_btn_packages:
                InternetPackageListDialogFragment internetPackageListDialogFragment = InternetPackageListDialogFragment.newInstance(mOperatorType, mSimcardType);
                internetPackageListDialogFragment.show(getActivity(), new InternetPackageListDialogFragment.InternetPackageListener() {
                    @Override
                    public void onPackageSelected(InternetPackageModel model) {
                        mInternetPackageModel = model;
                        mBTN_Internet_packages.setText(model.serviceName);
                        mBTN_Internet_packages.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_change, 0, 0, 0);
                    }
                });
                break;

        }
    }

    private boolean validation() {

        boolean valid = true;
        if (mInternetPackageModel == null) {
            Helper.alert(mActivity, "شما هیچ بسته ای را انتخاب نکرده اید", Constant.AlertType.Information);
            valid = false;
        } else if (mET_Mobile.getText().toString().equals("")) {
            Helper.alert(mActivity, "شماره موبایل را وارد نکرده اید", Constant.AlertType.Information);
            valid = false;
        }

        return valid;
    }

    private void resetOperatorTypeUi() {
        mBTN_Hamrahemraheaval.setImageResource(R.drawable.ic_hamrah_aval);
        mBTN_Irancell.setImageResource(R.drawable.ic_irancell);
        mBTN_Internet_packages.setText("مشاهده لیست بسته ها");
        mInternetPackageModel = null;
        mBTN_Internet_packages.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }

    private void resetSimcardTypeUi() {
        mBTN_Type_etebari.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));
        mBTN_Type_daemi.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));
        mBTN_Type_tdlte.setBackground(mActivity.getResources().getDrawable(R.drawable.cs_btn_charge_fragment_type_charge));
        mBTN_Type_etebari.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mBTN_Type_daemi.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mBTN_Type_tdlte.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mBTN_Internet_packages.setText("مشاهده لیست بسته ها");
        mInternetPackageModel = null;
        mBTN_Internet_packages.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private BuyInternetPackageDto createDto() {
        BuyInternetPackageDto dto = new BuyInternetPackageDto();
        dto.internetPackModel.param = "0";
        dto.internetPackModel.service = mInternetPackageModel.serviceID;
        dto.internetPackModel.provider = String.valueOf(mOperatorType);
        dto.internetPackModel.userOrderId = UUID.randomUUID().toString();
        dto.internetPackModel.mobileNumber = mET_Mobile.getText().toString();
        dto.internetPackModel.amount = mInternetPackageModel.servicePrice;
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        return dto;
    }


}