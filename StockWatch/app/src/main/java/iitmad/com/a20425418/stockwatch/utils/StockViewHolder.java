package iitmad.com.a20425418.stockwatch.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import iitmad.com.a20425418.stockwatch.R;


/**
 * Created by Ashok Kumar - A20425418 on 10/10/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class StockViewHolder extends RecyclerView.ViewHolder{

    public TextView tvStockSymbol;
    public TextView  tvStockLatestPrice;
    public TextView tvStockChange_ChangePercentage;
    public TextView tvStockCompanyName;
    public ImageView ivStockPriceChangeMarker;
    //public RelativeLayout rlStockListRowParent;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);
        tvStockSymbol = (TextView) itemView.findViewById(R.id.tvStockSymbol);
        tvStockLatestPrice = (TextView) itemView.findViewById(R.id.tvStockLatestPrice);
        tvStockChange_ChangePercentage = (TextView) itemView.findViewById(R.id.tvStockChange_ChangePercentage);
        tvStockCompanyName = (TextView) itemView.findViewById(R.id.tvStockCompanyName);
        ivStockPriceChangeMarker = (ImageView) itemView.findViewById(R.id.ivStockPriceChangeMarker);
    }
}
