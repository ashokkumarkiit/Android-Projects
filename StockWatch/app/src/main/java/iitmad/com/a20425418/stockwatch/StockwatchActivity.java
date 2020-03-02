package iitmad.com.a20425418.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import iitmad.com.a20425418.stockwatch.utils.AsyncStockDownloader;
import iitmad.com.a20425418.stockwatch.utils.AsyncStockSymbolDownloader;
import iitmad.com.a20425418.stockwatch.utils.AsyncStockUpdater;
import iitmad.com.a20425418.stockwatch.utils.DatabaseHandler;
import iitmad.com.a20425418.stockwatch.utils.StockBean;
import iitmad.com.a20425418.stockwatch.utils.StockSymbolBean;
import iitmad.com.a20425418.stockwatch.utils.StockWatchAdapter;

/**
 * Created by Ashok Kumar - A20425418 on 10/10/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class StockwatchActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    AsyncStockSymbolDownloader asyncStockSymbolDownloader;
    AsyncStockDownloader asyncStockDownloader;
    private AsyncStockUpdater asyncStockUpdater;
    Context context;
    private TextView tvAppFirstLoading,tvStockSymbolLoadErrorMessage,tvGeneralMessage;
    private ProgressBar pbAppFirstLoading;
    private RecyclerView recyclerStockWatch;
    private StockWatchAdapter stockWatchAdapter;
    private List<StockBean> listStockBean;
    DatabaseHandler dbHandler;
    private static final String TAG = "StockwatchActivity";
    private String selectedStockSymbol = "";
    private SwipeRefreshLayout srlSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockwatch);
        context = this;

        tvAppFirstLoading = (TextView) findViewById(R.id.tvAppFirstLoading);
        pbAppFirstLoading = (ProgressBar) findViewById(R.id.pbAppFirstLoading);
        tvStockSymbolLoadErrorMessage = (TextView) findViewById(R.id.tvStockSymbolLoadErrorMessage);
        tvGeneralMessage = (TextView) findViewById(R.id.tvGeneralMessage);

        tvStockSymbolLoadErrorMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStockSymbol();
            }
        });

        recyclerStockWatch = (RecyclerView) findViewById(R.id.recyclerStockWatch);
        srlSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.srlSwipeRefresh);
        srlSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRecyclerContentOnSwipe();
            }
        });
        listStockBean = new ArrayList<StockBean>();
        dbHandler = new DatabaseHandler(this);
        listStockBean = dbHandler.getStockList();
        loadRecyclerView(listStockBean);
        if(dbHandler.getStockSymbolsCount().intValue() == 0)
            loadStockSymbol();
        else{
                refreshRecyclerContentOnSwipe();
        }
    }

    //Opening URL on Click of Recycler View List Item
    @Override
    public void onClick(View v) {
        int pos = recyclerStockWatch.getChildLayoutPosition(v);
        final StockBean bean = listStockBean.get(pos);
        Intent intentOpenURL = new Intent(Intent.ACTION_VIEW);
        intentOpenURL.setData(Uri.parse(this.getResources().getString(R.string.strStockURL,bean.getStockSymbol())));
        startActivity(intentOpenURL);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerStockWatch.getChildLayoutPosition(v);
        final StockBean bean = listStockBean.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.ic_action_delete);

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Delete Record from SQLite DB and refresh Recycler
                dialog.dismiss();
                boolean result = dbHandler.deleteStock(bean.getStockSymbol());
                if(!(result)){
                    Toast.makeText(StockwatchActivity.this,"Erro Deleting record",Toast.LENGTH_SHORT).show();
                }
                else{
                    listStockBean = dbHandler.getStockList();
                    loadRecyclerView(listStockBean);
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setMessage(getResources().getString(R.string.strDeleteStockBody,bean.getStockSymbol()));
        builder.setTitle(getResources().getString(R.string.strDeletStockTitle));

        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    public void loadRecyclerView(List<StockBean> listBean){
        if(listBean.size() > 0){
            recyclerStockWatch.setVisibility(View.VISIBLE);
            tvGeneralMessage.setVisibility(View.GONE);
            stockWatchAdapter = new StockWatchAdapter(listBean, this);
            recyclerStockWatch.setAdapter(stockWatchAdapter);
            recyclerStockWatch.setLayoutManager(new LinearLayoutManager(this));
            stockWatchAdapter.notifyDataSetChanged();
        }
        else{
            recyclerStockWatch.setVisibility(View.GONE);
            tvGeneralMessage.setVisibility(View.VISIBLE);
            tvGeneralMessage.setText(getResources().getString(R.string.strNoStocksAvailable));
        }

    }

    public void loadStockSymbol(){
        if(this.doNetCheck(getResources().getString(R.string.strLoadSymbolNetworkError))){
            asyncStockSymbolDownloader = new AsyncStockSymbolDownloader();
            if (!(asyncStockSymbolDownloader.getStatus() == AsyncTask.Status.RUNNING)){
                asyncStockSymbolDownloader.setListener(asyncStockSymbolDownloaderListener);
                asyncStockSymbolDownloader.execute("");
            }
        }
        else{
            tvAppFirstLoading.setVisibility(View.GONE);
            pbAppFirstLoading.setVisibility(View.GONE);
            tvStockSymbolLoadErrorMessage.setVisibility(View.VISIBLE);
            recyclerStockWatch.setVisibility(View.GONE);
            tvGeneralMessage.setVisibility(View.GONE);
        }


    }

    public void loadStock(String stockName){
        asyncStockDownloader = new AsyncStockDownloader();
        if (!(asyncStockDownloader.getStatus() == AsyncTask.Status.RUNNING)){
            asyncStockDownloader.setListener(asyncStockDownloaderListener);
            asyncStockDownloader.execute(stockName);
        }

    }

    //This method is used for updating the lists of stock
    public void updateStocks(){
        asyncStockUpdater = new AsyncStockUpdater();
        if (!(asyncStockUpdater.getStatus() == AsyncTask.Status.RUNNING)){
            asyncStockUpdater.setListener(asyncStockUpdaterListener);
            asyncStockUpdater.execute("");
        }

    }

    //This method is called when swipe to refresh is called
    public void refreshRecyclerContentOnSwipe(){
        if(doNetCheck(getResources().getString(R.string.strStockUpdateNetworkError))) {
            if(dbHandler.getStockSymbolsCount().intValue() == 0){
                loadStockSymbol();
                srlSwipeRefresh.setRefreshing(false);
            }
            else
            if(dbHandler.getStockList().size() > 0 )
                updateStocks();
            else
                srlSwipeRefresh.setRefreshing(false);
        }
        else{
            srlSwipeRefresh.setRefreshing(false);
        }
    }

    AsyncStockSymbolDownloader.AsyncStockSymbolDownloaderListener asyncStockSymbolDownloaderListener =
            new AsyncStockSymbolDownloader.AsyncStockSymbolDownloaderListener(){
        @Override
        public Context onPreDownload() {
            try{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                tvAppFirstLoading.setVisibility(View.VISIBLE);
                pbAppFirstLoading.setVisibility(View.VISIBLE);
                tvStockSymbolLoadErrorMessage.setVisibility(View.GONE);
                recyclerStockWatch.setVisibility(View.GONE);
                tvGeneralMessage.setVisibility(View.GONE);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return StockwatchActivity.this;
        }

        @Override
        public void onPostDownload(Object result) {
            try{
                //result = null;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                if(result != null){
                    recyclerStockWatch.setVisibility(View.GONE);
                    tvAppFirstLoading.setVisibility(View.GONE);
                    pbAppFirstLoading.setVisibility(View.GONE);
                    tvStockSymbolLoadErrorMessage.setVisibility(View.GONE);
                    tvGeneralMessage.setVisibility(View.GONE);
                    listStockBean = dbHandler.getStockList();
                    loadRecyclerView(listStockBean);
                }
                else{
                    tvAppFirstLoading.setVisibility(View.GONE);
                    pbAppFirstLoading.setVisibility(View.GONE);
                    tvStockSymbolLoadErrorMessage.setVisibility(View.VISIBLE);
                    recyclerStockWatch.setVisibility(View.GONE);
                    tvGeneralMessage.setVisibility(View.GONE);
                }

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    };

    AsyncStockDownloader.AsyncStockDownloaderListener asyncStockDownloaderListener = new AsyncStockDownloader.AsyncStockDownloaderListener() {
        @Override
        public Context onPreDownload() {
            return StockwatchActivity.this;
        }

        @Override
        public void onPostDownload(Object result) {
            //Update the recycler View
            if(((Boolean) result).booleanValue()){
                listStockBean = dbHandler.getStockList();
                loadRecyclerView(listStockBean);
            }
            else{
                infoDialog(getResources().getString(R.string.strDuplicateStockTitle),
                        getResources().getString(R.string.strDuplicateStockMessage,selectedStockSymbol),
                        true,R.drawable.ic_action_warning);
            }
        }
    };

    AsyncStockUpdater.AsyncStockUpdaterListener asyncStockUpdaterListener = new AsyncStockUpdater.AsyncStockUpdaterListener() {
        @Override
        public Context onPreDownload() {
            return StockwatchActivity.this;
        }

        @Override
        public void onPostDownload(Object result) {
            srlSwipeRefresh.setRefreshing(false);
            if(result != null){
                Toast.makeText(context, "Stocks Updated Successfully",Toast.LENGTH_SHORT).show();
                listStockBean = dbHandler.getStockList();
                loadRecyclerView(listStockBean);
            }
            else{
                Toast.makeText(context, "Error Updating Records. Please try again after sometime.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAddStock:
                if(doNetCheck(getResources().getString(R.string.strStockAddNetworkError))){
                    // Single input value dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    // Create an edittext and set it to be the builder's view
                    final EditText et = new EditText(this);
                    et.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    et.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                    et.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(et);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(et.getText().toString().trim().length() > 0){
                                if(doNetCheck(getResources().getString(R.string.strStockAddNetworkError))){
                                    populateSymbolList(et.getText().toString());
                                }
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    builder.setMessage("Please enter a Stock Symbol:");
                    builder.setTitle("Stock Selection");

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.setCancelable(false);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void populateSymbolList(String enteredSymbol){
        try {
            List<StockSymbolBean> listStockSymbol = dbHandler.searchStockSymbolList(enteredSymbol);
            if(listStockSymbol.size() > 1){
                final CharSequence[] sArray = new CharSequence[listStockSymbol.size()];
                final CharSequence[] sArrayKey = new CharSequence[listStockSymbol.size()];
                for (int i = 0; i < listStockSymbol.size(); i++)
                {
                    sArrayKey[i] = listStockSymbol.get(i).getStockSymbol();
                    sArray[i] = listStockSymbol.get(i).getStockSymbol() + " - " + listStockSymbol.get(i).getStockName();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Make a selection");

                // Set the builder to display the string array as a selectable
                // list, and add the "onClick" for when a selection is made
                builder.setItems(sArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        selectedStockSymbol = sArrayKey[which].toString();
                        loadStock(sArrayKey[which].toString());
                    }
                });

                builder.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();
            }
            else if(listStockSymbol.size() == 1){
                //Directly Add the stock symbol to the list of added stocks
                selectedStockSymbol = listStockSymbol.get(0).getStockSymbol();
                loadStock(listStockSymbol.get(0).getStockSymbol());
            }
            else{
                //No Matching Stock Symbols found
                infoDialog(getResources().getString(R.string.strSymbolNotFoundTitle,enteredSymbol),
                        getResources().getString(R.string.strSymbolNotFoundBody),false,0);
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //Check For internet connection. If No internet connection, it will show a dialog
    private Boolean doNetCheck(String dialogMessage) {
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
                infoDialog(getResources().getString(R.string.strTitleNoNetwork),
                        dialogMessage,false,0);
                return false;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }





    public void infoDialog(String title, String messageBody,boolean iconRequired,int icon){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        if(iconRequired)
            alertDialogBuilder.setIcon(icon);
        alertDialogBuilder.setMessage(messageBody);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
