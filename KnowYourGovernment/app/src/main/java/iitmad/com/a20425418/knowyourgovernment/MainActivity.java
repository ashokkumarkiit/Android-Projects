package iitmad.com.a20425418.knowyourgovernment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Console;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import iitmad.com.a20425418.knowyourgovernment.beans.GoogleCivicBean;
import iitmad.com.a20425418.knowyourgovernment.beans.RecyclerClassBean;
import iitmad.com.a20425418.knowyourgovernment.utils.AsyncTaskLoadingCivicAPI;
import iitmad.com.a20425418.knowyourgovernment.utils.AsyncTaskLocationDetails;
import iitmad.com.a20425418.knowyourgovernment.utils.LocationService;
import iitmad.com.a20425418.knowyourgovernment.utils.RecyclerAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerKnowYourGovernment;
    RecyclerAdapter recyclerAdapter;
    List<RecyclerClassBean> listRecyclerClassBean;
    AsyncTaskLoadingCivicAPI asyncTaskLoadingCivicAPI;
    TextView tvLocationInfo;
    GoogleCivicBean googleCivicBean;
    private int MY_PERM_REQUEST_CODE = 25418;
    private static final String TAG = "MainActivity";
    private Location bestLocation;
    private boolean showingInfo = false;
    String locationString;
    AsyncTaskLocationDetails asyncTaskLocationDetails;
    LocationManager locationManager;
    String googleCivicAPISearchString = "";
    Boolean isFirstLaunchDone = false;
    RelativeLayout rlNoNetworkConnection;
    public static final String BROADCAST_FROM_SERVICE = "BROADCAST FROM SERVICE";
    private LocationBroadcastReceiver locationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerKnowYourGovernment = (RecyclerView) findViewById(R.id.recyclerKnowYourGovernment);
        tvLocationInfo = (TextView) findViewById(R.id.tvLocationInfo);
        rlNoNetworkConnection = (RelativeLayout) findViewById(R.id.rlNoNetworkConnection);
        listRecyclerClassBean = new ArrayList<RecyclerClassBean>();
        googleCivicBean = new GoogleCivicBean();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        doNetCheck();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstLaunchDone) {
            boolean havePermission = checkPermission();
            if (havePermission) {
                startService(new Intent(this, LocationService.class));
                locationBroadcastReceiver = new LocationBroadcastReceiver();
                IntentFilter filter = new IntentFilter(BROADCAST_FROM_SERVICE);
                registerReceiver(locationBroadcastReceiver, filter);
                if (checkLocation())
                    executeAsyncTaskLocationDetails();
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(locationBroadcastReceiver != null){
                unregisterReceiver(locationBroadcastReceiver);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.strEnableLocationTitle))
                .setMessage(getResources().getString(R.string.strEnableLocationBody))
                .setPositiveButton(getResources().getString(R.string.strEnableLocationButton), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        paramDialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.dismiss();
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void loadRecyclerView(List<RecyclerClassBean> v_listRecyclerClassBean) {
        rlNoNetworkConnection.setVisibility(View.GONE);
        recyclerKnowYourGovernment.setVisibility(View.VISIBLE);
        recyclerAdapter = new RecyclerAdapter(v_listRecyclerClassBean, MainActivity.this);
        recyclerKnowYourGovernment.setAdapter(recyclerAdapter);
        recyclerKnowYourGovernment.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter.notifyDataSetChanged();
    }

    public void executeAsyncTaskKnowYourGovernment(String param) {
        if (doNetCheck()) {
            asyncTaskLoadingCivicAPI = new AsyncTaskLoadingCivicAPI();
            asyncTaskLoadingCivicAPI.setListener(asyncTaskLoadingCivicAPIListener);
            asyncTaskLoadingCivicAPI.execute(param);
        }
    }

    public void executeAsyncTaskLocationDetails() {
        asyncTaskLocationDetails = new AsyncTaskLocationDetails();
        asyncTaskLocationDetails.setListener(asyncTaskLocationDetailsListener);
        asyncTaskLocationDetails.execute("");
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerKnowYourGovernment.getChildLayoutPosition(v);
        RecyclerClassBean recylcerBean = listRecyclerClassBean.get(pos);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra(this.getResources().getString(R.string.strIntentExtraLocationDetails),
                googleCivicBean.getOfficesLocation());
        //intent.putExtra(this.getResources().getString(R.string.strIntentExtraPosition),pos);
        intent.putExtra(this.getResources().getString(R.string.strIntentExtraOfficesName),
                recylcerBean.getTvOfficeName());
        intent.putExtra(this.getResources().getString(R.string.strIntentExtraOfficialDetails),
                googleCivicBean.getOfficials().get(pos));
        startActivityForResult(intent,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuSearch:
                showInputDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    AsyncTaskLoadingCivicAPI.AsyncTaskLoadingCivicAPIListener asyncTaskLoadingCivicAPIListener =
            new AsyncTaskLoadingCivicAPI.AsyncTaskLoadingCivicAPIListener() {
                @Override
                public Context onPreDownload() {
                    return MainActivity.this;
                }

                @Override
                public void onPostDownload(GoogleCivicBean result) {
                    if (result != null) {
                        isFirstLaunchDone = true;
                        googleCivicBean = result;
                        String tempLocationString = "";
                        if (!(result.getOfficesLocation().getCity().equals("")))
                            tempLocationString = result.getOfficesLocation().getCity() + "," +
                                    result.getOfficesLocation().getState() + " " + result.getOfficesLocation().getZip();
                        else
                            tempLocationString = result.getOfficesLocation().getState() + " " + result.getOfficesLocation().getZip();
                        tvLocationInfo.setText(tempLocationString);

                        //Removing all the previous values of the recycler class bean list
                        listRecyclerClassBean.clear();

                        //Looping to the offices and populating the details of officials to those offices
                        for (int i = 0; i < result.getOffices().size(); i++) {
                            //Pulling the list of all the officials indices and fetching officials name
                            for (int j = 0; j < result.getOffices().get(i).getOfficialIndices().size(); j++) {
                                RecyclerClassBean recyclerBean = new RecyclerClassBean();
                                recyclerBean.setTvOfficeName(result.getOffices().get(i).getOffice_name());
                                int officialIndex = Integer.parseInt(result.getOffices().get(i).getOfficialIndices().get(j));
                                recyclerBean.setTvOfficialsName(result.getOfficials().get(officialIndex).getOfficialName());
                                recyclerBean.setTvOfficialsParty(result.getOfficials().get(officialIndex).getOfficialParty());
                                listRecyclerClassBean.add(recyclerBean);
                            }
                        }
                        loadRecyclerView(listRecyclerClassBean);
                    } else {
                        tvLocationInfo.setText("No Data For Location");
                    }
                }
            };

    AsyncTaskLocationDetails.AsyncTaskLocationDetailsListener asyncTaskLocationDetailsListener =
            new AsyncTaskLocationDetails.AsyncTaskLocationDetailsListener() {
                @Override
                public MainActivity onPreDownload() {
                    return MainActivity.this;
                }

                @Override
                public void onPostDownload(Location result) {
                    if (result != null) {
                        stopService(new Intent(MainActivity.this, LocationService.class));
                        Log.d(TAG, "onPostDownload: " + result);
                        bestLocation = result;
                        getInfo("LOCATION");
                    }
                }
            };

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // I do not yet have permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERM_REQUEST_CODE);

            Log.d(TAG, "checkPermission: ACCESS_FINE_LOCATION Permission requested, awaiting response.");
            return false; // Do not yet have permission - but I just asked for it
        } else {
            Log.d(TAG, "checkPermission: Already have ACCESS_FINE_LOCATION Permission for this app.");
            return true;  // I already have this permission
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == MY_PERM_REQUEST_CODE) {
            if (grantResults.length == 0) {
                Log.d(TAG, "onRequestPermissionsResult: Somehow I got an empty 'grantResults' array");
                return;
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //User granted permissions!
                Log.d("TAG", "Fine location permission granted");
                //findLocation();

            } else {
                //User denied Location permissions. Here you could warn the user that without
                //Location permissions the app is not usable.
                //Toast.makeText(this, "Have a good day!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void getInfo(String type) {
        int numResults = 1;
        if (bestLocation == null) {
            Toast.makeText(this, "No location available for Geocoder to use", Toast.LENGTH_LONG).show();
            return;
        }
        //if (!showingInfo) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        StringBuilder sb = new StringBuilder();
        try {
            if (type.equals("LOCATION")) {
                addresses = geocoder.getFromLocation(
                        bestLocation.getLatitude(), bestLocation.getLongitude(), numResults);
                if (addresses.size() > 0) {
                    googleCivicAPISearchString = addresses.get(0).getPostalCode();
                } else {
                    listRecyclerClassBean.clear();
                    recyclerKnowYourGovernment.setVisibility(View.GONE);
                    tvLocationInfo.setText(this.getResources().getString(R.string.strNoDataForLocation));
                    Toast.makeText(this, getResources().getString(R.string.strNoDataForLocationMessage),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                addresses = geocoder.getFromLocationName(locationString, 1);
                if (addresses.size() > 0) {
                    googleCivicAPISearchString = locationString;
                /*addresses.get(0).getPostalCode();
                addresses.get(0).getAdminArea();
                addresses.get(0).getLocality();*/
                } else {
                    listRecyclerClassBean.clear();
                    recyclerKnowYourGovernment.setVisibility(View.GONE);
                    tvLocationInfo.setText(this.getResources().getString(R.string.strNoDataForLocation));
                    Toast.makeText(this, getResources().getString(R.string.strNoDataForLocationMessage),
                            Toast.LENGTH_SHORT).show();
                }
            }
            if (addresses.size() > 0)
                executeAsyncTaskKnowYourGovernment(googleCivicAPISearchString);

            Log.d(TAG, "getInfo: " + sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().toUpperCase().contains("GRPC")) {
                if(!(type.equals("LOCATION")))
                    Toast.makeText(this, getResources().getString(R.string.strNoDataForLocationMessage),
                            Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showInputDialog() {
        // Single input value dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create an edittext and set it to be the builder's view
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Set Value to varible
                locationString = et.getText().toString();
                getInfo("SEARCH");
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setMessage(getResources().getString(R.string.strSearchDialogMessage));

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Check For internet connection. If No internet connection, it will show a dialog
    private Boolean doNetCheck() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                //Cannot Access Connectivity Manager
                return false;
            }

            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                //Connected
                return true;
            } else {
                //Not Connected
                //Show Dialog for Not available network
                rlNoNetworkConnection.setVisibility(View.VISIBLE);
                recyclerKnowYourGovernment.setVisibility(View.GONE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    class LocationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action == null)
                return;
            switch (action) {
                case BROADCAST_FROM_SERVICE:
                    Log.d(TAG, "onReceive: ");
                    if (!isFirstLaunchDone) {
                        boolean havePermission = checkPermission();
                        if (havePermission) {
                            if (checkLocation())
                                executeAsyncTaskLocationDetails();
                        }
                    }
                    break;
                default:
                    Log.d(TAG, "onReceive: Unknown broadcast received");
            }
        }
    }
}
