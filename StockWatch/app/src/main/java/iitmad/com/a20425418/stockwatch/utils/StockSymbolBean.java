package iitmad.com.a20425418.stockwatch.utils;

/**
 * Created by Ashok Kumar - A20425418 on 10/10/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class StockSymbolBean {

    public String stockSymbol;
    public String stockName;

    public StockSymbolBean(String stockSymbol, String stockName) {
        this.stockSymbol = stockSymbol;
        this.stockName = stockName;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
