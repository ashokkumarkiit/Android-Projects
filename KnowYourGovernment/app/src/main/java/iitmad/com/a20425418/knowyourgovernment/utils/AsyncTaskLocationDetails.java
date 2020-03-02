package iitmad.com.a20425418.knowyourgovernment.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import iitmad.com.a20425418.knowyourgovernment.MainActivity;
import iitmad.com.a20425418.knowyourgovernment.beans.GoogleCivicBean;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Ashok Kumar - A20425418 on 11/4/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class AsyncTaskLocationDetails  extends AsyncTask<String,Void,Location> {

    AsyncTaskLocationDetailsListener asyncTaskLocationDetailsListener;
    MainActivity context;
    private Location bestLocation;
    private static final String TAG = "AsyncTaskLocationDetail";
    private boolean showingInfo = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(asyncTaskLocationDetailsListener != null){
            context = asyncTaskLocationDetailsListener.onPreDownload();
        }
    }

    @Override
    protected Location doInBackground(String... strings) {
        List<Address> addressList = new ArrayList<Address>();
        try{
            findLocation();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return bestLocation;
    }

    @Override
    protected void onPostExecute(Location v_bestlocation) {
        super.onPostExecute(v_bestlocation);
        if(asyncTaskLocationDetailsListener != null){
            asyncTaskLocationDetailsListener.onPostDownload(v_bestlocation);
        }
    }

    // Setting the Listener and is set by the caller function
    public void setListener(AsyncTaskLocationDetails.AsyncTaskLocationDetailsListener asyncTaskLocationDetailsListener) {
        this.asyncTaskLocationDetailsListener = asyncTaskLocationDetailsListener;
    }

    // Interface with methods
    public interface AsyncTaskLocationDetailsListener {
        public MainActivity onPreDownload();

        public void onPostDownload(Location result);

    }

    public void findLocation() {

        bestLocation = null;
        long timeNow = System.currentTimeMillis();
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            Toast.makeText(context, "No Location services available", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String providerName : locationManager.getAllProviders()) {

            sb.append("PROVIDER: ").append(providerName).append("\n");


            // You have to do this check here ot Android Studio complains. :(
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location loc = locationManager.getLastKnownLocation(providerName);

            if (loc != null) {
                sb.append("Location found:\n");
                sb.append("  Accuracy: ").append(loc.getAccuracy()).append("m\n");
                sb.append("  Time: ").append((timeNow - loc.getTime()) / 1000).append("sec\n");
                sb.append("  Latitude: ").append(loc.getLatitude()).append("\n");
                sb.append("  Longitude: ").append(loc.getLongitude()).append("\n\n");
                if (bestLocation == null || loc.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = new Location(loc);
                }
            } else {
                sb.append("No location for ").append(providerName).append("\n");
            }
        }
        if (bestLocation == null){
            sb.append("No location provider available :(");
        }
        else
            sb.append(String.format(Locale.US, "\n\nBest Provider: %s (%.2f)",
                    bestLocation.getProvider(), bestLocation.getAccuracy()));
        Log.d(TAG, "findLocation: " + sb.toString());


    }
}
