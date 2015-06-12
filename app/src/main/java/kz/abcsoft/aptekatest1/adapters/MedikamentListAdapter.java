package kz.abcsoft.aptekatest1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kz.abcsoft.aptekatest1.R;
import kz.abcsoft.aptekatest1.models.Apteka;
import kz.abcsoft.aptekatest1.models.Medikament;


public class MedikamentListAdapter extends BaseAdapter implements Filterable{

    private LayoutInflater inflater ;
    private Context context ;
    private List<Medikament> allMedikaments ;
    private List<Apteka> allApteks ;

    private List<Medikament> originalData ;
    private List<Medikament> filteredData ;
    private ItemFilter mFilter = new ItemFilter() ;

    public MedikamentListAdapter(Context _context, List<Medikament> _allMedikaments,
                                 List<Apteka> _allApteks){
        this.context = _context ;
        originalData = _allMedikaments ;
        filteredData = _allMedikaments ;
        this.allMedikaments = _allMedikaments ;
        this.allApteks = _allApteks ;
    }

    @Override
    public int getCount() {
        return filteredData.size() ;
    }

    @Override
    public Object getItem(int i) {
        return filteredData.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return i ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_medikaments_row, null);

        TextView medikamentTitle = (TextView)convertView.findViewById(R.id.medikament_title) ;
        TextView aptekaTitle = (TextView)convertView.findViewById(R.id.apteka_title) ;
        TextView aptekaID = (TextView)convertView.findViewById(R.id.apteka_id) ;
        TextView mediakmentID = (TextView)convertView.findViewById(R.id.medikament_id) ;
        TextView medikamentPrice = (TextView)convertView.findViewById(R.id.medikament_price) ;


        Medikament medikament = filteredData.get(position) ;

        medikamentTitle.setText(medikament.getTitle());
        for(Apteka a : allApteks){
            if(medikament.getAid() == a.getAid()) {
                aptekaTitle.setText(a.getName());

            }

        }
        aptekaID.setText(medikament.getAid());
        mediakmentID.setText(medikament.getMid());
        medikamentPrice.setText(Double.toString(medikament.getPrice()) + " тг");

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            List<Medikament> list = originalData ;
            int count = list.size() ;
            final ArrayList<Medikament> nlist = new ArrayList<>(count) ;

            String filterableString ;

            for(int i = 0; i < count; i++){
                filterableString = list.get(i).getTitle() ;
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist ;
            results.count = nlist.size() ;

            return results ;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Medikament>)results.values ;
            notifyDataSetChanged();
        }
    }

}
