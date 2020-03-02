package iitmad.com.a20425418.multinotepad.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import iitmad.com.a20425418.multinotepad.MainActivity;
import iitmad.com.a20425418.multinotepad.R;

/**
 * Created by Ashok Kumar - A20425418 on 9/23/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class NotePadAdapter extends RecyclerView.Adapter<NotepadViewHolder> {

    MainActivity contextMainAct;
    List<NoteBean> listNotePadBean;

    public NotePadAdapter(List<NoteBean> listBean, MainActivity conMainAct) {
        this.listNotePadBean = listBean;
        contextMainAct = conMainAct;
    }

    @Override
    public NotepadViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notepad_list_row, viewGroup, false);

        itemView.setOnClickListener(contextMainAct);
        itemView.setOnLongClickListener(contextMainAct);

        return new NotepadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotepadViewHolder notepadViewHolder, int position) {
        NoteBean bean = listNotePadBean.get(position);
        notepadViewHolder.tvTitle.setText(bean.getNoteTitle());
        if(bean.getNoteDescription().length() >= 80)
            notepadViewHolder.tvDescription.setText((bean.getNoteDescription().substring(0,80) + "..."));
                    //.replaceAll("\\\\r\\\\n|\\\\r|\\\\n|\\n|\\r"," "));//Starts at 0 and goes to end-1
        else
            notepadViewHolder.tvDescription.setText((bean.getNoteDescription().toString()));
                    //.replaceAll("\\\\r\\\\n|\\\\r|\\\\n|\\n|\\r"," "));
        SimpleDateFormat orignalFormatter = new SimpleDateFormat(contextMainAct.getString(R.string.strDateFormat));
        SimpleDateFormat formatter = new SimpleDateFormat(contextMainAct.getString(R.string.strDateFormatToBeDisplayed));

        Date date = null; //new Date();
        try {
            date = orignalFormatter.parse(bean.getNoteLastUpdated().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notepadViewHolder.tvLastUpdated.setText(formatter.format(date));
    }

    @Override
    public int getItemCount() {
        return listNotePadBean.size();
    }
}
