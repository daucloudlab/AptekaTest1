package kz.abcsoft.aptekatest1.maps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import kz.abcsoft.aptekatest1.R;

public class MapsActivity extends AppCompatActivity {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar)findViewById(R.id.map_toolbar) ;
        toolbar.setTitle(R.string.nav_menu_item_near_apteki);

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        double myLatitude = myLocation.getLatitude();
        double myLongitude = myLocation.getLongitude();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE
        );
        mMap.addMarker(new MarkerOptions().position(new LatLng(myLatitude, myLongitude))
                .icon(bitmapDescriptor)
                .title("Я нахожусь здесь"));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(myLatitude, myLongitude))
                .zoom(13)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);


//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.31854237, 69.5962429)).title("Аптека 1"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.33306634279456, 69.58523947745562)).title("Аптека 2"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.337768136670306, 69.59050666540861)).title("Аптека 3"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.33212103432582, 69.5817157253623)).title("Аптека 4"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.323667401903194, 69.58151590079069)).title("Аптека 5"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.317695758479736, 69.5814773440361)).title("Аптека 6"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.313774428866246, 69.58416223526001)).title("Аптека 7"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(42.32053724959474, 69.58785999566317)).title("Аптека 8"));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Apteka") ;
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject object : list){
                    double latitude = ((Number)object.get("latitude")).doubleValue() ;
                    double longitude = ((Number)object.get("longitude")).doubleValue() ;
                    String aptekaName = object.getString("name") ;
                    String aptekaAddress = object.getString("address") ;
                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                            .title(aptekaName + "\n" + aptekaAddress));
            }
        }
        }) ;
    }


}
