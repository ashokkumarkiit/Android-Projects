package iitmad.com.a20425418.knowyourgovernment.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import iitmad.com.a20425418.knowyourgovernment.MainActivity;
import iitmad.com.a20425418.knowyourgovernment.R;
import iitmad.com.a20425418.knowyourgovernment.beans.GoogleCivicBean;
import iitmad.com.a20425418.knowyourgovernment.beans.RecyclerClassBean;

/**
 * Created by Ashok Kumar - A20425418 on 10/29/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    List<RecyclerClassBean> listRecyclerClassBean;
    MainActivity contextMainActivity;

    public RecyclerAdapter(List<RecyclerClassBean> listBean, MainActivity conMainActivity){

        this.listRecyclerClassBean = listBean;
        this.contextMainActivity = conMainActivity;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_row_item, viewGroup, false);

        itemView.setOnClickListener(contextMainActivity);

        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        RecyclerClassBean bean = listRecyclerClassBean.get(position);
        recyclerViewHolder.tvOfficeName.setText(bean.getTvOfficeName());
        if(bean.getTvOfficialsParty().toUpperCase().equals("UNKNOWN"))
            recyclerViewHolder.tvOfficialsName.setText(contextMainActivity.getResources().
                    getString(R.string.strRecyclerOfficialsNameWithoutParty,bean.getTvOfficialsName()));
        else
            recyclerViewHolder.tvOfficialsName.setText(contextMainActivity.getResources().
                getString(R.string.strRecyclerOfficialsName,bean.getTvOfficialsName(),bean.getTvOfficialsParty()));


    }

    @Override
    public int getItemCount() {
        return listRecyclerClassBean.size();
    }
}
