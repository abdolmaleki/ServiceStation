package com.technotapp.servicestation.fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.InternetPackageModel;
import com.technotapp.servicestation.adapter.InternetPackageAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.GetInternetPackageDto;
import com.technotapp.servicestation.connection.restapi.sto.InternetPackageSto;
import com.technotapp.servicestation.mapper.InternetPackageMapper;
import com.technotapp.servicestation.setting.Session;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class InternetPackageListDialogFragment extends DialogFragment implements View.OnClickListener {

    Session mSession;
    private int mOperatorType;
    private int mSimcardType;
    private InternetPackageListener mInternetPackageListener;
    private Activity mActivity;
    private ProgressBar progressBar;
    private ListView mPackageList;

    public static InternetPackageListDialogFragment newInstance(int operatorType, int simcardType) {
        InternetPackageListDialogFragment fragment = new InternetPackageListDialogFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Key.OPERATOR_TYPE, operatorType);
        args.putInt(Constant.Key.SIMCARD_TYPE, simcardType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mOperatorType = bundle.getInt(Constant.Key.OPERATOR_TYPE);
            mSimcardType = bundle.getInt(Constant.Key.SIMCARD_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        View rootView = inflater.inflate(R.layout.fragment_dialog_internet_package_list, container, false);
        initView(rootView);

        callGetInternetPackages();

        return rootView;
    }

    private void initView(View rootView) {
        mSession = Session.getInstance(getActivity());
        mPackageList = rootView.findViewById(R.id.fragment_dialog_internet_package_list_listview);
        progressBar = rootView.findViewById(R.id.fragment_dialog_internet_package_list_progress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {

    }

    public void show(Activity activity, InternetPackageListener internetPackageListener) {
        mInternetPackageListener = internetPackageListener;
        this.show(activity.getFragmentManager(),getClass().getName());
    }

    public interface InternetPackageListener {
        void onPackageSelected(InternetPackageModel model);
    }


    private void callGetInternetPackages() {

        try {
            GetInternetPackageDto dto = createDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.GET_INTERNET_PACKAGE).call(mActivity, dto, AESsecretKey, "در حال دریافت بسته ها", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        InternetPackageSto internetPackageSto = gson.fromJson(jsonResult, InternetPackageSto.class);
                        if (internetPackageSto != null) {
                            if (Integer.parseInt(internetPackageSto.errorCode) == Constant.Api.ErrorCode.Successfull) {
                                if (internetPackageSto.internetPackListResult != null && internetPackageSto.internetPackListResult.size() > 0) { // have package
                                    progressBar.setVisibility(View.GONE);
                                    mPackageList.setVisibility(View.VISIBLE);
                                    initAdapter(internetPackageSto.internetPackListResult);
                                } else {  // have not registered acount
                                    Helper.alert(mActivity, "بسته ای ثبت نشده است", Constant.AlertType.Error);
                                    closeDialog();
                                }
                            } else {
                                Helper.alert(mActivity, internetPackageSto.errorString, Constant.AlertType.Error);
                                closeDialog();

                            }
                        } else {
                            Helper.alert(mActivity, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                            closeDialog();
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(mActivity, e, "InternetPackageListDialogFragment", "callGetInternetPackages" + "-OnResponse");
                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(mActivity, message, Constant.AlertType.Error);
                    closeDialog();
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(mActivity, e, "InternetPackageListDialogFragment", "callGetInternetPackages");
            closeDialog();
        }
    }

    private void closeDialog() {
        dismiss();
    }

    private void initAdapter(List<InternetPackageSto.InternetPackListResult> internetPackListResult) {
        ArrayList<InternetPackageModel> adapterModels = InternetPackageMapper.convertStoToAdapterModel(internetPackListResult,mSimcardType);
        InternetPackageAdapter adapter = new InternetPackageAdapter(mActivity, adapterModels, new InternetPackageListener() {
            @Override
            public void onPackageSelected(InternetPackageModel model) {
                mInternetPackageListener.onPackageSelected(model);
                closeDialog();
            }
        });
        mPackageList.setAdapter(adapter);
    }

    private GetInternetPackageDto createDto() {
        GetInternetPackageDto dto = new GetInternetPackageDto();
        dto.operatorId = mOperatorType;
        dto.terminalCode = mSession.getTerminalId();
        dto.tokenId = mSession.getTokenId();
        return dto;
    }


}
