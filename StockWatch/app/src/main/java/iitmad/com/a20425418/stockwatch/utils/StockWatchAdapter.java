package iitmad.com.a20425418.stockwatch.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.List;

import iitmad.com.a20425418.stockwatch.R;
import iitmad.com.a20425418.stockwatch.StockwatchActivity;

/**
 * Created by Ashok Kumar - A20425418 on 10/10/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class StockWatchAdapter extends RecyclerView.Adapter<StockViewHolder>{

    List<StockBean> listStockBean;
    StockwatchActivity contextStockwatchActivity;

    public StockWatchAdapter(List<StockBean> listBean, StockwatchActivity stockWatchActivity){
        listStockBean = listBean;
        contextStockwatchActivity = stockWatchActivity;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_row_stock_list_item, viewGroup, false);

        itemView.setOnClickListener(contextStockwatchActivity);
        itemView.setOnLongClickListener(contextStockwatchActivity);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder stockViewHolder, int position) {
        StockBean bean = listStockBean.get(position);
        double priceChange = Double.parseDouble(bean.getStockChange().equals("null") ? "0.00" : bean.getStockChange());
        DecimalFormat df2 = new DecimalFormat("#.##");
        double priceChangePercentage = Double.parseDouble(bean.getStockChangePercent().equals("null") ? "0.00" : bean.getStockChangePercent());
        String strPriceChangeWithPercentage  = contextStockwatchActivity.getResources().
                getString(R.string.strStockPriceChangeWithPercentage,df2.format(priceChange),df2.format(priceChangePercentage));
        stockViewHolder.tvStockSymbol.setText(bean.getStockSymbol());
        stockViewHolder.tvStockLatestPrice.setText(bean.getStockLatestPrice());
        stockViewHolder.tvStockChange_ChangePercentage.setText(strPriceChangeWithPercentage);
        stockViewHolder.tvStockCompanyName.setText(bean.getStockCompanyName());
        if(priceChange > 0 ){
            stockViewHolder.tvStockSymbol.setTextColor(contextStockwatchActivity.getColor(R.color.colorGreen));
            stockViewHolder.tvStockLatestPrice.setTextColor(contextStockwatchActivity.getColor(R.color.colorGreen));
            stockViewHolder.tvStockChange_ChangePercentage.setTextColor(contextStockwatchActivity.getColor(R.color.colorGreen));
            stockViewHolder.tvStockCompanyName.setTextColor(contextStockwatchActivity.getColor(R.color.colorGreen));
            stockViewHolder.ivStockPriceChangeMarker.setImageResource(R.drawable.ic_arrow_drop_up);
        }
        else{
            stockViewHolder.tvStockSymbol.setTextColor(contextStockwatchActivity.getColor(R.color.colorRed));
            stockViewHolder.tvStockLatestPrice.setTextColor(contextStockwatchActivity.getColor(R.color.colorRed));
            stockViewHolder.tvStockChange_ChangePercentage.setTextColor(contextStockwatchActivity.getColor(R.color.colorRed));
            stockViewHolder.tvStockCompanyName.setTextColor(contextStockwatchActivity.getColor(R.color.colorRed));
            stockViewHolder.ivStockPriceChangeMarker.setImageResource(R.drawable.ic_arrow_drop_down);
        }
    }

    @Override
    public int getItemCount() {
        return listStockBean.size();
    }
}
