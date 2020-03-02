package iitmad.com.a20425418.multinotepad.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import iitmad.com.a20425418.multinotepad.R;

/**
 * Created by Ashok Kumar - A20425418 on 9/23/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class NotepadViewHolder extends RecyclerView.ViewHolder{

    public TextView tvTitle;
    public TextView tvDescription;
    public TextView tvLastUpdated;

    public NotepadViewHolder(View view) {
        super(view);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvLastUpdated = (TextView) view.findViewById(R.id.tvLastUpdated);
    }
}
