package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.AddProductDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.setting.Session;

import javax.crypto.SecretKey;

public class ProductAddEditDialogFragment extends DialogFragment implements View.OnClickListener {

    private Activity mActivity;
    private EditText mEtProductName;
    private EditText mEtProductUnit;
    private EditText mEtPrice;
    private EditText mETDescription;
    private TextView mTxvHeader;
    private Button mBTNConfirm;
    private Session mSession;


    public static ProductAddEditDialogFragment newInstance() {
        ProductAddEditDialogFragment fragment = new ProductAddEditDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
            }
            View view = inflater.inflate(R.layout.fragment_dialog_product_management_add_edit, container);
            initView(view);
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductAddEditDialogFragment", "onCreateView");
            return null;
        }
    }

    private void initView(View v) {
        mEtProductName = v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtName);
        mEtProductUnit = v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtUnit);
        mEtPrice = v.findViewById(R.id.fragment_dialog_product_management_add_edit_edt_price);
        mETDescription = v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtDescription);
        mTxvHeader = v.findViewById(R.id.fragment_dialog_product_management_add_edit_txtHeader);
        mBTNConfirm = v.findViewById(R.id.fragment_dialog_product_management_add_edit_btnConfirm);
        mBTNConfirm.setOnClickListener(this);
        mSession = Session.getInstance(mActivity);
    }

    public void show(Activity activity) {
        try {
            this.setCancelable(false);
            this.show(activity.getFragmentManager(), "ProductManagementAddEdit");
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductAddEditDialogFragment", "show");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    public void callAddProduct() {

        try {
            AddProductDto addProductDto = createAddProdustDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.ADD_PRODUCT).call(mActivity, addProductDto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    Gson gson = Helper.getGson();
                    BaseSto sto = gson.fromJson(jsonResult, BaseSto.class);

                    if (sto != null) {
                        if (sto.messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                            mSession.setLastVersion(sto.messageModel.get(0).ver);
                            Helper.alert(mActivity, "کالای شما با موفقیت ثبت شد", Constant.AlertType.Success);

                        } else {
                            Helper.alert(mActivity, sto.messageModel.get(0).errorString, Constant.AlertType.Error);
                        }
                    } else {
                        Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                    }
                }

                @Override
                public void onFail() {
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductAddEditDialogFragment", "callAddProduct");
        }

    }

    private AddProductDto createAddProdustDto() {

        AddProductDto dto = new AddProductDto();
        dto.title = mEtProductName.getText().toString();
        dto.unitCode = Byte.parseByte(mEtProductUnit.getText().toString());
        dto.price = Double.parseDouble(mEtPrice.getText().toString());
        dto.description = mETDescription.getText().toString();
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        return dto;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == mBTNConfirm.getId()) {
            if (validation()) {
                callAddProduct();
            }
        }
    }

    private boolean validation() {
        // Todo
        return true;
    }

}
