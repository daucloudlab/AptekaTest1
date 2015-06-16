package kz.abcsoft.aptekatest1.maps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
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

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "GooglePlayServicesActiv";
    protected static final int REQUEST_CODE_RESOLUTION = 1;
    private static final String KEY_IN_RESOLUTION = "is_in_resolution";
    private boolean mIsInResolution;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (savedInstanceState != null) {
            mIsInResolution = savedInstanceState.getBoolean(KEY_IN_RESOLUTION, false);
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.map_toolbar) ;
        toolbar.setTitle(R.string.nav_menu_item_near_apteki);



    }



    @Override
    protected void onStart() {
        super.onStart();

        servicesConnected();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    // Optionally, add additional APIs and scopes if required.
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();

        setUpMapIfNeeded();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IN_RESOLUTION, mIsInResolution);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RESOLUTION:
                retryConnecting();
                break;
        }
    }

    private void retryConnecting() {
        mIsInResolution = false;
        if (!mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "GoogleApiClient connected");
        // TODO: Start making API requests.
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

    }


    private boolean servicesConnected(){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ;
        if(resultCode == ConnectionResult.SUCCESS){
            Log.d(TAG, "Google Play services доступен") ;
            return true ;
        } else{
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0) ;
            if(dialog != null){
                ErrorDialogFragment errorFragment = new ErrorDialogFragment() ;
                errorFragment.setDialog(dialog) ;
                errorFragment.show(getFragmentManager(), TAG);
            }
            return false ;
        }
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
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = locationManager.getBestProvider(criteria, true) ;
//        String provider = LocationManager.NETWORK_PROVIDER ;
//        if(provider == null){
//            Intent gpsOptionsIntent = new Intent(
//                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(gpsOptionsIntent);
//        }else {
//            Location myLocation = locationManager.getLastKnownLocation(provider);

        if(mLastLocation != null) {
            double myLatitude = mLastLocation.getLatitude();
            double myLongitude = mLastLocation.getLongitude();

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
        }else{
            // Мынау жерге dialog шығару керек
            Intent gpsNetworkOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsNetworkOptionsIntent);
        }
//        }

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
                            .title(aptekaName + " " + aptekaAddress));
            }
        }
        }) ;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
        retryConnecting();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // Show a localized error dialog.
            GooglePlayServicesUtil.getErrorDialog(
                    result.getErrorCode(), this, 0, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            retryConnecting();
                        }
                    }).show();
            return;
        }
        // If there is an existing resolution error being displayed or a resolution
        // activity has started before, do nothing and wait for resolution
        // progress to be completed.
        if (mIsInResolution) {
            return;
        }
        mIsInResolution = true;
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
            retryConnecting();
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog ;


        public ErrorDialogFragment(){
            super() ;
            mDialog = null ;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

}
