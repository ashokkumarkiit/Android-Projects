package iitmad.com.a20425418.stockwatch.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashok Kumar - A20425418 on 10/10/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class AsyncStockSymbolDownloader extends AsyncTask<String,Void,Boolean> {

    public AsyncStockSymbolDownloaderListener stockSymbolDownloaderListener;
    public Context context;
    private static final String TAG = "AsyncStockSymbolDownloa";
    DatabaseHandler dbHandler;
    private List<StockSymbolBean> listStockSymbolBean;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(stockSymbolDownloaderListener != null){
            context = stockSymbolDownloaderListener.onPreDownload();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        Uri dataUri = Uri.parse(Constants.stockSymbolDownloadURL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);
        dbHandler = new DatabaseHandler(context);
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
            listStockSymbolBean = JSONParser.parseStockSymbolJSON(sb.toString());
            dbHandler.deleteStockSymbolList();
            dbHandler.insertStockSymbolList(listStockSymbolBean);

            Log.d(TAG, "doInBackground: " + sb.toString());
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        super.onPostExecute(bool);
        if(stockSymbolDownloaderListener != null){
            stockSymbolDownloaderListener.onPostDownload(bool);
        }
    }

    // Setting the Listener and is set by the caller function
    public void setListener(AsyncStockSymbolDownloaderListener stockSymbolDownloaderListener) {
        this.stockSymbolDownloaderListener = stockSymbolDownloaderListener;
    }

    // Interface with methods
    public interface AsyncStockSymbolDownloaderListener {
        public Context onPreDownload();

        public void onPostDownload(Object result);

    }
}


