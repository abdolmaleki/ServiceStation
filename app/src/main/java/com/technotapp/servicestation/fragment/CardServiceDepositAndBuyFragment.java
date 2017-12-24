package com.technotapp.servicestation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;

import java.util.Locale;

public class CardServiceDepositAndBuyFragment extends SubMenuFragment implements View.OnClickListener, View.OnLongClickListener {
    private TransactionDataModel transactionDataModel;
    private TextView tvAmount;
    private boolean isDeposit;
    private Button btnZero;
    private Button btnTripleZero;

    public static CardServiceDepositAndBuyFragment newInstance() {
        CardServiceDepositAndBuyFragment fragment = new CardServiceDepositAndBuyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {


            View rootView = inflater.inflate(R.layout.fragment_card_service_deposit_and_buy, container, false);
            loadData();
            initView(rootView);

            return rootView;
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositAndBuyFragment", "onCreateView");
            return null;
        }
    }

    private void loadData() {
        try {
            Bundle bundle = getArguments();
            isDeposit = bundle.getBoolean("isDeposit");
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositAndBuyFragment", "loadData");
        }
    }

    private void initView(View v) {
        try {
            tvAmount = (TextView) v.findViewById(R.id.fragment_card_service_buy_txtAmount);
            if (isDeposit) {
                setTitle("واریز");
            } else {
                setTitle("خرید");
            }

            btnZero = v.findViewById(R.id.fragment_keypad_triple_zero);
            btnTripleZero = v.findViewById(R.id.fragment_keypad_zero);
            btnZero.setEnabled(false);
            btnTripleZero.setEnabled(false);
            (v.findViewById(R.id.fragment_card_service_buy_btn)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_one)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_two)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_three)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_four)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_five)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_six)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_seven)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_eight)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_nine)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_triple_zero)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_zero)).setOnClickListener(this);
            (v.findViewById(R.id.fragment_keypad_backspace)).setOnClickListener(this);

            (v.findViewById(R.id.fragment_keypad_backspace)).setOnLongClickListener(this);

        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositAndBuyFragment", "initView");
        }


        tvAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            private String current = "";

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    tvAmount.removeTextChangedListener(this);
                    if (!tvAmount.getText().toString().trim().equals("")) {
                        if (!s.toString().equals(current)) {

                            String cleanString = s.toString().replaceAll("[,]", "");
                            String formatted = String.format(Locale.CANADA, "%,d", Long.parseLong(cleanString));
                            current = formatted;
                            tvAmount.setText(formatted);
                        }
                    }
                    tvAmount.addTextChangedListener(this);
                } catch (Exception e) {
                    AppMonitor.reportBug(e, "CardServiceDepositAndBuyFragment", "afterTextChanged");
                }
            }
        });


    }


    private void startMagCard() {
        try {

            MagCard magCard = MagCard.getInstance();
            magCard.start(getActivity(), new IMagCardCallback() {
                @Override
                public void onFail() {

                }

                @Override
                public void onSuccessful(String track1, String track2, String track3) {

                    initTransactionModel();

                    if (track1 != null && !track1.equals("")) {
                        transactionDataModel.setPanNumber(track1.substring(1, 17));
                    } else if (track2 != null && !track2.equals("")) {
                        transactionDataModel.setPanNumber(track2.substring(0, 16));
                    }
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositAndBuyFragment", "startMagCard");
        }
    }

    private void initTransactionModel() {
        try {

            transactionDataModel = new TransactionDataModel();
            //TODO setTerminalID
            transactionDataModel.setTerminalID("23801101741");
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositAndBuyFragment", "initTransactionModel");
        }
    }

    @Override
    public void onPinEnteredSuccessfully() {
        super.onPinEnteredSuccessfully();
        try {
            if (isDeposit) {
                TransactionHelper.sendRequest(getActivity(), Constant.RequestMode.DEPOSIT, transactionDataModel, tvAmount.getText().toString().replaceAll(",", ""), new TransactionHelper.TransactionResultListener() {
                    @Override
                    public void onSuccessfull() {

                    }

                    @Override
                    public void onFail() {

                    }
                });
            } else {
                TransactionHelper.sendRequest(getActivity(), Constant.RequestMode.BUY, transactionDataModel, tvAmount.getText().toString().replaceAll(",", ""), new TransactionHelper.TransactionResultListener() {
                    @Override
                    public void onSuccessfull() {

                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceDepositAndBuyFragment", "onPinEnteredSuccessfully");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_card_service_buy_btn:
                startMagCard();
                break;

            case R.id.fragment_keypad_one:
                tvAmount.append("1");
                break;
            case R.id.fragment_keypad_two:
                tvAmount.append("2");
                break;
            case R.id.fragment_keypad_three:
                tvAmount.append("3");
                break;

            case R.id.fragment_keypad_four:
                tvAmount.append("4");
                break;
            case R.id.fragment_keypad_five:
                tvAmount.append("5");
                break;
            case R.id.fragment_keypad_six:
                tvAmount.append("6");
                break;

            case R.id.fragment_keypad_seven:
                tvAmount.append("7");
                break;
            case R.id.fragment_keypad_eight:
                tvAmount.append("8");
                break;
            case R.id.fragment_keypad_nine:
                tvAmount.append("9");
                break;

            case R.id.fragment_keypad_triple_zero:
                tvAmount.append("000");
                break;
            case R.id.fragment_keypad_zero:
                tvAmount.append("0");
                break;
            case R.id.fragment_keypad_backspace:
                if (tvAmount.getText().toString().length() <= 1) {
                    btnZero.setEnabled(false);
                    btnTripleZero.setEnabled(false);
                }
                if (tvAmount.getText().toString().length() > 0) {
                    tvAmount.setText(tvAmount.getText().toString().substring(0, tvAmount.getText().toString().length() - 1));
                }
                break;
        }

        if (!tvAmount.getText().toString().isEmpty()) {
            btnZero.setEnabled(true);
            btnTripleZero.setEnabled(true);
        }


    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.fragment_keypad_backspace) {
            btnZero.setEnabled(false);
            btnTripleZero.setEnabled(false);
            tvAmount.setText("");
        }
        return false;
    }
}
