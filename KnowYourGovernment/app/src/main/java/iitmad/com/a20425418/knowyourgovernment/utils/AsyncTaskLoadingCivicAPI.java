package iitmad.com.a20425418.knowyourgovernment.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import iitmad.com.a20425418.knowyourgovernment.R;
import iitmad.com.a20425418.knowyourgovernment.beans.GoogleCivicBean;

/**
 * Created by Ashok Kumar - A20425418 on 10/31/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class AsyncTaskLoadingCivicAPI extends AsyncTask<String,Void,GoogleCivicBean> {

    AsyncTaskLoadingCivicAPIListener asyncTaskLoadingCivicAPIListener;
    Context context;
    private static final String TAG = "AsyncTaskLoadingCivicAP";
    public GoogleCivicBean googleCivicBean;
    JSONParser parseJSON;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(asyncTaskLoadingCivicAPIListener != null){
            context = asyncTaskLoadingCivicAPIListener.onPreDownload();
        }
    }

    @Override
    protected GoogleCivicBean doInBackground(String... strings) {
        //API URL for calling google CIVIC API based on Location information that is passed using location services.
        Uri dataUri = Uri.parse(context.getString(R.string.google_civic_api_url,context.getString(R.string.API_KEY),strings[0]));
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d(TAG, "doInBackground: " + sb.toString());
            parseJSON = new JSONParser();
            googleCivicBean = parseJSON.parser(sb.toString());
            Log.d(TAG, "doInBackground: googleCivicBean" + googleCivicBean);
            return(googleCivicBean);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(GoogleCivicBean result) {
        super.onPostExecute(result);
        if(asyncTaskLoadingCivicAPIListener != null){
            asyncTaskLoadingCivicAPIListener.onPostDownload(result);
        }
    }

    // Setting the Listener and is set by the caller function
    public void setListener(AsyncTaskLoadingCivicAPI.AsyncTaskLoadingCivicAPIListener asyncTaskLoadingCivicAPIListener) {
        this.asyncTaskLoadingCivicAPIListener = asyncTaskLoadingCivicAPIListener;
    }


    // Interface with methods
    public interface AsyncTaskLoadingCivicAPIListener {
        public Context onPreDownload();

        public void onPostDownload(GoogleCivicBean result);

    }
}
