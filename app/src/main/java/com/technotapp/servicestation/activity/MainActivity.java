package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.UpdateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.MainMenuPageAdapter;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.fragment.MainGridFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_viewPager)
    ViewPager viewPager;
    @BindView(R.id.activity_main_pagerIndicator)
    InkPageIndicator mIndicator;

    private MainMenuPageAdapter mPagerAdapter;
    private Toast mToastMessage;
    private byte mCounter = 0;
    private boolean mIsFirst = true;
    private final String mClassName = getClass().getSimpleName();
    private Context mContext;

    public static final int NUMBER_OF_ITEMS = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSetting();

        initDb();

        loadData();

        initView();

        initAdapter();

    }

    private void initDb() {
        Db.init();
    }

    private void loadSetting() {
        UpdateHelper.checkNeedingUpdate(this);
    }

    private void loadData() {

    }

    //create gridViewPager
    private void initAdapter() {
        try {
            RealmResults<MenuModel> models = Db.Menu.getMainMenu();
            Iterator<MenuModel> it;
            List<MainGridFragment> gridFragments = new ArrayList<>();
            it = models.iterator();
            while (it.hasNext()) {
                ArrayList<MenuAdapterModel> imLst = new ArrayList<>();

                for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
                    if (it.hasNext()) {
                        MenuAdapterModel itm = new MenuAdapterModel(it.next());
                        imLst.add(itm);
                    } else break;
                }
                gridFragments.add(new MainGridFragment(mContext, imLst));
            }
            mPagerAdapter = new MainMenuPageAdapter(getSupportFragmentManager(), gridFragments);
            viewPager.setAdapter(mPagerAdapter);
            mIndicator.setViewPager(viewPager);
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "initAdapter");
        }
    }

    private void initView() {
        try {
            ButterKnife.bind(this);
            mContext = MainActivity.this;

        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "initView");
        }
    }

    //open settingActivity after 10th click on logo
    public void openSettingMenu(View view) {
        try {
            if (mToastMessage != null) {
                mToastMessage.cancel();
            }

            if (mCounter == 9) {
                mCounter = 0;
                startActivity(new Intent(mContext, SettingActivity.class));
                finish();
                return;
            } else if (mCounter > 3) {
                mToastMessage = Toast.makeText(mContext, "شما " + (9 - mCounter) + " قدمی دسترسی به تنظیمات هستید.", Toast.LENGTH_SHORT);
                mToastMessage.show();
            }
            mCounter++;
            if (mIsFirst) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mCounter = 0;
                        mIsFirst = false;
                    }
                }, 10000);

            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "openSettingMenu");
        }
    }


}
