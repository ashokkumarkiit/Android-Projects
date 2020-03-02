package iitmad.com.a20425418.stockwatch.utils;

/**
 * Created by Ashok Kumar - A20425418 on 10/10/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class StockBean {
    public String stockSymbol;
    public String stockCompanyName;
    public String stockLatestPrice;
    public String stockChange;
    public String stockChangePercent;

    public StockBean() {
    }

    public StockBean(String stockSymbol, String stockCompanyName, String stockLatestPrice, String stockChange, String stockChangePercent) {
        this.stockSymbol = stockSymbol;
        this.stockCompanyName = stockCompanyName;
        this.stockLatestPrice = stockLatestPrice;
        this.stockChange = stockChange;
        this.stockChangePercent = stockChangePercent;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockCompanyName() {
        return stockCompanyName;
    }

    public void setStockCompanyName(String stockCompanyName) {
        this.stockCompanyName = stockCompanyName;
    }

    public String getStockLatestPrice() {
        return stockLatestPrice;
    }

    public void setStockLatestPrice(String stockLatestPrice) {
        this.stockLatestPrice = stockLatestPrice;
    }

    public String getStockChange() {
        return stockChange;
    }

    public void setStockChange(String stockChange) {
        this.stockChange = stockChange;
    }

    public String getStockChangePercent() {
        return stockChangePercent;
    }

    public void setStockChangePercent(String stockChangePercent) {
        this.stockChangePercent = stockChangePercent;
    }
}
