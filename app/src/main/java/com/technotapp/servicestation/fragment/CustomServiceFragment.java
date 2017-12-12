package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.adapter.ProductFactorAdapter;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.ProductModel;
import com.technotapp.servicestation.entity.FactorMaker;
import com.technotapp.servicestation.mapper.ProductMapper;

import java.util.ArrayList;
import java.util.List;

public class CustomServiceFragment extends Fragment implements SearchView.OnQueryTextListener, ProductFactorAdapter.OnFactorChangeListener {

    private Activity mActivity;
    private GridView mGVProduct;
    private SearchView mSearchView;
    private TextView mTV_totalPrice;
    private FactorMaker mFactorMaker;


    public static CustomServiceFragment newInstance() {
        CustomServiceFragment fragment = new CustomServiceFragment();
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
        View rooView = inflater.inflate(R.layout.fragment_custom_services, container, false);

        loadData();

        initView(rooView);

        initAdapter();

        return rooView;
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
            setRetainInstance(true);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CustomServiceFragment", "initView");
        }
    }

    private void initAdapter() {
        try {
            List<ProductModel> productModels = Db.Product.getAll();
            ArrayList<ProductFactorAdapterModel> adapterModels = ProductMapper.convertModelToFactorAdapterModel(productModels);
            ProductFactorAdapter adapter = new ProductFactorAdapter(mActivity, adapterModels, this);
            mGVProduct.setAdapter(adapter);
            mGVProduct.setTextFilterEnabled(true);

        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductManagementFragment", "initAdapter");
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
}
