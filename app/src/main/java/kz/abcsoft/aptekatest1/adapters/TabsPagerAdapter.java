package kz.abcsoft.aptekatest1.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import kz.abcsoft.aptekatest1.categoryfragments.Fragment1;
import kz.abcsoft.aptekatest1.categoryfragments.Fragment2;
import kz.abcsoft.aptekatest1.categoryfragments.Fragment3;
import kz.abcsoft.aptekatest1.categoryfragments.Fragment4;
import kz.abcsoft.aptekatest1.categoryfragments.Fragment5;
import kz.abcsoft.aptekatest1.categoryfragments.Fragment6;
import kz.abcsoft.aptekatest1.categoryfragments.Fragment7;
import kz.abcsoft.aptekatest1.categoryfragments.Fragment8;


public class TabsPagerAdapter extends FragmentPagerAdapter {
    String [] tabsName = new String[] {
            "Лекарственные препараты",
            "Травы, фито чаи",
            "Добавки и витамины",
            "Интим и здоровье",
            "Медтехника",
            "Для мам и детей",
            "Красота и уход",
            "Разное"
    } ;




    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new Fragment3();
            case 3:
                return new Fragment4() ;
            case 4:
                return new Fragment5() ;
            case 5:
                return new Fragment6() ;
            case 6:
                return new Fragment7() ;
            case 7:
                return new Fragment8() ;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 8;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsName[position] ;
    }
}
