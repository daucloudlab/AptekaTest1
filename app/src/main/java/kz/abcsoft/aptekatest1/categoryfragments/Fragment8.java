package kz.abcsoft.aptekatest1.categoryfragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import kz.abcsoft.aptekatest1.R;
import kz.abcsoft.aptekatest1.adapters.AptekaMedikamentListAdapter;
import kz.abcsoft.aptekatest1.models.Apteka;
import kz.abcsoft.aptekatest1.models.Medikament;

public class Fragment8 extends Fragment {

    private ProgressDialog dialog;
    private List<ParseObject> mObjects;
    private List<Medikament> categoryMedikaments;
    private String pid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false) ;

        pid = getActivity().getIntent().getStringExtra("pid") ;

        new CategoryMedikamentsTask().execute();

        return rootView ;
    }


    private class CategoryMedikamentsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("подождите...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoryMedikaments = new ArrayList<Medikament>();

            ParseQuery<ParseObject> apteksQuery = ParseQuery.getQuery("Apteka");
            apteksQuery.fromLocalDatastore() ;
            apteksQuery.whereEqualTo("objectId", pid);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Medikament");
            query.fromLocalDatastore() ;
            query.whereEqualTo("category_id", "8");

            query.whereMatchesQuery("apteka_rel", apteksQuery);

            try {
                mObjects = query.find();

                for (ParseObject object : mObjects) {
                    String mid = object.getObjectId();
                    String title = object.getString("title");
                    String description = object.getString("description");
                    double price = ((Number) object.get("price")).doubleValue();
                    Medikament medikament = new Medikament(mid, pid, title, description, price);
                    categoryMedikaments.add(medikament);
                }
            } catch (ParseException e) {
                Log.d("ОШИБКА", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ListView medikamentsListView = (ListView) getView().findViewById(R.id.listFragment1);
            AptekaMedikamentListAdapter medikamentListAdapter = new AptekaMedikamentListAdapter(getActivity(),
                    categoryMedikaments);
            medikamentsListView.setAdapter(medikamentListAdapter);
            Log.d("CATEGORY_MEDIKAMENTS", categoryMedikaments.toString());
            dialog.dismiss();

//            medikamentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    Intent medikamentDetailIntent = new Intent(getActivity(), MedikamentDetailActivity.class);
//                    medikamentDetailIntent.putExtra("pid", pid);
//                    Medikament medikament = medikamentsCategory1.get(i);
//                    medikamentDetailIntent.putExtra("mid", Integer.toString(medikament.getMid()));
//                    startActivity(medikamentDetailIntent);
//                }
//            }) ;

        }

    }

}