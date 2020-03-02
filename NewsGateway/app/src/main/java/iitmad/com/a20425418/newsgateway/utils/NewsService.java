package iitmad.com.a20425418.newsgateway.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import iitmad.com.a20425418.newsgateway.MainActivity;
import iitmad.com.a20425418.newsgateway.bean.ArticleBean;

public class NewsService extends Service {

    private static final String TAG = "NewsService";
    private boolean running = true;
    AsyncTaskArticlesDownloader asyncTaskArticlesDownloader;

    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String newsSourceId = "";
        if(intent.hasExtra(MainActivity.NEWS_SOURCE)){
            newsSourceId = intent.getStringExtra(MainActivity.NEWS_SOURCE);
        }
        asyncTaskArticlesDownloader = new AsyncTaskArticlesDownloader();
        asyncTaskArticlesDownloader.setListener(asyncTaskArticlesDownloaderListener);
        asyncTaskArticlesDownloader.execute(newsSourceId);

        return Service.START_NOT_STICKY;
    }

    private void sendMessage(String msg) {
        Intent intent = new Intent();
        intent.setAction(MainActivity.BROADCAST_FROM_SERVICE);
        intent.putExtra(MainActivity.SERVICE_DATA_MSG, msg);
        sendBroadcast(intent);
    }

    private void sendResult(List<ArticleBean> result) {
        Intent intent = new Intent();
        intent.setAction(MainActivity.BROADCAST_FROM_SERVICE);
        intent.putExtra(MainActivity.SERVICE_DATA, (Serializable) result);
        sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        sendMessage("Service Destroyed");
        running = false;
        super.onDestroy();
    }

    AsyncTaskArticlesDownloader.AsyncTaskArticlesDownloaderListener asyncTaskArticlesDownloaderListener = new AsyncTaskArticlesDownloader.AsyncTaskArticlesDownloaderListener() {
        @Override
        public Context onPreDownload() {
            return null;
        }

        @Override
        public void onPostDownload(List<ArticleBean> result) {
            if(result != null){
                Log.d(TAG, "onPostDownload: " + result.toString() );
                sendResult(result);
            }

        }
    };
}
