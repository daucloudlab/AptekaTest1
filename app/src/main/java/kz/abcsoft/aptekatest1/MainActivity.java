package kz.abcsoft.aptekatest1;

import android.app.Dialog;
import android.app.DialogFragment;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

import kz.abcsoft.aptekatest1.maps.MapsActivity;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "APTEKA_MAINACTIVITY" ;

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

//        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Apteka") ;
//        query1.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                ParseObject.pinAllInBackground("Apteka", list);
//            }
//        });
//
//        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Medikament") ;
//        query2.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> list, ParseException e) {
//                ParseObject.pinAllInBackground("Medikament", list);
//            }
//        });





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ParseObject.unpinAllInBackground("Apteka") ;
//        ParseObject.unpinAllInBackground("Medikament") ;

    }

    private boolean servicesConnected(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ;
        if(resultCode == ConnectionResult.SUCCESS){
            Log.d(TAG, "Google Play services доступен") ;
            return true ;
        } else{
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0) ;
            if(dialog != null){
                ErrorDialogFragment errorFragment = new ErrorDialogFragment() ;
                errorFragment.setDialog(dialog) ;
                errorFragment.show(getFragmentManager(), TAG);
            }
            return false ;
        }
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
                                if(servicesConnected()) {
                                    Intent intent2 = new Intent(MainActivity.this, MapsActivity.class);
                                    startActivity(intent2);
                                }
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

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog ;


        public ErrorDialogFragment(){
            super() ;
            mDialog = null ;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}
