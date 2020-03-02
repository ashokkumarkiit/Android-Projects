package iitmad.com.a20425418.stockwatch.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ashok Kumar - A20425418 on 10/12/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    public Context context;
    private SQLiteDatabase database;
    private static final String TAG = "DatabaseHandler";

    //Database Table Names
    private static final String TABLE_STOCK_WATCH = "StockWatchTable";
    private static final String TABLE_STOCK_SYMBOL = "StockSymbolTable";

    //DB Columns for StockSymbol Table
    private static final String SST_SYMBOL = "StockSymbol";
    private static final String SST_NAME = "StockName";

    //DB Columns for StockWatch Table
    private static final String SWT_SYMBOL = "StockSymbol";
    private static final String SWT_COMPANY_NAME = "StockCompanyName";
    private static final String SWT_LATEST_PRICE = "StockLatestPrice";
    private static final String SWT_CHANGE = "StockChange";
    private static final String SWT_CHANGE_PERCENT = "StockChangePercent";


    // DB Create Table for StockSymbol
    private static final String SQL_CREATE_TABLE_STOCK_SYMBOL =
            "CREATE TABLE " + TABLE_STOCK_SYMBOL + " (" +
                    SST_SYMBOL + " TEXT not null unique," +
                    SST_NAME + " TEXT not null )";

    // DB Create Table for StockSymbol
    private static final String SQL_CREATE_TABLE_STOCK_WATCH =
            "CREATE TABLE " + TABLE_STOCK_WATCH + " (" +
                    SWT_SYMBOL + " TEXT not null unique," +
                    SWT_COMPANY_NAME + " TEXT not null, " +
                    SWT_LATEST_PRICE + " TEXT not null, " +
                    SWT_CHANGE + " TEXT not null, " +
                    SWT_CHANGE_PERCENT + " TEXT not null)";

    //Deleting Table StockSymbol
    private static final String SQL_DELETE_TABLE_STOCK_SYMBOL =
            "DROP TABLE IF EXISTS " + TABLE_STOCK_SYMBOL;

    //Deleting Table StockWatch
    private static final String SQL_DELETE_TABLE_STOCK_WATCH =
            "DROP TABLE IF EXISTS " + TABLE_STOCK_WATCH;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_STOCK_SYMBOL);
        db.execSQL(SQL_CREATE_TABLE_STOCK_WATCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABLE_STOCK_SYMBOL);
        db.execSQL(SQL_DELETE_TABLE_STOCK_WATCH);
        onCreate(db);
    }

    /************ STOCK SYMBOL TABLE ***************/

    //This method is used for inserting data into stockSymbol Table in database taking dtocksymbollist as input
    public void insertStockSymbolList( List<StockSymbolBean> listStockSymbolBean){
        ContentValues values;
        int insertCounter = 0;
        for(int i = 0; i< listStockSymbolBean.size(); i++){
            StockSymbolBean bean = listStockSymbolBean.get(i);
            values = new ContentValues();
            values.put(SST_SYMBOL, bean.getStockSymbol());
            values.put(SST_NAME, bean.getStockName());
            long key = database.insert(TABLE_STOCK_SYMBOL, null, values);
            if(key == -1)
                insertCounter--;
            else
                insertCounter++;
        }
        Log.d(TAG, "insertStockSymbolList: "+ insertCounter);
    }


    //This method is used for fetching the list of all stock symbol lists from database
    public List<StockSymbolBean> getStockSymbolList(){
        List<StockSymbolBean> listStockSymbol = new ArrayList<StockSymbolBean>();
        Cursor cursor = database.query(
                TABLE_STOCK_SYMBOL,  // The table to query
                new String[]{SST_SYMBOL, SST_NAME}, // The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String name = cursor.getString(1);
                StockSymbolBean bean = new StockSymbolBean(symbol, name);
                listStockSymbol.add(bean);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return listStockSymbol;
    }

    //This method is used for fetching the list of all stock symbol lists from database
    public List<StockSymbolBean> searchStockSymbolList(String searchString){
        String[] stringParam = new String[1];
        stringParam[0] = searchString;
        List<StockSymbolBean> listStockSymbol = new ArrayList<StockSymbolBean>();

        String sql = "SELECT * FROM " + TABLE_STOCK_SYMBOL + " WHERE " + SST_SYMBOL + " LIKE '%" + searchString + "%' OR " + SST_NAME + " LIKE '%" + searchString + "%'";
        Cursor cursor = database.rawQuery(sql, null);


        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String name = cursor.getString(1);
                StockSymbolBean bean = new StockSymbolBean(symbol, name);
                listStockSymbol.add(bean);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return listStockSymbol;
    }


    //This method is used for fetching the count of all stock symbols from database
    public Integer getStockSymbolsCount(){

        int noOfSymbols = 0;
        Cursor cursor = database.query(
                TABLE_STOCK_SYMBOL,  // The table to query
                new String[]{SST_SYMBOL, SST_NAME},null,null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            noOfSymbols = cursor.getCount();
            cursor.close();
        }
        return noOfSymbols;

    }

    //This method is used for deleting the stock Symbol table from database.
    public void deleteStockSymbolList() {
        try{
            int cnt = database.delete(TABLE_STOCK_SYMBOL,null,null);
            Log.d(TAG, "deleteStockSymbolList: " + cnt);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /************ STOCK TABLE ***************/

    public boolean insertStock(StockBean stockBean){
        try {
            ContentValues values;
            values = new ContentValues();
            values.put(SWT_SYMBOL, stockBean.getStockSymbol());
            values.put(SWT_COMPANY_NAME, stockBean.getStockCompanyName());
            values.put(SWT_LATEST_PRICE, stockBean.getStockLatestPrice());
            values.put(SWT_CHANGE, stockBean.getStockChange());
            values.put(SWT_CHANGE_PERCENT, stockBean.getStockChangePercent());

            long key = database.insert(TABLE_STOCK_WATCH, null, values);
            Log.d(TAG, "insertStockSymbolList: " + key);
            if(key == -1)
                return false;
        }
        catch(SQLiteConstraintException excon){
            excon.printStackTrace();
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //This method is used for fetching the list of all stock present in the user DB
    public List<StockBean> getStockList(){
        List<StockBean> listStocks = new ArrayList<StockBean>();
        Cursor cursor = database.query(
                TABLE_STOCK_WATCH,  // The table to query
                new String[]{SWT_SYMBOL, SWT_COMPANY_NAME,SWT_LATEST_PRICE,SWT_CHANGE,SWT_CHANGE_PERCENT},
                null, null,null,null,SWT_SYMBOL+" ASC");

        if (cursor != null) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String name = cursor.getString(1);
                String latestPrice = cursor.getString(2);
                String change = cursor.getString(3);
                String changePercent = cursor.getString(4);
                StockBean bean = new StockBean(symbol, name,latestPrice,change,changePercent);
                listStocks.add(bean);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listStocks;
    }

    public boolean deleteStock(String name) {
        try{
            int cnt = database.delete(TABLE_STOCK_WATCH, SWT_SYMBOL + " = ?", new String[]{name});
            if(cnt > 0) {
                //Row Deleted
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return false;


    }

    public boolean updateStockList(StockBean stockBean){
        try {
            ContentValues values;
            values = new ContentValues();
            values.put(SWT_SYMBOL, stockBean.getStockSymbol());
            values.put(SWT_COMPANY_NAME, stockBean.getStockCompanyName());
            values.put(SWT_LATEST_PRICE, stockBean.getStockLatestPrice());
            values.put(SWT_CHANGE, stockBean.getStockChange());
            values.put(SWT_CHANGE_PERCENT, stockBean.getStockChangePercent());

            long key = database.update(TABLE_STOCK_WATCH,values,SWT_SYMBOL + " = ?", new String[]{stockBean.getStockSymbol()});
            Log.d(TAG, "dataUpdated: " + key);
            if(key < 1)
                return false;
        }
        catch(SQLiteConstraintException excon){
            excon.printStackTrace();
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
