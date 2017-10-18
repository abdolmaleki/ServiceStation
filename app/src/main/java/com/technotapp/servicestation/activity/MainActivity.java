package com.technotapp.servicestation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MainMenuModel;
import com.technotapp.servicestation.adapter.MainMenuAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSetting();

        loadData();

        bindView();

        initAdapter();

        clickHandler();

    }

    private void loadSetting() {

    }

    private void loadData() {

    }

    private void initAdapter() {
        ArrayList<MainMenuModel> mainMenuModels = new ArrayList<>();
        mainMenuModels.add(new MainMenuModel("خدمات کارت", R.drawable.ic_ansar_logo));
        mainMenuModels.add(new MainMenuModel("خدمات کارت", R.drawable.ic_ansar_logo));
        mainMenuModels.add(new MainMenuModel("خدمات کارت", R.drawable.ic_ansar_logo));

        MainMenuAdapter menuAdapter = new MainMenuAdapter(this, mainMenuModels);
        gridView.setAdapter(menuAdapter);
    }

    private void bindView() {
        gridView = (GridView) findViewById(R.id.activity_main_grdList);
    }

    private void clickHandler() {

    }


}
