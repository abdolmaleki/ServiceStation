package com.technotapp.servicestation.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;


public class InputDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String INPUT_DIALOG_MESSAGE = "INPUT_DIALOG_MESSAGE";
    private static final String INPUT_DIALOG_COLOR = "INPUT_DIALOG_COLOR";
    private static final String INPUT_DIALOG_TYPE = "INPUT_DIALOG_TYPE";
    private EditText mETPassword;
    private TextView mTV_Message;
    private LinearLayout mPanel_frame;
    private OnInputDialogClick mOnInputDialogClick;
    private String mMessage;
    private Button mBTN_Accept;
    private Button mBTN_Cancel;
    private int mPanelColor;
    private int mInputType;

    public static InputDialogFragment newInstance() {
        InputDialogFragment fragment = new InputDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static InputDialogFragment newInstance(String message, int color, int inputType) {
        InputDialogFragment fragment = new InputDialogFragment();
        Bundle args = new Bundle();
        args.putString(INPUT_DIALOG_MESSAGE, message);
        args.putInt(INPUT_DIALOG_COLOR, color);
        args.putInt(INPUT_DIALOG_TYPE, inputType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPanelColor = bundle.getInt(INPUT_DIALOG_COLOR, -1);
            mInputType = bundle.getInt(INPUT_DIALOG_TYPE, -1);
            mMessage = bundle.getString(INPUT_DIALOG_MESSAGE, null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dialog_input, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_transparent);
        mETPassword = rootView.findViewById(R.id.fragment_dialog_input_et_password);
        mBTN_Accept = rootView.findViewById(R.id.fragment_dialog_input_btn_accept);
        mBTN_Cancel = rootView.findViewById(R.id.fragment_dialog_input_btn_cancel);
        mTV_Message = rootView.findViewById(R.id.fragment_dialog_input_tv_message);
        mPanel_frame = rootView.findViewById(R.id.fragment_dialog_input_panel);
        rootView.findViewById(R.id.fragment_dialog_input_btn_accept).setOnClickListener(this);
        rootView.findViewById(R.id.fragment_dialog_input_btn_cancel).setOnClickListener(this);

        initView();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setRetainInstance(true);
    }

    private void initView() {
        try {
            if (mMessage != null) {
                mTV_Message.setText(mMessage);
            }
            if (mPanelColor != -1) {
                if (mPanelColor == Color.BLUE) {
                    mPanel_frame.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_lyr_dialog_input_purple));
                    mBTN_Accept.setTextColor(getResources().getColor(R.color.purple));
                    mBTN_Cancel.setTextColor(getResources().getColor(R.color.purple));

                    mETPassword.requestFocus();
                }
            }

            if (mInputType != -1) {
                mETPassword.setInputType(mInputType);
                if (mInputType == (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    mETPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mETPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(), e, "SuccessfulDialogFragment", "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_dialog_input_btn_cancel) {
            dismiss();
        } else if (v.getId() == R.id.fragment_dialog_input_btn_accept) {
            mOnInputDialogClick.onAccept(mETPassword.getText().toString());
            dismiss();
        }
    }

    public void setOnInputDialogClickListener(OnInputDialogClick onInputDialogClick) {
        mOnInputDialogClick = onInputDialogClick;
    }

    public interface OnInputDialogClick {
        void onAccept(String password);
    }

}
