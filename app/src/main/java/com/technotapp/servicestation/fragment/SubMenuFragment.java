package com.technotapp.servicestation.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.activity.IToolBar;

public class SubMenuFragment extends Fragment implements IMagCard {

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
            //todo change this frame
            fragmentTransaction.replace(R.id.activity_public_service_frame, fragment, backStateName);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        } catch (Exception e) {
            AppMonitor.reportBug(getActivity(),e, "SubMenuFragment", "submitFragment");
        }
    }


    public void setTitle(String title) {
        mToolbarController.setTitle(title);
    }


    @Override
    public void onPinEnteredSuccessfully() {

    }
}

