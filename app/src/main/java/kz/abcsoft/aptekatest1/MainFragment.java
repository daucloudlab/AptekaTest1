package kz.abcsoft.aptekatest1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import kz.abcsoft.aptekatest1.adapters.MedikamentListAdapter;
import kz.abcsoft.aptekatest1.models.Apteka;
import kz.abcsoft.aptekatest1.models.Medikament;

public class MainFragment extends Fragment {

    private EditText medikamentSearch ;
    private List<Medikament> listMedikaments ;
    private List<ParseObject> mObjects ;
    private List<Apteka> listApteks ;
    private MedikamentListAdapter adapter ;

    private ProgressDialog dialog ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false) ;

//        listApteks = AptekaTestList.getListApteks() ;
//        listMedikaments = MedikamentTestList.getAllMedikaments();

        new MedikamentsOutTask().execute() ;

        medikamentSearch = (EditText)view.findViewById(R.id.input_medikament_search) ;
        medikamentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String stringAID = ((TextView)view.findViewById(R.id.apteka_id)).getText().toString() ;
//                String stringMID = ((TextView)view.findViewById(R.id.medikament_id)).getText().toString() ;
//                Intent medikamentDetailIntent = new Intent(getActivity(), MedikamentDetailActivity.class) ;
//                medikamentDetailIntent.putExtra("pid", stringAID) ;
//                medikamentDetailIntent.putExtra("mid", stringMID) ;
//                startActivity(medikamentDetailIntent);
//            }
//        });


        return view ;
    }

    private class MedikamentsOutTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity()) ;
            dialog.setMessage("подождите...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            listMedikaments = new ArrayList<Medikament>() ;
            listApteks = new ArrayList<Apteka>() ;

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Medikament") ;
            query.fromLocalDatastore() ;
            try{
                mObjects = query.find() ;
                for(ParseObject m : mObjects){
                    String mid = m.getObjectId() ;
                    String medikamentTitle = m.getString("title") ;
                    String medikamentDescription = m.getString("description") ;
                    double medikamentPrice = ((Number)m.get("price")).doubleValue() ;

                    ParseObject aptekaObject = m.getParseObject("apteka_rel") ;
                    String aid = aptekaObject.fetchIfNeeded().getObjectId() ;
                    String aptekaName = aptekaObject.fetchIfNeeded().getString("name") ;
                    String aptekaPhone = aptekaObject.fetchIfNeeded().getString("phone") ;
                     Medikament medikament = new Medikament(mid, aid, medikamentTitle, medikamentDescription, medikamentPrice) ;
                    Apteka apteka = new Apteka(aid, aptekaName, aptekaPhone) ;

                    listApteks.add(apteka) ;
                    listMedikaments.add(medikament) ;
                }
//                Log.d("ПРОВЕРКА", listApteks.toString()) ;
//                Log.d("ПРОВЕРКА", listMedikaments.toString()) ;
            }catch(ParseException e){
                Log.d("ERROR!!!!", e.getMessage()) ;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter  = new MedikamentListAdapter(getActivity(), listMedikaments, listApteks) ;
            ListView listView = (ListView)getView().findViewById(R.id.medikaments_list) ;
            listView.setAdapter(adapter);

            dialog.dismiss();
        }
    }
}
