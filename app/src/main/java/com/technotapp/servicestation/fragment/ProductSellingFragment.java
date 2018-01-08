package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.FactorActivity;
import com.technotapp.servicestation.activity.ProductManagementActivity;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.adapter.ProductSellingAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.SearchProductDto;
import com.technotapp.servicestation.connection.restapi.sto.SearchProductSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.FactorModel;
import com.technotapp.servicestation.database.model.ProductModel;
import com.technotapp.servicestation.entity.FactorMaker;
import com.technotapp.servicestation.mapper.ProductMapper;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class ProductSellingFragment extends Fragment implements SearchView.OnQueryTextListener, ProductSellingAdapter.OnFactorChangeListener, View.OnClickListener {

    private Activity mActivity;
    private GridView mGVProduct;
    private SearchView mSearchView;
    private TextView mTV_totalPrice;
    private FactorMaker mFactorMaker;
    private Button mBTN_confirm;
    private Button mBTN_Refresh;
    private Session mSession;

    public static ProductSellingFragment newInstance() {
        ProductSellingFragment fragment = new ProductSellingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void loadData() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rooView = inflater.inflate(R.layout.fragment_selling_product, container, false);

        loadData();

        initDb();

        initView(rooView);

        initAdapter();

        return rooView;
    }


    private void initDb() {
        Db.init(getActivity());
    }

    private void initView(View rootView) {
        try {
            mGVProduct = rootView.findViewById(R.id.fragment_custom_service_gridView);
            mSearchView = rootView.findViewById(R.id.fragment_custom_service_sv_product);
            mTV_totalPrice = rootView.findViewById(R.id.fragment_custom_service_txt_totalPrice);
            mSearchView.setIconifiedByDefault(false);
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setQueryHint("جستجو");
            mFactorMaker = new FactorMaker();
            mBTN_confirm = rootView.findViewById(R.id.fragment_selling_product_btn_Confirm);
            mBTN_confirm.setOnClickListener(this);
            mBTN_Refresh = rootView.findViewById(R.id.fragment_custom_service_btn_refresh);
            mBTN_Refresh.setOnClickListener(this);
            mBTN_Refresh.requestFocus();

            mSession = Session.getInstance(getActivity());
            setRetainInstance(true);
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductSellingFragment", "initView");
        }
    }

    private void initAdapter() {
        try {
            List<ProductModel> productModels = Db.Product.getAll();
            ArrayList<ProductFactorAdapterModel> adapterModels = ProductMapper.convertModelToFactorAdapterModel(productModels);
            ProductSellingAdapter adapter = new ProductSellingAdapter(mActivity, adapterModels, this);
            mGVProduct.setAdapter(adapter);
            mGVProduct.setTextFilterEnabled(true);

        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductManagementFragment", "initAdapter");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mGVProduct.clearTextFilter();
        } else {
            mGVProduct.setFilterText(newText);
        }
        return true;
    }

    @Override
    public void onAddNewProduct(ProductFactorAdapterModel model) {
        mFactorMaker.updateFactor(model);
        updatePriceBoard(String.valueOf(mFactorMaker.getTotalPrice()));
    }

    private void updatePriceBoard(String totalPrice) {
        mTV_totalPrice.setText(String.valueOf(totalPrice) + " " + "ریال");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBTN_confirm.getId()) {
            FactorModel factorModel = mFactorMaker.exportFactor();
            if (Db.Factor.insert(factorModel) != -1) {
                long factorId = factorModel.getId();
                Intent intent = new Intent(mActivity, FactorActivity.class);
                intent.putExtra(Constant.Key.FACTOR_ID, factorId);
                mActivity.startActivity(intent);
            } else {
                Helper.alert(mActivity, "خطا در ذخیره سازی اطلاعات", Constant.AlertType.Error);
            }
        }

        if (id == R.id.fragment_custom_service_btn_refresh) {
            callSearchProduct();
        }
    }

    private void callSearchProduct() {

        try {
            SearchProductDto searchProductDto = createSearchProductDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.SEARCH_PRODUCT).call(mActivity, searchProductDto, AESsecretKey, "در حال بروزرسانی کالاها", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<SearchProductSto>>() {
                        }.getType();
                        ArrayList<SearchProductSto> searchProductStos = gson.fromJson(jsonResult, listType);

                        if (searchProductStos != null) {
                            if (searchProductStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(searchProductStos.get(0).messageModel.get(0).ver);
                                if (searchProductStos.get(0).dataModel.get(0).result != null && searchProductStos.get(0).dataModel.get(0).result.size() > 0) { // have registered product
                                    if (saveProduct(searchProductStos.get(0).dataModel.get(0).result)) {//db persisant have done
                                        initAdapter();
                                        Helper.alert(mActivity, "بروزرسانی کالاها و خدمات انجام شد", Constant.AlertType.Success);
                                    } else // db persisant have error
                                    {
                                        Helper.alert(mActivity, "بروزرسانی کالاها و خدمات با مشکل مواجه شد", Constant.AlertType.Error);
                                    }

                                } else {  // have not registered product

                                }
                            } else {
                                Helper.alert(mActivity, searchProductStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(getActivity(), e, "ProductSellingFragment", "callSearchProduct-OnResponse");
                    }
                }

                @Override
                public void onFail() {
                    Helper.progressBar.hideDialog();
                    Helper.alert(mActivity, getString(R.string.serverConnectingError), Constant.AlertType.Error);
                }

                @Override
                public void onNetworkProblem(String message) {
                    Helper.alert(mActivity, message, Constant.AlertType.Error);

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductSellingFragment", "callSearchProduct");
        }
    }

    private SearchProductDto createSearchProductDto() {

        SearchProductDto dto = new SearchProductDto();
        dto.terminalCode = mSession.getTerminalId();
        dto.tokenId = mSession.getTokenId();
        dto.skipRows = 0;
        dto.takeRows = Constant.Valuse.MAX_ROW_PRODUCT;
        return dto;
    }

    private boolean saveProduct(List<SearchProductSto.DataModel.Result> results) {
        try {
            for (SearchProductSto.DataModel.Result result : results) {
                ProductModel productModel = ProductMapper.convertSearchResultToProductModel(result);
                Db.Product.insert(productModel, result.nidProduct);
            }
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "ProductManagementActivity", "saveProduct");
            return true;
        }

    }
}
