package kz.abcsoft.aptekatest1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import kz.abcsoft.aptekatest1.adapters.TabsPagerAdapter;
import kz.abcsoft.aptekatest1.models.Apteka;

public class AptekaDetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;

    private Toolbar toolbar ;
    private ProgressDialog dialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apteka_detail) ;

        String pid = getIntent().getStringExtra("pid") ;

        toolbar = (Toolbar)findViewById(R.id.apteka_detail_toolbar) ;
        toolbar.setNavigationIcon(R.drawable.previous_24);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Apteka") ;
        query.fromLocalDatastore() ;
        query.getInBackground(pid, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                String apteName = parseObject.getString("name") ;
                toolbar.setTitle(apteName);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.phone:
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        callIntent.setData(Uri.parse("tel:+77078914322"));
//                        startActivity(callIntent);
                        //finish() ;
                        return true;
                    case R.id.get_addresses:
//                        Intent outputAddresses = new Intent(getApplicationContext(), OutputAddressesActivity.class) ;
//                        outputAddresses.putExtra("addresses", addresses);
//                        startActivity(outputAddresses);
//                        finish() ;
                        return true;

                }

                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMedikamentsList = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(toMedikamentsList);

            }
        });

        toolbar.inflateMenu(R.menu.apteka_detail_menu) ;



        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager()) ;
        viewPager.setAdapter(mAdapter);

    }


}
