package com.technotapp.servicestation.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.AddFactorDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.FactorModel;
import com.technotapp.servicestation.enums.PaymentType;
import com.technotapp.servicestation.mapper.FactorMapper;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;
import com.technotapp.servicestation.pax.printer.PrintFactory;
import com.technotapp.servicestation.pax.printer.Printable;
import com.technotapp.servicestation.pax.printer.PrinterHelper;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class PaymentMenuDialog extends DialogFragment implements View.OnClickListener, IMagCard {

    private FactorModel mFactorModel;
    private Session mSession;
    private TransactionDataModel transactionDataModel;
    private PaymentType mPaymentType;
    private PaymentResultListener mPaymentResultListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            long factorId = bundle.getLong(Constant.Key.FACTOR_ID, -1);
            if (factorId != -1) {
                mFactorModel = Db.Factor.getFactorById(factorId);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_dialog_payment_menu, container);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_btn_swipe_card);
            }
            initView(view);
            return view;

        } catch (Exception e) {
            AppMonitor.reportBug(e, "PaymentMenuDialog", "onCreateView");
            return null;
        }
    }

    private void initView(View view) {
        view.findViewById(R.id.fragment_dialog_payment_menu_btn_cash).setOnClickListener(this);
        view.findViewById(R.id.fragment_dialog_payment_menu_btn_ewallet).setOnClickListener(this);
        mSession = Session.getInstance(getActivity());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fragment_dialog_payment_menu_btn_cash:
                cashPayment(mFactorModel.getTotalPrice());
                break;
            case R.id.fragment_dialog_payment_menu_btn_ewallet:
                ewalletPayment(mFactorModel.getTotalPrice());
                break;
        }

    }

    private void ewalletPayment(long factorTotalPrice) {
        try {
            mPaymentType = PaymentType.EWALLET;
            MagCard magCard = MagCard.getInstance();
            magCard.start(getActivity(), new IMagCardCallback() {
                @Override
                public void onFail() {

                }

                @Override
                public void onSuccessful(String track1, String track2, String track3) {
                    transactionDataModel = new TransactionDataModel();
                    transactionDataModel.setTerminalID(mSession.getTerminalId());

                    if (track1 != null && !track1.equals("")) {
                        transactionDataModel.setPanNumber(track1.substring(1, 17));
                    } else if (track2 != null && !track2.equals("")) {
                        transactionDataModel.setPanNumber(track2.substring(0, 16));
                    }
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "PaymentMenuDialog", "startMagCard");
        }
    }


    @Override
    public void onPinEnteredSuccessfully() {
        if (mFactorModel.getProductModels() != null && mFactorModel.getProductModels().size() > 0) {
            callSubmitFactor();

        } else {
            mPaymentResultListener.onDone(transactionDataModel);
        }
    }

    private void cashPayment(long factorTotalPrice) {
        try {
            mPaymentType = PaymentType.CASH;
            CashPaymentConfirmDialog cashPaymentConfirmDialog = CashPaymentConfirmDialog.newInstance(factorTotalPrice);
            cashPaymentConfirmDialog.show(getActivity().getFragmentManager(), "cashPaymentConfirmDialog");
            cashPaymentConfirmDialog.setmOnCashPaymentDialogClick(new CashPaymentConfirmDialog.OnCashPaymentDialogClick() {
                @Override
                public void onAccept() {
                    if (mFactorModel.getProductModels() != null && mFactorModel.getProductModels().size() > 0) { // must submit factor
                        callSubmitFactor();
                    } else {
                        mPaymentResultListener.onDone(null);
                    }
                    cashPaymentConfirmDialog.dismiss();
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "PaymentMenuDialog", "cashPayment");
        }

    }

    public void setOnPaymentResultListener(PaymentResultListener resultListener) {
        mPaymentResultListener = resultListener;
    }

    private void callSubmitFactor() {
        try {
            AddFactorDto dto = createAddFactorDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.SUBMIT_FACTOR).call(getActivity(), dto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
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
                                Helper.alert(getActivity(), "پرداخت با موفقیت انجام شد", Constant.AlertType.Success);

                                if (mPaymentType == PaymentType.EWALLET) {
                                    TransactionHelper.sendRequest(getActivity(), Constant.RequestMode.BUY, transactionDataModel, String.valueOf(mFactorModel.getTotalPrice()), new TransactionHelper.TransactionResultListener() {
                                        @Override
                                        public void onSuccessfull() {
                                            mPaymentResultListener.onDone(transactionDataModel);
                                        }

                                        @Override
                                        public void onFail() {
                                            mPaymentResultListener.onFail();
                                        }
                                    });
                                } else if (mPaymentType == PaymentType.CASH) {
                                    Printable printable = PrintFactory.getPrintContent(Printable.CASH);
                                    PrinterHelper.getInstance().startPrint(getActivity(), printable.getContent(getActivity(), mSession.getShopName(), mSession.getTelephone(), "1475478589", mSession.getTerminalId(), DateHelper.getGregorianDateTime("HH:mm:ss"), DateHelper.getShamsiDate(), String.valueOf(mFactorModel.getTotalPrice())));

                                }

                            } else {
                                Helper.alert(getActivity(), sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(getActivity(), getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(e, "PaymentMenuDialog", "callSubmitFactor");
                        Helper.alert(getActivity(), getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }

                    closeDialog();
                }

                @Override
                public void onFail() {
                    Helper.alert(getActivity(), getString(R.string.serverConnectingError), Constant.AlertType.Error);
                    closeDialog();
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "PaymentMenuDialog", "callSubmitFactor");
        }
    }

    private void closeDialog() {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    private AddFactorDto createAddFactorDto() {
        return FactorMapper.convertModelToDto(getActivity(), mFactorModel);
    }

    public interface PaymentResultListener {
        void onDone(TransactionDataModel transactionDataModel);

        void onFail();

        void onCancel();
    }
}
