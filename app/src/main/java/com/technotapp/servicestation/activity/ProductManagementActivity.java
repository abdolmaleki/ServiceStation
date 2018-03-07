package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.ProductManagementAdapter;
import com.technotapp.servicestation.adapter.DataModel.ProductAdapterModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.SearchProductDto;
import com.technotapp.servicestation.connection.restapi.sto.SearchProductSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.ProductModel;
import com.technotapp.servicestation.fragment.ProductAddEditDialogFragment;
import com.technotapp.servicestation.mapper.ProductMapper;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductManagementActivity extends BaseActivity implements View.OnClickListener, ProductAddEditDialogFragment.ChangeProductsListener, SearchView.OnQueryTextListener {

    @BindView(R.id.activity_product_management_listView)
    ListView productListView;
    @BindView(R.id.activity_product_management_fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;
    @BindView(R.id.activity_product_management_btn_refresh)
    Button mBTN_Refresh;

    private SearchView mSearchView;
    private Session mSession;
    private String mUserTokenId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        ButterKnife.bind(this);
        loadData();

        initDb();

        initView();

        initAdapter();

        callSearchProduct();
    }

    private void initView() {
        try {
            mSearchView = findViewById(R.id.activity_product_management_sv_product);
            back.setOnClickListener(this);
            fab.setOnClickListener(this);
            txtTitle.setText("مدیریت کالا ها");
            mBTN_Refresh.requestFocus();

            findViewById(R.id.activity_product_management_btn_refresh).setOnClickListener(this);

        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "ProductManagementActivity", "initView");
        }

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("جستجو");

        mSession = Session.getInstance(this);
    }

    private void initAdapter() {

        try {
            List<ProductModel> productModels = Db.Product.getAll();
            ArrayList<ProductAdapterModel> adapterModels = ProductMapper.convertModelToAdapterModel(productModels);
            ProductManagementAdapter adapter = new ProductManagementAdapter(this, adapterModels);
            productListView.setAdapter(adapter);
            productListView.setTextFilterEnabled(true);

        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "ProductManagementFragment", "initAdapter");
        }
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUserTokenId = bundle.getString(Constant.Key.SUPPORT_TOKEN_ID);
        }
    }

    private void initDb() {
        Db.init(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.toolbar_img_back:
                finish();
                startActivity(new Intent(this, SettingActivity.class));
                break;

            case R.id.activity_product_management_fab:
                ProductAddEditDialogFragment dialogFragment = ProductAddEditDialogFragment.newInstance(-1); // -1 means this is a new product
                dialogFragment.show(this);
                break;

            case R.id.activity_product_management_btn_refresh:
                callSearchProduct();
                break;
        }
    }

    @Override
    public void onAddNewProduct() {
        initAdapter();
    }

    @Override
    public void onUpdateRequest(long productId) {
        ProductAddEditDialogFragment dialogFragment = ProductAddEditDialogFragment.newInstance(productId); // -1 means this is a new product
        dialogFragment.show(this);
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            productListView.clearTextFilter();
        } else {
            productListView.setFilterText(newText);
        }
        return true;
    }

    private void callSearchProduct() {

        try {
            SearchProductDto searchProductDto = createSearchProductDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.SEARCH_PRODUCT).call(this, searchProductDto, AESsecretKey, "در حال بروزرسانی کالاها", new ApiCaller.ApiCallback() {
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
                                    } else // db persisant have error
                                    {
                                        Helper.alert(ProductManagementActivity.this, "بروزرسانی کالاها و خدمات با مشکل مواجه شد", Constant.AlertType.Error);
                                    }

                                } else {  // have not registered product

                                }
                            } else {
                                Helper.alert(ProductManagementActivity.this, searchProductStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(ProductManagementActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(ProductManagementActivity.this, e, "ProductManagementActivity", "callSearchProduct-OnResponse");
                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(ProductManagementActivity.this, message, Constant.AlertType.Error);

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(ProductManagementActivity.this, e, "ProductManagementActivity", "callSearchProduct");
        }
    }

    private boolean saveProduct(List<SearchProductSto.DataModel.Result> results) {
        try {
            for (SearchProductSto.DataModel.Result result : results) {
                ProductModel productModel = ProductMapper.convertSearchResultToProductModel(result);
                Db.Product.insert(productModel, result.nidProduct);
            }
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "ProductManagementActivity", "saveProduct");
            return true;
        }

    }

    private SearchProductDto createSearchProductDto() {

        SearchProductDto dto = new SearchProductDto();
        dto.terminalCode = mSession.getTerminalId();
        if (mUserTokenId == null) {
            dto.tokenId = mSession.getTokenId();

        } else {
            dto.tokenId = mUserTokenId;

        }
        dto.skipRows = 0;
        dto.takeRows = Constant.Valuse.MAX_ROW_PRODUCT;
        return dto;
    }


}
