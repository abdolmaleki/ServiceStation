package com.technotapp.servicestation.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.setting.Session;

import net.glxn.qrgen.android.QRCode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class QrFragment extends SubMenuFragment {

    @BindView(R.id.fragment_imgQr)
    ImageView img;
    private Session mSession;

    Button btnConfirm;
    private Unbinder unbinder;
    private final String mClassName = getClass().getSimpleName();

    public static QrFragment newInstance() {
        QrFragment fragment = new QrFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        try {
            setRetainInstance(true);
            setTitle(getString(R.string.QrFragmentTitle));
            mSession = Session.getInstance(getActivity());

            Bitmap myBitmap = QRCode.from(mSession.getHashId()).bitmap();
            img.setImageBitmap(myBitmap);

        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "initView");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
