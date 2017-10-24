package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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
        ArrayList<MainMenuModel> mainMenuModels = new ArrayList<>();
    //TODO set main menu item title and icons
        mainMenuModels.add(new MainMenuModel("خرید شارژ", R.drawable.ic_charg));
        mainMenuModels.add(new MainMenuModel("خرید بلیط", R.drawable.ic_ticket));
        mainMenuModels.add(new MainMenuModel("خدمات و کالاها", R.drawable.ic_shopping));
        mainMenuModels.add(new MainMenuModel("شارژ سیم کارت", R.drawable.ic_simcard));
        mainMenuModels.add(new MainMenuModel("پرداخات قبوض", R.drawable.ic_recipt));
        mainMenuModels.add(new MainMenuModel("خدمات بیمه", R.drawable.ic_insurance));
        mainMenuModels.add(new MainMenuModel("خدمات کارت", R.drawable.ic_bank_card));
        mainMenuModels.add(new MainMenuModel("خدمات بانک انصار", R.drawable.ic_ansar_logo));
        mainMenuModels.add(new MainMenuModel("خرید بسته های اینترنتی", R.drawable.ic_net));

        MainMenuAdapter menuAdapter = new MainMenuAdapter(this, mainMenuModels);
        gridView.setAdapter(menuAdapter);
    }

    private void bindView() {
        gridView = (GridView) findViewById(R.id.activity_main_grdList);
        gridView.setOnItemClickListener(this);
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
