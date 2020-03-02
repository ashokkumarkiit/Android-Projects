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
public class AsyncStockUpdater extends AsyncTask<String,Void,Boolean> {

    AsyncStockUpdaterListener stockUpdaterListener;
    Context context;
    DatabaseHandler dbHandler;
    private static final String TAG = "AsyncStockUpdater";
    StockBean stockBean;
    List<StockBean> listStockBean,tempListStockBean;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(stockUpdaterListener != null){
            context = stockUpdaterListener.onPreDownload();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        dbHandler = new DatabaseHandler(context);
        listStockBean = dbHandler.getStockList();
        try{
            for(int i = 0; i< listStockBean.size(); i++){
                Uri dataUri = Uri.parse(context.getString(R.string.strStockDownloadURL,listStockBean.get(i).getStockSymbol(),context.getString(R.string.token)));
                String urlToUse = dataUri.toString();
                Log.d(TAG, "doInBackground: " + urlToUse);
                StringBuilder sb = new StringBuilder();
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
                tempListStockBean = JSONParser.parseStockJSON(sb.toString());
                dbHandler.updateStockList(tempListStockBean.get(0));
            }
            return true;

        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(stockUpdaterListener != null){
            stockUpdaterListener.onPostDownload(aBoolean);
        }
    }

    // Setting the Listener and is set by the caller function
    public void setListener(AsyncStockUpdater.AsyncStockUpdaterListener stockUpdaterListener) {
        this.stockUpdaterListener = stockUpdaterListener;
    }

    // Interface with methods
    public interface AsyncStockUpdaterListener {
        public Context onPreDownload();

        public void onPostDownload(Object result);

    }
}
