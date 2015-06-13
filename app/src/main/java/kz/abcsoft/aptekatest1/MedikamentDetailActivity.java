package kz.abcsoft.aptekatest1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class MedikamentDetailActivity extends AppCompatActivity {

    private ProgressDialog dialog ;
    String pid ;
    String mid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medikament_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.activity_medikament_detail_toolbar) ;
        toolbar.setTitle(R.string.medikament_detail_toolbar_title);

        pid= getIntent().getStringExtra("pid") ;
        mid = getIntent().getStringExtra("mid") ;

        new ConcreteMedikamentTask().execute() ;

    }

    private class ConcreteMedikamentTask extends AsyncTask<Void, Void, Void>{
        String aptekaName ;
        String medikamentTitle ;
        String medikamentDescription ;
        double medikamentPrice ;
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
            }catch(ParseException e2){
                e2.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            TextView aptekaNameTV = (TextView)findViewById(R.id.medikament_apteka_name) ;
            TextView medikamentNameTV = (TextView)findViewById(R.id.medikament_title_in_med_detail_activity) ;
            TextView medikamentDescriptionTV = (TextView)findViewById(R.id.medikament_description_in_med_detail_activity);
            TextView medikamentPriceTV = (TextView)findViewById(R.id.medikament_price) ;

            aptekaNameTV.setText(aptekaName);
            medikamentNameTV.setText(medikamentTitle);
            medikamentDescriptionTV.setText(medikamentDescription);
            medikamentPriceTV.setText(((Double)medikamentPrice).toString() + " тг.");

            dialog.dismiss();
        }
    }
}
