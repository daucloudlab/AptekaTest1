package kz.abcsoft.aptekatest1.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kz.abcsoft.aptekatest1.R;
import kz.abcsoft.aptekatest1.models.Medikament;

public class AptekaMedikamentListAdapter extends BaseAdapter {
    private Activity activity ;
    private LayoutInflater inflater ;
    private List<Medikament> medikaments ;

    public AptekaMedikamentListAdapter(Activity activity, List<Medikament> medikaments){
        this.activity = activity ;
        this.medikaments = medikaments ;
    }

    @Override
    public int getCount(){
        return medikaments.size() ;
    }

    @Override
    public Object getItem(int location){
        return medikaments.get(location) ;
    }

    @Override
    public long getItemId(int position){
        return position ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater == null)
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_medikament_row, null) ;

        TextView title = (TextView)convertView.findViewById(R.id.title) ;
        TextView description = (TextView)convertView.findViewById(R.id.description) ;
        TextView price = (TextView)convertView.findViewById(R.id.price) ;
        TextView mid = (TextView)convertView.findViewById(R.id.mid_field) ;

        Medikament medikament = medikaments.get(position) ;

        title.setText(medikament.getTitle());
        description.setText(medikament.getDescription());
        mid.setText(medikament.getMid());
        price.setText(Double.toString(medikament.getPrice()) + " тг");


        return convertView;
    }
}
