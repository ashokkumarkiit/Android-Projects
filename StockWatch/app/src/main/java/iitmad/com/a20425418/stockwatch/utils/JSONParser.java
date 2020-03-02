package iitmad.com.a20425418.stockwatch.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Ashok Kumar - A20425418 on 10/12/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class JSONParser {

    //Method is used for parsing JSON obtained from calling StockSymbol API
    public static ArrayList<StockSymbolBean> parseStockSymbolJSON(String jsonString) {

        ArrayList<StockSymbolBean> stockSymbolsList = new ArrayList<>();
        try {
            JSONArray jsonStockSymbolList = new JSONArray(jsonString);

            for (int i = 0; i < jsonStockSymbolList.length(); i++) {
                JSONObject jsonStockSymbol = (JSONObject) jsonStockSymbolList.get(i);
                String symbol = jsonStockSymbol.getString("symbol");
                String name = jsonStockSymbol.getString("name");
                StockSymbolBean bean = new StockSymbolBean(symbol,name);
                stockSymbolsList.add(bean);
            }
            return stockSymbolsList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Method is used for parsing JSON obtained from calling Stock API
    public static ArrayList<StockBean> parseStockJSON(String jsonString) {

        ArrayList<StockBean> stockList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> iter = jsonObject.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                //Object value = json.get(key);
                String symbol = jsonObject.getString("symbol");
                String companyName = jsonObject.getString("companyName");
                String latestPrice = jsonObject.getString("latestPrice");
                String change = jsonObject.getString("change");
                String changePercent = jsonObject.getString("changePercent");
                StockBean bean = new StockBean(symbol,companyName,latestPrice,change,changePercent);
                stockList.add(bean);
            }

            return stockList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
