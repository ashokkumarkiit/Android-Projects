package iitmad.com.a20425418.newsgateway.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import iitmad.com.a20425418.newsgateway.R;
import iitmad.com.a20425418.newsgateway.bean.NewsSourcesBean;
import iitmad.com.a20425418.newsgateway.bean.SourcesBean;

/**
 * Created by Ashok Kumar - A20425418 on 11/21/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class AsyncTaskNewsSourceDownloader extends AsyncTask<String,Void,NewsSourcesBean> {


    AsyncTaskNewsSourceDownloaderListener asyncTaskNewsSourceDownloaderListener;
    Context context;
    private static final String TAG = "AsyncTaskNewsSourceDown";
    JSONParser parseJSON;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(asyncTaskNewsSourceDownloaderListener != null){
            context = asyncTaskNewsSourceDownloaderListener.onPreDownload();
        }
    }

    @Override
    protected NewsSourcesBean doInBackground(String... params) {
        NewsSourcesBean newsSourcesBean = new NewsSourcesBean();
        //API URL for calling NewsSources
        Uri dataUri = Uri.parse(context.getString(R.string.strSourceURL,context.getString(R.string.strAPIKey)));
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
            newsSourcesBean = parseJSON.parseNewsSource(sb.toString());
            return(newsSourcesBean);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(NewsSourcesBean result) {
        super.onPostExecute(result);
        if(asyncTaskNewsSourceDownloaderListener != null){
            asyncTaskNewsSourceDownloaderListener.onPostDownload(result);
        }
    }


    // Setting the Listener and is set by the caller function
    public void setListener(AsyncTaskNewsSourceDownloader.AsyncTaskNewsSourceDownloaderListener asyncTaskNewsSourceDownloaderListener) {
        this.asyncTaskNewsSourceDownloaderListener = asyncTaskNewsSourceDownloaderListener;
    }


    // Interface with methods
    public interface AsyncTaskNewsSourceDownloaderListener {
        public Context onPreDownload();

        public void onPostDownload(NewsSourcesBean result);

    }
}
