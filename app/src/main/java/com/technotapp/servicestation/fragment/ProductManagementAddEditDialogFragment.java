package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;

import butterknife.BindView;

public class ProductManagementAddEditDialogFragment extends DialogFragment implements View.OnClickListener {


    public static ProductManagementAddEditDialogFragment newInstance() {
        ProductManagementAddEditDialogFragment fragment = new ProductManagementAddEditDialogFragment();
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
            AppMonitor.reportBug(e, "ProductManagementAddEditDialogFragment", "onCreateView");
            return null;
        }
    }

    private void initView(View v) {
        EditText edtName=v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtName);
        EditText edtUnit=v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtUnit);
        EditText edtAmount=v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtAmount);
        EditText edtDescription=v.findViewById(R.id.fragment_dialog_product_management_add_edit_edtDescription);
        TextView txtHeader=v.findViewById(R.id.fragment_dialog_product_management_add_edit_txtHeader);
        Button btnConfirm=v.findViewById(R.id.fragment_dialog_product_management_add_edit_btnConfirm);

        btnConfirm.setOnClickListener(this);
    }

    public void show(Activity activity) {
        try {
            this.setCancelable(false);
            this.show(activity.getFragmentManager(), "ProductManagementAddEdit");
        } catch (Exception e) {
            AppMonitor.reportBug(e, "ProductManagementAddEditDialogFragment", "show");
        }
    }


    @Override
    public void onClick(View view) {
        dismiss();
    }
}
