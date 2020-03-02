package iitmad.com.a20425418.multinotepad.utils;

import android.content.Context;
import android.os.AsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import iitmad.com.a20425418.multinotepad.R;

/**
 * Created by Ashok Kumar - A20425418 on 9/23/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class AsyncLoadNotePad extends AsyncTask<String,Void,List<NoteBean>> {

    Context context;
    AsyncLoadNotePadListener notePadListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(notePadListener != null){
            context = notePadListener.onPreDownload();
        }

    }

    @Override
    protected List<NoteBean> doInBackground(String... strings) {

        List<NoteBean> noteBeans = new ArrayList<NoteBean>();
        try {
            InputStream inputStream = context.openFileInput(context.getString(R.string.strNotePadFileName));
                       JSONFileOperation jsonOperation = new JSONFileOperation(context);
            noteBeans = jsonOperation.readJsonStream(inputStream);

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noteBeans;
    }



    @Override
    protected void onPostExecute(List<NoteBean> listNotepadBean) {
        super.onPostExecute(listNotepadBean);
        if(notePadListener != null){
            notePadListener.onPostDownload(listNotepadBean);
        }
    }

    // Setting the Listener and is set by the caller function
    public void setListener(AsyncLoadNotePadListener notePadListener) {
        this.notePadListener = notePadListener;
    }

    // Interface with methods
    public interface AsyncLoadNotePadListener {
        public Context onPreDownload();

        public void onPostDownload(Object result);

    }
}

