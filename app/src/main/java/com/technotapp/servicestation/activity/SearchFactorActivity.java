package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ibm.icu.util.PersianCalendar;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.SearchFactorAdapterModel;
import com.technotapp.servicestation.adapter.SearchFactorAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.SearchFactorDto;
import com.technotapp.servicestation.connection.restapi.sto.SearchFactorSto;
import com.technotapp.servicestation.customView.CustomButton;
import com.technotapp.servicestation.customView.CustomTextView;
import com.technotapp.servicestation.mapper.FactorMapper;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFactorActivity extends BaseActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;
    @BindView(R.id.toolbar_img_back)
    LinearLayout back;

    private CustomTextView mET_From_Date;
    private CustomTextView mET_End_Date;
    private CustomButton mBTN_Serach;
    private int mSelectedEditText;
    private SpinKitView mProgressBar;
    private RecyclerView mRecyclerView;
    private CustomTextView mTxt_NotFound;
    private PersianCalendar mFromCalendar = new PersianCalendar();
    private PersianCalendar mEndCalendar = new PersianCalendar();
    private Session mSession;
    private final String mClassName = getClass().getSimpleName();
    private int mSkipRows;
    private int mTakeRows = 5;
    private long mTotalRows = 0;
    private boolean mIsLoading = false;

    private SearchFactorAdapter mAdapter;
    private String mUserTokenId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_factor);
        loadData();
        initView();

    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUserTokenId = bundle.getString(Constant.Key.SUPPORT_TOKEN_ID);
        }
    }

    private void initView() {
        ButterKnife.bind(this);
        back.setOnClickListener(this);
        txtTitle.setText("آرشیو فاکتورها");
        mET_From_Date = findViewById(R.id.activity_search_factor_et_from_date);
        mET_End_Date = findViewById(R.id.activity_search_factor_et_end_date);
        mBTN_Serach = findViewById(R.id.activity_search_factor_btn_search);
        mTxt_NotFound = findViewById(R.id.activity_search_factor_txt_not_found);
        mRecyclerView = findViewById(R.id.activity_search_factor_list);
        mProgressBar = findViewById(R.id.activity_search_factor_progress);
        mBTN_Serach.setOnClickListener(this);
        mBTN_Serach.requestFocus();
        mET_From_Date.setOnClickListener(this);
        mET_End_Date.setOnClickListener(this);
        mSession = Session.getInstance(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(llm);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == back.getId()) {
            finish();
        } else if (id == mET_From_Date.getId()) {
            mSelectedEditText = DateHelper.showDatePicker(this, mET_From_Date);


        } else if (id == mET_End_Date.getId()) {
            mSelectedEditText = DateHelper.showDatePicker(this, mET_End_Date);


        } else if (id == mBTN_Serach.getId()) {
            if (!mIsLoading) {
                mSkipRows = 0;
                mTotalRows = 0;
                callSearchFactor(createDto());
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (mSelectedEditText == mET_From_Date.getId()) {
            mET_From_Date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            mFromCalendar.set(year, monthOfYear, dayOfMonth, 0, 0);
        } else if (mSelectedEditText == mET_End_Date.getId()) {
            mET_End_Date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
            mEndCalendar.set(year, monthOfYear, dayOfMonth, 23, 59);

        }
    }

    private void showNotFoundState() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mTxt_NotFound.setVisibility(View.VISIBLE);
        mIsLoading = false;
    }

    private void showFoundState() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mTxt_NotFound.setVisibility(View.GONE);
        mIsLoading = false;
    }

    private void showLoadMoreState() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mTxt_NotFound.setVisibility(View.GONE);
        mIsLoading = true;
    }


    private void callSearchFactor(SearchFactorDto searchFactorDto) {
        try {
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.SEARCH_Factor).call(this, searchFactorDto, AESsecretKey, "در حال بارگیری اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<SearchFactorSto>>() {
                        }.getType();
                        ArrayList<SearchFactorSto> factorStos = gson.fromJson(jsonResult, listType);
                        if (factorStos != null) {
                            if (factorStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(factorStos.get(0).messageModel.get(0).ver);
                                if (factorStos.get(0).dataModel.get(0).result != null && factorStos.get(0).dataModel.get(0).result.size() > 0) { // have factor
                                    showFoundState();
                                    initAdapter(factorStos.get(0).dataModel.get(0).result);
                                    mSkipRows += mTakeRows;
                                    mTotalRows = factorStos.get(0).dataModel.get(0).dataRecord.get(0).totalRows;
                                } else {  // have not active  menu
                                    showNotFoundState();
                                }
                            } else {
                                Helper.alert(SearchFactorActivity.this, factorStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(SearchFactorActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(SearchFactorActivity.this, e, mClassName, "callSearchFactor-OnResponse");
                        Helper.alert(SearchFactorActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(SearchFactorActivity.this, message, Constant.AlertType.Error);

                }

            });
        } catch (Exception e) {
            AppMonitor.reportBug(SearchFactorActivity.this, e, mClassName, "callSearchFactor");
        }
    }

    private void callLoadMoreFactor(SearchFactorDto searchFactorDto) {
        try {
            showLoadMoreState();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.SEARCH_Factor).call(this, searchFactorDto, AESsecretKey, null, new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<SearchFactorSto>>() {
                        }.getType();
                        ArrayList<SearchFactorSto> factorStos = gson.fromJson(jsonResult, listType);
                        if (factorStos != null) {
                            if (factorStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(factorStos.get(0).messageModel.get(0).ver);
                                if (factorStos.get(0).dataModel.get(0).result != null && factorStos.get(0).dataModel.get(0).result.size() > 0) { // have factor
                                    showFoundState();
                                    addMoreItems(factorStos.get(0).dataModel.get(0).result);
                                    mSkipRows += mTakeRows;
                                } else {
                                    showFoundState();
                                }
                            } else {
                                Helper.alert(SearchFactorActivity.this, factorStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                                showFoundState();
                            }
                        } else {
                            Helper.alert(SearchFactorActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                            showFoundState();

                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(SearchFactorActivity.this, e, mClassName, "callLoadMoreFactor-OnResponse");
                        Helper.alert(SearchFactorActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        showFoundState();
                    }
                }

                @Override
                public void onFail(String message) {
                    showFoundState();
                    Helper.alert(SearchFactorActivity.this, message, Constant.AlertType.Error);
                }

            });
        } catch (Exception e) {
            AppMonitor.reportBug(SearchFactorActivity.this, e, mClassName, "callLoadMoreFactor");
        }
    }

    private void initAdapter(List<SearchFactorSto.DataModel.Result> results) {
        ArrayList<SearchFactorAdapterModel> adapterModels = FactorMapper.convertStoToAdaptrerModel(results);
        mAdapter = new SearchFactorAdapter(this, adapterModels);
        mRecyclerView.setAdapter(mAdapter);
        setLazyLoading();

    }

    private void addMoreItems(List<SearchFactorSto.DataModel.Result> results) {
        ArrayList<SearchFactorAdapterModel> adapterModels = FactorMapper.convertStoToAdaptrerModel(results);
        mAdapter.addNewFactor(adapterModels);
        mAdapter.notifyDataSetChanged();
    }

    private void setLazyLoading() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView.getAdapter() != null) {
                    if (!mIsLoading) {
                        int existItemsCount = recyclerView.getAdapter().getItemCount();
                        if (existItemsCount > 0) {
                            LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                            int currentPosition = llm.findLastVisibleItemPosition();
                            if (currentPosition >= existItemsCount - 1 && currentPosition != mTotalRows - 1) {
                                mIsLoading = true;
                                callLoadMoreFactor(createDto());
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private SearchFactorDto createDto() {
        SearchFactorDto dto = new SearchFactorDto();
        dto.dateFrom = DateHelper.shamsiToMiladiDate(mFromCalendar);
        dto.dateTo = DateHelper.shamsiToMiladiDate(mEndCalendar);
        dto.idMerchant = Long.parseLong(mSession.getMerchantId());
        dto.terminalCode = mSession.getTerminalId();
        if (mUserTokenId == null) {
            dto.tokenId = mSession.getTokenId();

        } else {
            dto.tokenId = mUserTokenId;

        }
        dto.skipRows = mSkipRows;
        dto.takeRows = mTakeRows;
        return dto;
    }
}
