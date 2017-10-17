package com.technotapp.servicestation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.MainMenuAdapter;
import com.technotapp.servicestation.pax.mag.IMagCardCallback;
import com.technotapp.servicestation.pax.mag.MagCard;

public class MainActivity extends AppCompatActivity {
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSetting();

        loadData();

        bindView();

        clickHandler();

        MainMenuAdapter menuAdapter=new MainMenuAdapter(this);
        gridView.setAdapter(menuAdapter);
    }

    private void loadSetting() {

    }

    private void loadData() {

    }

    private void bindView() {
        gridView = (GridView) findViewById(R.id.activity_main_grdList);
    }

    private void clickHandler() {

    }


}
