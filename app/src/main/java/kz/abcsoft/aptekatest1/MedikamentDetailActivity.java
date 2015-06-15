package kz.abcsoft.aptekatest1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;


public class MedikamentDetailActivity extends AppCompatActivity {

    private ProgressDialog dialog ;
    String pid ;
    String mid ;

    Toolbar toolbar ;
    String aptekaPhone ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medikament_detail);
        toolbar = (Toolbar)findViewById(R.id.activity_medikament_detail_toolbar) ;
        toolbar.setTitle(R.string.medikament_detail_toolbar_title);
        toolbar.inflateMenu(R.menu.medikament_detail_menu);
        toolbar.setNavigationIcon(R.drawable.previous_24);

        pid= getIntent().getStringExtra("pid") ;
        mid = getIntent().getStringExtra("mid") ;

        new ConcreteMedikamentTask().execute() ;

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.phone_in_med_detail_activity:
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        String tel = "tel:" + aptekaPhone;
                        callIntent.setData(Uri.parse(tel));
                        startActivity(callIntent);
                        finish();

                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private class ConcreteMedikamentTask extends AsyncTask<Void, Void, Void>{
        String aptekaName ;
        String medikamentTitle ;
        String medikamentDescription ;
        double medikamentPrice ;
        String imageUrl ;
        String medikamentFullInformation ;

        String aptekaAddress ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MedikamentDetailActivity.this) ;
            dialog.setMessage("подождите...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ParseQuery<ParseObject> aptekaQuery = ParseQuery.getQuery("Apteka") ;
            aptekaQuery.fromLocalDatastore() ;
            try {
                ParseObject aptekaObject = aptekaQuery.get(pid);
                aptekaName = aptekaObject.getString("name") ;
                aptekaPhone = aptekaObject.getString("phone") ;
                aptekaAddress = aptekaObject.getString("address") ;

            }catch (ParseException e1){
                e1.printStackTrace();
            }

            ParseQuery<ParseObject> medikamentQuery = ParseQuery.getQuery("Medikament") ;
            medikamentQuery.fromLocalDatastore() ;
            try{
                ParseObject medikamentObject = medikamentQuery.get(mid) ;
                medikamentTitle = medikamentObject.getString("title") ;
                medikamentDescription = medikamentObject.getString("description") ;
                medikamentPrice = ((Number)medikamentObject.get("price")).doubleValue() ;

                ParseFile medikamentImage = medikamentObject.getParseFile("image") ;
                imageUrl = medikamentImage.getUrl() ;

                ParseFile medikamentInformation = medikamentObject.getParseFile("information") ;
                byte [] information = medikamentInformation.getData() ;
                medikamentFullInformation = new String(information, "UTF-8") ;

            }catch(ParseException e2){
                e2.printStackTrace();
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView aptekaNameTV = (TextView)findViewById(R.id.medikament_apteka_name) ;
            TextView medikamentNameTV = (TextView)findViewById(R.id.medikament_title_in_med_detail_activity) ;
            TextView medikamentDescriptionTV = (TextView)findViewById(R.id.medikament_description_in_med_detail_activity);
            TextView medikamentPriceTV = (TextView)findViewById(R.id.medikament_price) ;
            ImageView medikamentImage = (ImageView)findViewById(R.id.medikement_image) ;
            TextView aptekaPhoneTV = (TextView)findViewById(R.id.apteka_phone_in_med_detail_activity) ;
            TextView aptekaAddressTV = (TextView)findViewById(R.id.apteka_address_in_med_detail_activity) ;
            TextView medikamentFullInformationTV = (TextView)findViewById(R.id.medikament_full_information) ;

            aptekaNameTV.setText(aptekaName);
            Picasso.with(MedikamentDetailActivity.this).load(imageUrl).into(medikamentImage);
            medikamentNameTV.setText(medikamentTitle);
            medikamentDescriptionTV.setText(medikamentDescription);
            medikamentPriceTV.setText(((Double)medikamentPrice).toString() + " тг.");
            aptekaPhoneTV.setText(aptekaPhone);
            aptekaAddressTV.setText(aptekaAddress);
            medikamentFullInformationTV.setText(medikamentFullInformation);
            dialog.dismiss();



        }
    }
}
