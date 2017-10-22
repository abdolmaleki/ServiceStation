package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.SubMenuModel;
import com.technotapp.servicestation.adapter.SubMenuAdapter;
import com.technotapp.servicestation.fragment.CardServiceBalanceFragment;
import com.technotapp.servicestation.fragment.CardServiceFragment;

import java.util.ArrayList;

public class CardServiceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
//    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_service);

        loadSetting();

        loadData();

        bindView();

        submitFragment();

    }

    private void submitFragment() {
        try {


            CardServiceFragment fragment = CardServiceFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_card_service_frame, fragment).commit();
        }catch (Exception e){
            AppMonitor.reportBug(e,"CardServiceActivity","submitFragment");
        }
    }

    private void bindView() {
//
//        btnBack.setOnClickListener(this);
//        btnBack = (ImageButton) findViewById(R.id.activity_card_service_back);


    }


    private void loadData() {

    }



    private void loadSetting() {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 3:
                startActivity(new Intent(CardServiceActivity.this, CardServiceBalanceFragment.class));
                break;
//            case 1:
//                startActivity(new Intent(CardServiceActivity.this, .class));
//                break;
//            case 2:
//                startActivity(new Intent(CardServiceActivity.this, .class));
//                break;
//            case 3:
//                startActivity(new Intent(CardServiceActivity.this, .class));
//                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_card_service_back:
                break;
        }
    }
}
