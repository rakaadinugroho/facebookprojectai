package com.rakaadinugroho.sealmood.core.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.core.adapters.TabDashboard;

import io.realm.Realm;

public class DashboardActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        mToolbar    = (Toolbar) findViewById(R.id.toolbar);
        mViewPager  = (ViewPager) findViewById(R.id.pagerdashboard);
        mTabLayout  = (TabLayout) findViewById(R.id.tabdashboard);
        setUpToolbar();
        setUpComponent();
    }

    private void setUpComponent() {
        TabDashboard tabDashboard   = new TabDashboard(getSupportFragmentManager(), this);
        mViewPager.setAdapter(tabDashboard);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    protected void setUpToolbar() {
        if (mToolbar != null){
            setSupportActionBar(mToolbar);
            this.setTitle(getResources().getString(R.string.app_name));
            /*
            Create Back Apps
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
             */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete_all_category:{
                final Realm realm   = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
