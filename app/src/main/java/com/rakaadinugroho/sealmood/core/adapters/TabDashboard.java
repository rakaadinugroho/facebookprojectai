package com.rakaadinugroho.sealmood.core.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rakaadinugroho.sealmood.R;
import com.rakaadinugroho.sealmood.core.activities.dashboard.Calender;
import com.rakaadinugroho.sealmood.core.activities.dashboard.Dashboard;
import com.rakaadinugroho.sealmood.core.activities.dashboard.Stats;

/**
 * Created by Raka Adi Nugroho on 4/4/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class TabDashboard extends FragmentPagerAdapter {
    Context context;
    final int TOTAL_TAB    = 3;
    public TabDashboard(FragmentManager fm, Context context) {
        super(fm);
        this.context    = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment   = null;
        switch (position){
            case 0:
                fragment    = new Dashboard();
                break;
            case 1:
                fragment    = new Stats();
                break;
            case 2:
                fragment    = new Calender();
                break;
            default:
                fragment    = null;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_TAB;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: {
                return context.getResources().getString(R.string.tab_dash);
            }
            case 1: {
                return context.getResources().getString(R.string.tab_stats);
            }
            case 2: {
                return context.getResources().getString(R.string.tab_calendar);
            }
        }
        return null;
    }
}
