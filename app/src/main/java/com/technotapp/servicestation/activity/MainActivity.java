package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MainMenuModel;
import com.technotapp.servicestation.adapter.MainMenuAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.sto.MenuSto;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    GridView gridView;
    ImageButton btnSetting;

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

        Bundle bundle;
        try {
            if (getIntent().hasExtra(Constant.Key.MENU_PACKAGE)) {
                bundle = getIntent().getExtras();
                if (bundle != null) {
                    ArrayList<MenuSto> menus = bundle.getParcelableArrayList(Constant.Key.MENU_PACKAGE);
                    AppMonitor.Log(menus.get(0).messageModel.get(0).errorCode + "");
                }

            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "MainActivity", "loadData");
        }
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
        Intent intent =new Intent(MainActivity.this, PublicServiceActivity.class);
        switch (position) {
            case 0:
                intent.putExtra(Constant.Key.CURRENT_FRAGMENT, Constant.MenuAction.CHARGE);
                break;
            case 4:
                intent.putExtra(Constant.Key.CURRENT_FRAGMENT, Constant.MenuAction.RECEIPT);
                break;
            case 6:
                intent.putExtra(Constant.Key.CURRENT_FRAGMENT, Constant.MenuAction.CARDSERVICE);
                break;
        }

        startActivity(intent);

    }

    byte counter = 0;
    boolean isFirst = true;
    Toast toastMessage;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_btnSetting:
                if (toastMessage!= null) {
                    toastMessage.cancel();
                }

                if (counter == 9) {
                    counter = 0;
                    startActivity(new Intent(MainActivity.this,SettingActivity.class));
                    finish();
                    return;
                }
                else if (counter>3){

                    toastMessage=Toast.makeText(MainActivity.this, "شما "+(9-counter)+" قدمی دسترسی به تنظیمات هستید.", Toast.LENGTH_SHORT);
                    toastMessage.show();

                }

                counter++;
                if (isFirst) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            counter = 0;
                            isFirst=false;
                        }
                    }, 10000);

                }
                break;
        }
    }
}
