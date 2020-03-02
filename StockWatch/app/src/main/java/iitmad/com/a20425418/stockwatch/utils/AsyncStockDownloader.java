package iitmad.com.a20425418.stockwatch.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import iitmad.com.a20425418.stockwatch.R;

/**
 * Created by Ashok Kumar - A20425418 on 10/12/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class AsyncStockDownloader extends AsyncTask<String,Void,Boolean> {

    AsyncStockDownloaderListener stockDownloaderListener;
    Context context;
    DatabaseHandler dbHandler;
    private static final String TAG = "AsyncStockDownloader";
    StockBean stockBean;
    List<StockBean> listStockBean;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(stockDownloaderListener != null){
            context = stockDownloaderListener.onPreDownload();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        dbHandler = new DatabaseHandler(context);
        Uri dataUri = Uri.parse(context.getString(R.string.strStockDownloadURL,strings[0],context.getString(R.string.token)));
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
            listStockBean = JSONParser.parseStockJSON(sb.toString());
            return(dbHandler.insertStock(listStockBean.get(0)));
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(stockDownloaderListener != null){
            stockDownloaderListener.onPostDownload(aBoolean);
        }
    }

    // Setting the Listener and is set by the caller function
    public void setListener(AsyncStockDownloader.AsyncStockDownloaderListener stockDownloaderListener) {
        this.stockDownloaderListener = stockDownloaderListener;
    }

    // Interface with methods
    public interface AsyncStockDownloaderListener {
        public Context onPreDownload();

        public void onPostDownload(Object result);

    }
}
