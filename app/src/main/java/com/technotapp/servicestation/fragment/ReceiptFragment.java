package com.technotapp.servicestation.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pax.dal.IScanner;
import com.pax.dal.entity.EScannerType;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReceiptFragment extends SubMenuFragment implements View.OnClickListener {

    @BindView(R.id.fragment_receipt_btnQrReader)
    Button btnQrReader;
    @BindView(R.id.fragment_receipt_edtPaymentCode)
    EditText edtPaymentCode;
    @BindView(R.id.fragment_receipt_edtBillingId)
    EditText edtBillingId;
    @BindView(R.id.fragment_receipt_btnConfirm)
    Button btnConfirm;
    private Unbinder unbinder;
    Bundle bundle;
    private final String mClassName = getClass().getSimpleName();
    private long mMenuId;
    private MenuModel mCurrenMenu;

    public static ReceiptFragment newInstance() {
        ReceiptFragment fragment = new ReceiptFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        initDb();

    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMenuId = bundle.getInt(Constant.Key.MENU_ID, -1);
            mCurrenMenu = Db.Menu.getMenuById(mMenuId);
        }
    }

    private void initDb() {
        Db.init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_receipt, container, false);
        ButterKnife.bind(this, rootView);
        unbinder = ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        try {
            setRetainInstance(true);

            setTitle(mCurrenMenu.title);
            bundle = new Bundle();
            btnQrReader.setOnClickListener(this);
            btnConfirm.setOnClickListener(this);
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private NeptuneLiteUser neptuneLiteUser = NeptuneLiteUser.getInstance();
    String str;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_receipt_btnQrReader:
                try {

                    neptuneLiteUser.getDal(getActivity()).getScanner(EScannerType.RIGHT).open();
                    neptuneLiteUser.getDal(getActivity()).getScanner(EScannerType.RIGHT).start(new IScanner.IScanListener() {
                        @Override
                        public void onRead(String result) {
                            str = result;


                        }

                        @Override
                        public void onFinish() {
                            try {
                                neptuneLiteUser.getDal(getActivity()).getScanner(EScannerType.RIGHT).close();
                                checkValidation(str);
                            } catch (Exception e) {
                                AppMonitor.reportBug(e, mClassName, "onFinish");
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                } catch (Exception e) {
                    AppMonitor.reportBug(e, mClassName, "submitFragment");
                }
                break;
            case R.id.fragment_receipt_btnConfirm:
                String strBill = edtBillingId.getText().toString();
                String strpayment = edtPaymentCode.getText().toString();
                int lenghtBill = (13 - strBill.length());
                int lenghtPayment = (13 - strpayment.length());
                for (int i = 0; i < lenghtBill; i++) {
                    strBill = "0" + strBill;
                }
                for (int i = 0; i < lenghtPayment; i++) {
                    strpayment = "0" + strpayment;
                }
                checkValidation(strBill + strpayment);
                break;
        }

    }

    private void checkValidation(String result) {
        AppMonitor.Log(result);
        if (parseBillDetail(result)) {
            try {
                ReceiptDialogFragment dialogFragment = ReceiptDialogFragment.newInstance();
                dialogFragment.show(getActivity().getFragmentManager(), "");
                dialogFragment.setCancelable(false);
                dialogFragment.setArguments(bundle);
                //todo change this transaction
            } catch (Exception e) {
                AppMonitor.reportBug(e, mClassName, "submitFragment");
            }
        }
    }


    private boolean parseBillDetail(String strBarcode) {
        boolean isTrue = false;
        if (strBarcode != null) {
            if (strBarcode.length() == 26) {

                String billingId = strBarcode.substring(0, 13);
                String paymentCode = strBarcode.substring(13);

                byte organizationCode = Byte.parseByte(billingId.substring(11, 12));
                bundle.putInt("organization_image", Helper.getImageOrganization(organizationCode));
                bundle.putString("amount", "" + Integer.parseInt(paymentCode.substring(0, 8)) * 10000);

                switch (organizationCode) {
                    case Constant.PayBill.Organization.WATER:
                        bundle.putString("organization_name", "" + "قبض آب");
                        if (isValidBillDetail(billingId, paymentCode)) {
                            isTrue = true;
                        } else {
                            Helper.alert(mActivity,"بارکد مورد نظر معتبر نمی باشد",Constant.AlertType.Error);
                        }
                        break;
                    case Constant.PayBill.Organization.ELECTRICAL:
                        bundle.putString("organization_name", "" + "قبض برق");
                        if (isValidBillDetail(billingId, paymentCode)) {
                            isTrue = true;
                        } else {
                            Helper.alert(mActivity,"بارکد مورد نظر معتبر نمی باشد",Constant.AlertType.Error);
                        }
                        break;
                    case Constant.PayBill.Organization.GAS:
                        bundle.putString("organization_name", "" + "قبض گاز");
                        if (isValidBillDetail(billingId, paymentCode)) {
                            isTrue = true;
                        } else {
                            Helper.alert(mActivity,"بارکد مورد نظر معتبر نمی باشد",Constant.AlertType.Error);
                        }
                        break;
                    case Constant.PayBill.Organization.PHONE:
                        bundle.putString("organization_name", "" + "قبض تلفن");
                        if (isValidBillDetail(billingId, paymentCode)) {
                            isTrue = true;
                        } else {
                            Helper.alert(mActivity,"بارکد مورد نظر معتبر نمی باشد",Constant.AlertType.Error);
                        }
                        break;
                    case Constant.PayBill.Organization.MOBILE:
                    case Constant.PayBill.Organization.TAX_OF_MUNICIPALITY:
                    case Constant.PayBill.Organization.TAX:
                    case Constant.PayBill.Organization.TRAFFIC_CRIME:
                        Helper.alert(mActivity,"بارکد مورد نظر معتبر نمی باشد",Constant.AlertType.Error);
                        break;
                }
            } else {
                Helper.alert(mActivity,"بارکد مورد نظر معتبر نمی باشد",Constant.AlertType.Error);
            }
        } else {
            AppMonitor.Log("result null");

        }

        return isTrue;
    }

    private boolean isValidBillDetail(String strBillingCode, String strPaymentCode) {
        int[] numBill = new int[13];
        int[] numPayment = new int[13];

        for (int i = 0; i < 13; i++) {
            numBill[i] = Integer.parseInt(String.valueOf(strBillingCode.charAt(i)));
            numPayment[i] = Integer.parseInt(String.valueOf(strPaymentCode.charAt(i)));
        }
        if (isValidBillingId(numBill)) {
            if (isValidPaymentControl1(numPayment)) {
                return isValidPaymentControl2(strPaymentCode, strBillingCode);
            }
        }


        return false;
    }

    private boolean isValidBillingId(int[] numbers) {
        byte k = 2;
        int sum = 0;

        for (int i = 12; i >= 0; i--) {

            if (i != 12) {
                sum += numbers[i] * k;
                k++;
                if (k == 8) {
                    k = 2;
                }
            }
        }
        int modeNumber = (sum % 11);
        if (modeNumber == 0 || modeNumber == 1) {
            modeNumber = 11;
        }
        return numbers[12] == (11 - modeNumber);
    }

    private boolean isValidPaymentControl1(int[] numbers) {

        byte k = 2;
        int sum = 0;

        for (int i = 11; i >= 0; i--) {

            if (i != 11) {
                sum += numbers[i] * k;
                k++;
                if (k == 8) {
                    k = 2;
                }
            }
        }
        int modeNumber = (sum % 11);
        if (modeNumber == 0 || modeNumber == 1) {
            modeNumber = 11;
        }
        return numbers[11] == (11 - modeNumber);
    }

    private boolean isValidPaymentControl2(String payment, String billing) {
        Long longPayment = Long.parseLong(payment);
        Long longBilling = Long.parseLong(billing);
        String strPayment = longPayment.toString();
        String strBilling = longBilling.toString();

        bundle.putString("paymentCode", strPayment);
        bundle.putString("billingId", strBilling);

        String total = strBilling + strPayment;
        byte k = 2;
        int sum = 0;
        int size = total.length();

        int[] numbers = new int[size];

        for (int i = size - 1; i >= 0; i--) {
            numbers[i] = Integer.parseInt(String.valueOf(total.charAt(i)));
            if (i != size - 1) {
                sum += numbers[i] * k;
                k++;
                if (k == 8) {
                    k = 2;
                }
            }
        }

        int modeNumber = (sum % 11);
        if (modeNumber == 0 || modeNumber == 1) {
            modeNumber = 11;
        }

        return (numbers[size - 1] == (11 - modeNumber));
    }


}
