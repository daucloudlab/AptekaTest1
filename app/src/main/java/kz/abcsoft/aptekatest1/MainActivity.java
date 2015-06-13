package kz.abcsoft.aptekatest1;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


import java.util.List;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar ;
    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar) ;
        toolbar.setTitle(R.string.main_toolbar_title);

        initNavigationDrawer();

        fm.beginTransaction().add(R.id.main_activity_container, new MainFragment()).commit() ;

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Apteka") ;
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                try{
                    ParseObject.pinAll(list);
                }catch (ParseException e1){
                    e1.printStackTrace();
                }
            }
        });

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Medikament") ;
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                try{
                    ParseObject.pinAll(list);
                }catch (ParseException e2){
                    e2.printStackTrace();
                }
            }
        });

    }


    private void initNavigationDrawer(){

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.material_background)
                .build() ;

        Drawer drawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.nav_menu_item_apteki)
                                .withIdentifier(1)
                                .withIcon(R.drawable.ic_clinic_room_24),

                        new DividerDrawerItem(),

                        new PrimaryDrawerItem()
                                .withName(R.string.nav_menu_item_near_apteki)
                                .withIdentifier(2)
                                .withIcon(R.drawable.ic_room_black_24dp),

                        new DividerDrawerItem(),

                        new PrimaryDrawerItem()
                                .withName(R.string.nav_menu_item_search_medikament)
                                .withIdentifier(3)
                                .withIcon(R.drawable.ic_pill_24)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                        switch (iDrawerItem.getIdentifier()) {
                            case 1:
                                fm.beginTransaction().replace(R.id.main_activity_container, new AptekaListFragment())
                                        .commit();
                                toolbar.setTitle(R.string.search_apteks_title);
                                return false;
                            case 2:
//                                Intent intent2 = new Intent(MainActivity.this, MapsActivity.class) ;
//                                startActivity(intent2);

                                return false;
                            case 3:
                                fm.beginTransaction().replace(R.id.main_activity_container, new MainFragment())
                                        .commit();
                                toolbar.setTitle(R.string.main_toolbar_title);
                                return false;
                        }
                        return false;
                    }
                })
                .build() ;
    }

}
