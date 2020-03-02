package iitmad.com.a20425418.multinotepad.utils;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import iitmad.com.a20425418.multinotepad.R;

/**
 * Created by Ashok Kumar - A20425418 on 9/22/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */
public class JSONFileOperation {

    public Context context;
    public JSONFileOperation(Context con){
        context = con;
    }

    /* Start of Write Operation on Json File */

    public void writeJsonStream(OutputStream out, List<NoteBean> noteBeans) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out,context.getString(R.string.strNotePadFileEncoding)));
        writer.setIndent("  ");
        writeNotePadArray(writer, noteBeans);
        writer.close();
    }

    public void writeNotePadArray(JsonWriter writer, List<NoteBean> noteBeans) throws IOException {
        writer.beginArray();
        for (NoteBean noteBean : noteBeans) {
            writeNotePad(writer, noteBean);
        }
        writer.endArray();
    }

    public void writeNotePad(JsonWriter writer, NoteBean noteBean) throws IOException {
        try{
        writer.beginObject();
            writer.name(context.getString(R.string.strJsonKeyTitle)).value(noteBean.getNoteTitle());
            writer.name(context.getString(R.string.strJsonKeyDescription)).value(noteBean.getNoteDescription());
            writer.name(context.getString(R.string.strJsonKeyLastUpdated)).value(noteBean.getNoteLastUpdated());
            writer.endObject();}
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /* End of Write Operation on Json File */

    /* Start of Read Operation on Json File */
    public List<NoteBean> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, context.getString(R.string.strNotePadFileEncoding)));
        try {
            return readNotePadArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<NoteBean> readNotePadArray(JsonReader reader) throws IOException {
        List<NoteBean> listNoteBeans = new ArrayList<NoteBean>();

        reader.beginArray();
        while (reader.hasNext()) {
            listNoteBeans.add(readNotePad(reader));
        }
        reader.endArray();
        return listNoteBeans;
    }

    public NoteBean readNotePad(JsonReader reader) throws IOException {
        String title = null;
        String description = null;
        String lastUpdated = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(context.getString(R.string.strJsonKeyTitle))) {
                title = reader.nextString();
            } else if (name.equals(context.getString(R.string.strJsonKeyDescription))) {
                description = reader.nextString();
            } else if (name.equals(context.getString(R.string.strJsonKeyLastUpdated))) {
                lastUpdated = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new NoteBean(title,description,lastUpdated);
    }

    /* End of Read Operation on Json File */
}
