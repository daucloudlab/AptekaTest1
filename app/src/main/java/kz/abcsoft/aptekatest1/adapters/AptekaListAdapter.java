package kz.abcsoft.aptekatest1.adapters;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
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


public class AptekaListAdapter extends BaseAdapter implements Filterable {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Apteka> aptekaItems;

    private List<Apteka> originalData = null;
    private List<Apteka> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();

    public AptekaListAdapter(Activity activity, List<Apteka> aptekaItems) {
        this.originalData = aptekaItems;
        this.filteredData = aptekaItems;
        this.activity = activity;
        this.aptekaItems = aptekaItems;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int location) {
        return filteredData.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_apteki_row, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView address = (TextView) convertView.findViewById(R.id.address);
        TextView pid = (TextView) convertView.findViewById(R.id.pid);
//        TextView mid = (TextView)convertView.findViewById(R.id.mid_in_apteki_list) ;

        Apteka apteka = filteredData.get(position);

        title.setText(apteka.getName());
        phone.setText(apteka.getPhone());

        address.setText(apteka.getAddress()) ;

        pid.setText(apteka.getAid()) ;


        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Apteka> list = originalData;

            int count = list.size();
            final ArrayList<Apteka> nlist = new ArrayList<Apteka>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Apteka>) results.values;
            notifyDataSetChanged();
        }


    }

}
