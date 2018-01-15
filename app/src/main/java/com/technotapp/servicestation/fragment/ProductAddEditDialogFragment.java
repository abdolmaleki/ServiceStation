package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.ProductManagementActivity;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.AddProductDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.connection.restapi.sto.ProductSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.ProductModel;
import com.technotapp.servicestation.enums.ProductUnitCode;
import com.technotapp.servicestation.mapper.ProductMapper;
import com.technotapp.servicestation.setting.Session;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;


public class ProductAddEditDialogFragment extends DialogFragment implements View.OnClickListener {

    private Activity mActivity;
    private EditText mEtProductName;
    private MaterialBetterSpinner mSPProductUnit;
    private EditText mEtPrice;
    private EditText mETDescription;
    private TextView mTxvHeader;
    private Button mBTNConfirm;
    private Session mSession;
    private ProductModel mProductModel;

    private ChangeProductsListener mChangeProductsListener;


    public static ProductAddEditDialogFragment newInstance(long productId) {
        ProductAddEditDialogFragment fragment = new ProductAddEditDialogFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.Key.PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        initDb();
    }

    private void initDb() {
        Db.init(getActivity());
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            long productId = bundle.getLong(Constant.Key.PRODUCT_ID, -1);
            if (productId > 0) {
                mProductModel = Db.Product.getProductById(productId);
            }
        }
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
            AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "onCreateView");
            return null;
        }
    }

    private void initView(View v) {
        setRetainInstance(true);
        mEtProductName = v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtName);
        mSPProductUnit = (MaterialBetterSpinner) v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtUnit);
        mEtPrice = v.findViewById(R.id.fragment_dialog_product_management_add_edit_edt_price);
        mETDescription = v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtDescription);
        mTxvHeader = v.findViewById(R.id.fragment_dialog_product_management_add_edit_txtHeader);
        mBTNConfirm = v.findViewById(R.id.fragment_dialog_product_management_add_edit_btnConfirm);
        mBTNConfirm.setOnClickListener(this);
        mSession = Session.getInstance(mActivity);
        if (mProductModel != null) {
            fillData();
            mBTNConfirm.setText("ویرایش");
            mTxvHeader.setText("ویرایش کالا");
        } else {
            mBTNConfirm.setText("افزودن");
            mTxvHeader.setText("افزودن کالا");
        }

        fillUnitSpinner();
    }

    private void fillUnitSpinner() {
        String[] ITEMS = {ProductUnitCode.valueOf(1), ProductUnitCode.valueOf(2), ProductUnitCode.valueOf(3), ProductUnitCode.valueOf(4), ProductUnitCode.valueOf(5), ProductUnitCode.valueOf(6), ProductUnitCode.valueOf(7), ProductUnitCode.valueOf(8)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSPProductUnit.setAdapter(adapter);
    }

    private void fillData() {
        mEtProductName.setText(mProductModel.title);
        mEtPrice.setText(mProductModel.price);
        mSPProductUnit.setText(ProductUnitCode.valueOf(mProductModel.unitCode));
        mETDescription.setText(mProductModel.description);
    }

    public void show(Activity activity) {
        try {
            this.setCancelable(true);
            this.show(activity.getFragmentManager(), "ProductManagementAddEdit");
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "show");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        if (mActivity instanceof ChangeProductsListener) {
            mChangeProductsListener = (ChangeProductsListener) mActivity;
        }
    }

    public void callAddProduct() {

        try {
            AddProductDto addProductDto = createAddProdustDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.ADD_UPDATE_PRODUCT).call(mActivity, addProductDto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<ProductSto>>() {
                        }.getType();
                        List<ProductSto> sto = gson.fromJson(jsonResult, listType);

                        if (sto != null) {
                            if (sto.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(sto.get(0).messageModel.get(0).ver);
                                Helper.alert(mActivity, "کالای شما با موفقیت ثبت شد", Constant.AlertType.Success);
                                onSuccessfulAddProduct(addProductDto, sto.get(0).dataModel.get(0).nidProduct);

                            } else {
                                Helper.alert(mActivity, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "callAddProduct-onResponse");
                        Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(mActivity, message, Constant.AlertType.Error);

                }

            });
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "callAddProduct");
        }

    }

    public void callUpdateProduct() {

        try {
            AddProductDto addProductDto = createUpdateProdustDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.ADD_UPDATE_PRODUCT).call(mActivity, addProductDto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<BaseSto>>() {
                        }.getType();
                        List<BaseSto> sto = gson.fromJson(jsonResult, listType);

                        if (sto != null) {
                            if (sto.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(sto.get(0).messageModel.get(0).ver);
                                Helper.alert(mActivity, "تغییرات با موفقیت اعمال شد", Constant.AlertType.Success);
                                onSuccessfulUpdateProduct(addProductDto);

                            } else {
                                Helper.alert(mActivity, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "callAddProduct-onResponse");
                        Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(mActivity, message, Constant.AlertType.Error);
                }

            });
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "callAddProduct");
        }

    }

    public void onSuccessfulAddProduct(AddProductDto addProductDto, long nidProduct) {
        try {
            ProductModel productModel = ProductMapper.convertDtoToModel(addProductDto);
            if (Db.Product.insert(productModel, nidProduct)) {
                mChangeProductsListener.onAddNewProduct();
            }
            dismiss();

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "onSuccessfulAddProduct");
        }
    }

    public void onSuccessfulUpdateProduct(AddProductDto addProductDto) {
        try {
            ProductModel productModel = ProductMapper.convertDtoToModel(addProductDto);
            if (Db.Product.update(productModel, mProductModel.nidProduct)) {
                mChangeProductsListener.onAddNewProduct();
            }
            dismiss();

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductAddEditDialogFragment", "onSuccessfulUpdateProduct");
        }
    }

    private AddProductDto createAddProdustDto() {

        AddProductDto dto = new AddProductDto();
        dto.title = mEtProductName.getText().toString();
        dto.unitCode = Byte.parseByte(ProductUnitCode.getKey(mSPProductUnit.getText().toString()) + "");
        dto.price = Double.parseDouble(mEtPrice.getText().toString());
        dto.description = mETDescription.getText().toString();
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        return dto;
    }

    private AddProductDto createUpdateProdustDto() {

        AddProductDto dto = new AddProductDto();
        dto.title = mEtProductName.getText().toString();
        dto.unitCode = Byte.parseByte(ProductUnitCode.getKey(mSPProductUnit.getText().toString()) + "");
        dto.price = Double.parseDouble(mEtPrice.getText().toString());
        dto.description = mETDescription.getText().toString();
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        dto.nidProduct = mProductModel.nidProduct;
        return dto;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == mBTNConfirm.getId()) {
            if (validation()) {
                if (mProductModel == null) {
                    callAddProduct();
                } else {
                    callUpdateProduct();
                }
            }
        }
    }

    private boolean validation() {
        // Todo
        return true;
    }

    public interface ChangeProductsListener {
        void onAddNewProduct();

        void onUpdateRequest(long productId);
    }

}
