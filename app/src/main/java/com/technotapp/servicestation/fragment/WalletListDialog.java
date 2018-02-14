package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.KeypadActivity;
import com.technotapp.servicestation.adapter.DataModel.WalletAdapterModel;
import com.technotapp.servicestation.adapter.WalletAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.GetCustomerAccountsDto;
import com.technotapp.servicestation.connection.restapi.sto.CustomerAccountSto;
import com.technotapp.servicestation.mapper.WalletMapper;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.crypto.SecretKey;


public class WalletListDialog extends DialogFragment implements View.OnClickListener, WalletAdapter.WalletClickListener {

    private Session mSession;
    private ListView mList_wallet;
    private Activity mActivity;
    private String mCardNumber;
    private long mAmount;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCardNumber = bundle.getString(Constant.Key.CARD_NUMBER, null);
            mAmount = bundle.getLong(Constant.Key.PAYMENT_AMOUNT, -1);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_dialog_wallets, container, false);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_btn_swipe_card);
            }
            initView(view);
            callGetCusomrtAccount();
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "PaymentMenuDialog", "onCreateView");
            return null;
        }
    }

    private void initAdapter(ArrayList<CustomerAccountSto.DataModel.CustomerAccount> accounts) {
        ArrayList<WalletAdapterModel> adapterModels = WalletMapper.convertStoToAdapterModel(accounts);
        WalletAdapter adapter = new WalletAdapter(mActivity, adapterModels, this);
        mList_wallet.setAdapter(adapter);

    }

    public static WalletListDialog newInstance(String cardnumber, long mAmount) {
        WalletListDialog fragment = new WalletListDialog();
        Bundle args = new Bundle();
        args.putString(Constant.Key.CARD_NUMBER, cardnumber);
        args.putLong(Constant.Key.PAYMENT_AMOUNT, mAmount);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View view) {

        mSession = Session.getInstance(getActivity());
        mList_wallet = view.findViewById(R.id.fragment_dialog_wallets_list);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
    }


    private void closeDialog() {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private void callGetCusomrtAccount() {

        try {
            GetCustomerAccountsDto dto = createDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.GET_CUSTOMER_ACCOUNT).call(mActivity, dto, AESsecretKey, "در حال بررسی حساب ها", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<CustomerAccountSto>>() {
                        }.getType();
                        ArrayList<CustomerAccountSto> accountStos = gson.fromJson(jsonResult, listType);

                        if (accountStos != null) {
                            if (accountStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(accountStos.get(0).messageModel.get(0).ver);
                                if (accountStos.get(0).dataModel.get(0).accounts != null && accountStos.get(0).dataModel.get(0).accounts.size() > 0) { // have registered account
                                    initAdapter(accountStos.get(0).dataModel.get(0).accounts);
                                } else {  // have not registered acount
                                    Helper.alert(mActivity, "متسفانه شماره حسابی برای این کارت ثبت نشده است", Constant.AlertType.Error);
                                    closeDialog();
                                }
                            } else {
                                Helper.alert(mActivity, accountStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                                closeDialog();

                            }
                        } else {
                            Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                            closeDialog();
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(mActivity, e, "WalletListDialog", "callGetCusomrtAccount" + "-OnResponse");
                        closeDialog();
                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(mActivity, message, Constant.AlertType.Error);
                    closeDialog();
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(mActivity, e, "WalletListDialog", "callGetCusomrtAccount");
            closeDialog();
        }
    }

    private GetCustomerAccountsDto createDto() {
        GetCustomerAccountsDto dto = new GetCustomerAccountsDto();
        dto.terminalCode = mSession.getTerminalId();
        dto.cardNumber = mCardNumber;
        dto.tokenId = mSession.getTokenId();
        return dto;
    }


    @Override
    public void onWalletClick(WalletAdapterModel model) {


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////   payment amount not entered in previouse steps
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (mAmount == -1) {
            Intent intent = new Intent(mActivity, KeypadActivity.class);
            intent.putExtra(Constant.Key.CARD_NUMBER, mCardNumber);
            intent.putExtra(Constant.Key.ACCOUNT_NUMBER, model.accountNumber);
            intent.putExtra(Constant.Key.IS_ACTIVE_PIN, model.isActivePin);
            mActivity.startActivity(intent);
            closeDialog();

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////   payment amount  entered in previouse steps
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } else {
            if (model.isActivePin) {
                InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance("رمز عبور را وارد کنید", Color.BLUE);
                inputDialogFragment.show(getActivity().getFragmentManager(), "input");
                inputDialogFragment.setOnInputDialogClickListener(new InputDialogFragment.OnInputDialogClick() {
                    @Override
                    public void onAccept(String password) {
                        callTransaction();
                    }
                });
            } else {
                callTransaction();
            }
        }
    }

    private void callTransaction() {
        closeDialog();
    }
}
