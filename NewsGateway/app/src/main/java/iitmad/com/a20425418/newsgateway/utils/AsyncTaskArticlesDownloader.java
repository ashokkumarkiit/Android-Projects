package iitmad.com.a20425418.newsgateway.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import iitmad.com.a20425418.newsgateway.R;
import iitmad.com.a20425418.newsgateway.bean.ArticleBean;
import iitmad.com.a20425418.newsgateway.bean.NewsSourcesBean;


/**
 * Created by Ashok Kumar - A20425418 on 11/22/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class AsyncTaskArticlesDownloader extends AsyncTask<String,Void,List<ArticleBean>> {

    AsyncTaskArticlesDownloaderListener asyncTaskArticlesDownloaderListener;
    private static final String TAG = "AsyncTaskArticlesDownlo";
    //Context context;
    JSONParser parseJSON;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(asyncTaskArticlesDownloaderListener != null){
            asyncTaskArticlesDownloaderListener.onPreDownload();
        }
    }

    @Override
    protected List<ArticleBean> doInBackground(String... params) {
        List<ArticleBean> listArticleBean = new ArrayList<ArticleBean>();
        //API URL for calling NewsSources
        //Uri dataUri = Uri.parse(context.getString(R.string.strArticlesURL,context.getString(R.string.strAPIKey)));
        Uri dataUri = Uri.parse("https://newsapi.org/v2/top-headlines?pageSize=10&sources="+ params[0] +"&apiKey=de954d1a67604d3fb961233d5e7ca93c");

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
            listArticleBean = parseJSON.parseNewsArticles(sb.toString());
            return(listArticleBean);
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<ArticleBean> articleBeans) {
        super.onPostExecute(articleBeans);
        if(asyncTaskArticlesDownloaderListener != null){
            asyncTaskArticlesDownloaderListener.onPostDownload(articleBeans);
        }
    }

    // Setting the Listener and is set by the caller function
    public void setListener(AsyncTaskArticlesDownloader.AsyncTaskArticlesDownloaderListener asyncTaskArticlesDownloaderListener) {
        this.asyncTaskArticlesDownloaderListener = asyncTaskArticlesDownloaderListener;
    }


    // Interface with methods
    public interface AsyncTaskArticlesDownloaderListener {
        public Context onPreDownload();

        public void onPostDownload(List<ArticleBean> result);

    }
}
