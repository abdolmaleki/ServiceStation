package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.MenuAdapter;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;

import java.util.ArrayList;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    GridView gridView;
    ImageButton btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSetting();

        initDb();

        loadData();

        bindView();

        initAdapter();

    }

    private void initDb() {
        Db.init();
    }

    private void loadSetting() {

    }

    private void loadData() {

    }

    private void initAdapter() {
        try {
            ArrayList<MenuAdapterModel> mainMenuAdapterModels = new ArrayList<>();
            RealmResults<MenuModel> models = Db.Menu.getMainMenu();
            for (MenuModel menuModel : models) {
                mainMenuAdapterModels.add(new MenuAdapterModel(menuModel));
            }
            //TODO set main menu item title and icons
            MenuAdapter menuAdapter = new MenuAdapter(this, mainMenuAdapterModels);
            gridView.setAdapter(menuAdapter);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MainActivity", "initAdapter");
        }
    }

    private void bindView() {
        try {
            btnSetting = (ImageButton) findViewById(R.id.activity_main_btnSetting);
            gridView = (GridView) findViewById(R.id.activity_main_grdList);
            gridView.setOnItemClickListener(this);
            btnSetting.setOnClickListener(this);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MainActivity", "bindView");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MenuModel selectedMenuItem = Db.Menu.getMenuById(id);
        Helper.lunchActivity(this, selectedMenuItem.controller, (int) id);
    }

    byte counter = 0;
    boolean isFirst = true;
    Toast toastMessage;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_btnSetting:
                openSettingMenu();
                break;
        }
    }

    private void openSettingMenu() {
        if (toastMessage != null) {
            toastMessage.cancel();
        }

        if (counter == 9) {
            counter = 0;
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            finish();
            return;
        } else if (counter > 3) {

            toastMessage = Toast.makeText(MainActivity.this, "شما " + (9 - counter) + " قدمی دسترسی به تنظیمات هستید.", Toast.LENGTH_SHORT);
            toastMessage.show();

        }

        counter++;
        if (isFirst) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    counter = 0;
                    isFirst = false;
                }
            }, 10000);

        }
    }
}
