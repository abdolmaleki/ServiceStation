package com.technotapp.servicestation.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.IToolBar;

public class SubMenuFragment extends Fragment {

    protected Context mActivity;
    protected IToolBar mToolbarController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = context;
        if (context instanceof IToolBar) {
            mToolbarController = (IToolBar) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;

    }

    public void submitFragment(Fragment fragment) {
        try {
            String backStateName = fragment.getClass().getName();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.replace(R.id.activity_card_service_frame, fragment).commit();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceActivity", "submitFragment");
        }
    }

    public void submitKeypadFragment(Fragment fragment, int frameID) {
        try {
            String backStateName = fragment.getClass().getName();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.replace(frameID, fragment).commit();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceActivity", "submitFragment");
        }
    }

    public void setTitle(String title) {
        mToolbarController.setTitle(title);
    }

    public void onPinEnteredSuccessfully() {
    }
}

