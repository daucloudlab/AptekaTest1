package kz.abcsoft.aptekatest1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import kz.abcsoft.aptekatest1.adapters.AptekaListAdapter;
import kz.abcsoft.aptekatest1.models.Apteka;


public class AptekaListFragment extends Fragment {
    EditText aptekaSearch ;

    List<Apteka> listApteks ;
    List<ParseObject> parseObjects ;
    AptekaListAdapter adapter ;

    ProgressDialog dialog ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apteka_list, container, false) ;


        new ApteksListTask().execute() ;



        aptekaSearch = (EditText)view.findViewById(R.id.inputAptekSearch) ;
        aptekaSearch.addTextChangedListener(new TextWatcher() {
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


        return view ;
    }



    private class ApteksListTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity()) ;
            dialog.setMessage("подождите...");
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            listApteks = new ArrayList<Apteka>() ;

            try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Apteka");
                parseObjects = query.find();

                for (ParseObject a : parseObjects) {
                    String aid = a.getObjectId();
                    String name = a.getString("name");
                    String phone = a.getString("phone");
                    String address = a.getString("address");
                    double latitude = ((Number) a.get("latitude")).doubleValue();
                    double longitude = ((Number) a.get("longitude")).doubleValue();
                    Apteka apteka = new Apteka(aid, name, phone, address, latitude, longitude);
                    listApteks.add(apteka);
                    Log.d("ПРОВЕРКА", listApteks.toString()) ;
                }
            }catch(ParseException e){
                Log.e("ERROR!!!", e.getMessage()) ;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new AptekaListAdapter(getActivity(), listApteks ) ;
            ListView listView = (ListView)getView().findViewById(R.id.apteks_list) ;
            listView.setAdapter(adapter);

            dialog.dismiss();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                    Intent intent = new Intent(getActivity(), AptekaDetailActivity.class);
                    intent.putExtra("pid", pid);
                    startActivity(intent);
                }
            });

        }
    }

}
