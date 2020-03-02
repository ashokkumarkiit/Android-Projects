package iitmad.com.a20425418.multinotepad;

/**
 * Created by Ashok Kumar - A20425418 on 9/22/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import iitmad.com.a20425418.multinotepad.utils.AsyncLoadNotePad;
import iitmad.com.a20425418.multinotepad.utils.Constants;
import iitmad.com.a20425418.multinotepad.utils.JSONFileOperation;
import iitmad.com.a20425418.multinotepad.utils.NoteBean;
import iitmad.com.a20425418.multinotepad.utils.NotePadAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    Context context;
    private static final String TAG = "MainActivity";
    RecyclerView recyclerNodePad;
    List<NoteBean> listNoteBean;
    NotePadAdapter notePadAdapter;
    AsyncLoadNotePad asyncLoadNotePad;
    TextView tvNotePadEmpty;
    Intent intent;
    Boolean freshNotePad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        recyclerNodePad = (RecyclerView) findViewById(R.id.recyclerNodePad);
        tvNotePadEmpty = (TextView) findViewById(R.id.tvNotePadEmpty);
        tvNotePadEmpty.setVisibility(View.GONE);
        listNoteBean = new ArrayList<NoteBean>();
        //Loading list of Notepad on App Start
        loadRecyclerViewList();

    }

    public void loadRecyclerViewList() {
        asyncLoadNotePad = new AsyncLoadNotePad();
        asyncLoadNotePad.setListener(notePadListener);
        asyncLoadNotePad.execute("");
    }

    public void refreshRecycler() {
        try{
            if (listNoteBean != null && listNoteBean.size() > 0) {
                recyclerNodePad.setVisibility(View.VISIBLE);
                tvNotePadEmpty.setVisibility(View.GONE);
                Collections.sort(listNoteBean, new Comparator<NoteBean>() {
                    @Override
                    public int compare(NoteBean o1, NoteBean o2) {
                        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.strDateFormat));
                        try {
                            return sdf.parse(o2.getNoteLastUpdated()).compareTo(sdf.parse(o1.getNoteLastUpdated()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                notePadAdapter = new NotePadAdapter(listNoteBean, MainActivity.this);
                recyclerNodePad.setAdapter(notePadAdapter);
                recyclerNodePad.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                notePadAdapter.notifyDataSetChanged();
            } else {
                recyclerNodePad.setVisibility(View.GONE);
                tvNotePadEmpty.setVisibility(View.VISIBLE);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRecycler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateNotePad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuCreate:
                sendToEditActivity("", "", "");
                freshNotePad = true;
                return true;
            case R.id.menuHelp:
                sendToAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerNodePad.getChildLayoutPosition(v);
        NoteBean bean = listNoteBean.get(pos);
        sendToEditActivity(bean.getNoteTitle(), bean.getNoteDescription(), bean.getNoteLastUpdated());
        freshNotePad = false;
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerNodePad.getChildLayoutPosition(v);
        final NoteBean bean = listNoteBean.get(pos);
        //Show Alert for changes
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.drawable.ic_delete);

        builder.setPositiveButton(getString(R.string.strButtonYes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Remove Note from Array List and update JSON file
                listNoteBean.remove(getItemIndexFromArrayList(bean.getNoteLastUpdated().toString()));
                if (listNoteBean.size() > 0)
                    refreshRecycler();
                else {
                    recyclerNodePad.setVisibility(View.GONE);
                    tvNotePadEmpty.setVisibility(View.VISIBLE);
                }


            }
        });
        builder.setNegativeButton(getString(R.string.strButtonNo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setMessage(this.getResources().getString(R.string.strNoteDeleteMessage, bean.getNoteTitle()));
        builder.setTitle("Delete Note");

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }

    public int getItemIndexFromArrayList(String lastUpdated) {
        int pos = -1;
        for (int i = 0; i < listNoteBean.size(); i++) {
            if (listNoteBean.get(i).getNoteLastUpdated().equals(lastUpdated)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    private void updateNotePad() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.strNotePadFileName), Context.MODE_PRIVATE);
            //Add the item in the list and pass it to write in the file
            JSONFileOperation jsonOperation = new JSONFileOperation(context);
            jsonOperation.writeJsonStream(fos, listNoteBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendToEditActivity(String title, String description, String lastUpdated) {
        intent = new Intent(context, EditActivity.class);
        intent.putExtra(this.getString(R.string.strIntentKeyTitle), title.trim());
        intent.putExtra(this.getString(R.string.strIntentKeyDescription), description.trim());
        intent.putExtra(this.getString(R.string.strIntentKeyLastUpdated), lastUpdated.trim());
        intent.putParcelableArrayListExtra(this.getString(R.string.strIntentKeylistNoteBean), (ArrayList<? extends Parcelable>) listNoteBean);
        startActivityForResult(intent, Constants.GET_LIST_NOTE_BEAN_REQUESTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GET_LIST_NOTE_BEAN_REQUESTED) {
            if (resultCode == RESULT_OK) {
                NoteBean notePadBean = data.getParcelableExtra(this.getString(R.string.strIntentKeylistNoteBean));
                //Updating the List with the bean received from Edit Activity
                try {
                    FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.strNotePadFileName), Context.MODE_PRIVATE);

                    //Add the item in the list
                    DateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.strDateFormat));
                    Date date = new Date();
                    if (freshNotePad) {
                        notePadBean.setNoteLastUpdated(dateFormat.format(date));
                        listNoteBean.add(notePadBean);
                    } else {
                        int indexToBeChanged = getItemIndexFromArrayList(notePadBean.getNoteLastUpdated());

                        notePadBean.setNoteLastUpdated(dateFormat.format(date));
                        listNoteBean.set(indexToBeChanged, notePadBean);
                    }
                    refreshRecycler();
                    Toast.makeText(context, getString(R.string.strNotePadSaved), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                refreshRecycler();
            }
        }
    }

    public void sendToAboutActivity() {
        intent = new Intent(context, AboutActivity.class);
        startActivity(intent);
    }

    /*public List<NoteBean> readNotePads() {
        List<NoteBean> noteBeans = new ArrayList<NoteBean>();
        try {
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.strNotePadFileName));
            JSONFileOperation jsonOperation = new JSONFileOperation(context);
            noteBeans = jsonOperation.readJsonStream(inputStream);
        } catch (FileNotFoundException fnfe) {
            //Show Toast
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //Show Toast
        }
        return noteBeans;
    }*/

    AsyncLoadNotePad.AsyncLoadNotePadListener notePadListener = new AsyncLoadNotePad.AsyncLoadNotePadListener() {
        @Override
        public Context onPreDownload() {
            return MainActivity.this;
        }

        @Override
        public void onPostDownload(Object result) {
            if (result != null) {
                listNoteBean = (List<NoteBean>) (Object) result;
                if (listNoteBean.size() > 0) {
                    recyclerNodePad.setVisibility(View.VISIBLE);
                    tvNotePadEmpty.setVisibility(View.GONE);
                    refreshRecycler();
                } else {
                    recyclerNodePad.setVisibility(View.GONE);
                    tvNotePadEmpty.setVisibility(View.VISIBLE);

                }
            }

        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
