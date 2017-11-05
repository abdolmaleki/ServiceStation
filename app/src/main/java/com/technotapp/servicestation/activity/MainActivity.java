package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MainMenuModel;
import com.technotapp.servicestation.adapter.MainMenuAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSetting();

        loadData();

        bindView();

        initAdapter();

    }

    private void loadSetting() {

    }

    private void loadData() {

    }

    private void initAdapter() {
        try {
            ArrayList<MainMenuModel> mainMenuModels = new ArrayList<>();
            //TODO set main menu item title and icons
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_buy_charge), R.drawable.ic_charg));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_buy_ticket), R.drawable.ic_ticket));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_services_and_goods), R.drawable.ic_shopping));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_simcard_services), R.drawable.ic_simcard));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_pay_bills), R.drawable.ic_recipt));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_insurance_services), R.drawable.ic_insurance));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_card_services), R.drawable.ic_bank_card));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_ansar_bank_services), R.drawable.ic_ansar_logo));
            mainMenuModels.add(new MainMenuModel(getString(R.string.MainActivity_buy_internet_packages), R.drawable.ic_net));

            MainMenuAdapter menuAdapter = new MainMenuAdapter(this, mainMenuModels);
            gridView.setAdapter(menuAdapter);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MainActivity", "initAdapter");
        }
    }

    private void bindView() {
        try {
            gridView = (GridView) findViewById(R.id.activity_main_grdList);
            gridView.setOnItemClickListener(this);
        }catch (Exception e){
            AppMonitor.reportBug(e, "MainActivity", "bindView");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 6:
                startActivity(new Intent(MainActivity.this, CardServiceActivity.class));
                break;
        }
    }
}
