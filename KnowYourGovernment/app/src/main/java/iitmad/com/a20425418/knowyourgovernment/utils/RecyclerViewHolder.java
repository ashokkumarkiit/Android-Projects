package iitmad.com.a20425418.knowyourgovernment.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import iitmad.com.a20425418.knowyourgovernment.R;

/**
 * Created by Ashok Kumar - A20425418 on 10/29/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tvOfficeName;
    public TextView tvOfficialsName;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        tvOfficeName = (TextView) itemView.findViewById(R.id.tvOfficeName);
        tvOfficialsName = (TextView) itemView.findViewById(R.id.tvOfficialsName);
    }
}
