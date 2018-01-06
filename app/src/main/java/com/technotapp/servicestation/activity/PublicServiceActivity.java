package com.technotapp.servicestation.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.fragment.AnsarServiceFragment;
import com.technotapp.servicestation.fragment.CardServiceFragment;
import com.technotapp.servicestation.fragment.ChargeFragment;
import com.technotapp.servicestation.fragment.IMagCard;
import com.technotapp.servicestation.fragment.KhalafiFragment;
import com.technotapp.servicestation.fragment.QrFragment;
import com.technotapp.servicestation.fragment.ReceiptFragment;
import com.technotapp.servicestation.fragment.SubMenuFragment;

public class PublicServiceActivity extends SubMenuActivity implements IPin {

    private int mParentMenuId;
    private MenuModel mCurrentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_service);

        loadSetting();

        loadData();

    }

    private void submitFragment() {
        try {
            Fragment fragment = getFragment(mCurrentMenu.action);
            if (fragment != null) {
                String backStateName = fragment.getClass().getName();
                FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.Key.MENU_ID, mCurrentMenu.id);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.activity_public_service_frame, fragment, backStateName);
                fragmentTransaction.addToBackStack(backStateName);
                fragmentTransaction.commit();
            }

        } catch (Exception e) {
            AppMonitor.reportBug(this, e, "PublicServiceActivity", "submitFragment");
        }
    }

    private Fragment getFragment(String action) {
        switch (action) {
            case Constant.MenuAction.RECEIPT:
                return ReceiptFragment.newInstance();
            case Constant.MenuAction.CHARGE:
                return ChargeFragment.newInstance();
            case Constant.MenuAction.CARDSERVICE:
                return CardServiceFragment.newInstance();
            case Constant.MenuAction.QR_READER:
                return QrFragment.newInstance();
            case Constant.MenuAction.ANSAR:
                return AnsarServiceFragment.newInstance();
            case Constant.MenuAction.KHALAFI:
                return KhalafiFragment.newInstance();
            default:
                Helper.alert(this, "برای این گزینه محتوایی تعریف نشده است", Constant.AlertType.Error);
                finish();
        }
        return null;
    }


    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mParentMenuId = bundle.getInt(Constant.Key.MENU_ID);
            mCurrentMenu = Db.Menu.getMenuById(mParentMenuId);
            submitFragment();
        }
    }

    private void loadSetting() {

    }

    @Override
    public void onPinEntered(String pin) {
        if (mSubmenuController != null) {
            mSubmenuController.onPinEnteredSuccessfully();
        }
        if (mMagCardController != null) {
            mMagCardController.onPinEnteredSuccessfully();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SubMenuFragment) {
            mSubmenuController = (SubMenuFragment) fragment;
        } else if (fragment instanceof IMagCard) {
            mMagCardController = (IMagCard) fragment;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSubmenuController != null) {
            mSubmenuController = null;
        }
    }

}
