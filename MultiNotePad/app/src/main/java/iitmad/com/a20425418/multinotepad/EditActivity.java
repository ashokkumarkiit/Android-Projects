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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import iitmad.com.a20425418.multinotepad.utils.NoteBean;

public class EditActivity extends AppCompatActivity {

    EditText etTitle, etDescription;
    private static final String TAG = "EditActivity";
    Context context;
    List<NoteBean> listNoteBean;
    Boolean notePadSaveFlag = false, discardedByBackpress = false, freshNotePad = false;
    String previousTitle = "", previousDescription = "", previousLastUpdated = "";
    Intent intent;
    NoteBean notePadBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        listNoteBean = new ArrayList<NoteBean>();
        intent = getIntent();
        previousTitle = intent.getStringExtra(this.getString(R.string.strIntentKeyTitle));
        previousDescription = intent.getStringExtra(this.getString(R.string.strIntentKeyDescription));
        previousLastUpdated = intent.getStringExtra(this.getString(R.string.strIntentKeyLastUpdated));
        listNoteBean = this.getIntent().getExtras().getParcelableArrayList(this.getString(R.string.strIntentKeylistNoteBean));

        //Setting the flag for fresh note pad
        if (previousTitle.equals("") && previousDescription.equals(""))
            freshNotePad = true;
        else
            freshNotePad = false;

        if (previousTitle != "") {
            etTitle.setText(previousTitle);
        }
        if (previousDescription != "") {
            etDescription.setText(previousDescription);
        }

        // Setting Scroll Movement for the description edit text
        etDescription.setMovementMethod(new ScrollingMovementMethod());
        context = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSave:
                try {
                    if ((this.etTitle != null && this.etTitle.getText().toString().trim().length() > 0)) {

                        if (this.etDescription != null) {
                            if (!notePadSaveFlag && !discardedByBackpress) {
                                if (!(previousTitle.equals(etTitle.getText().toString().trim())
                                        && previousDescription.equals(etDescription.getText().toString().trim()))) {

                                    saveNotePad(etTitle.getText().toString().trim(), etDescription.getText().toString().trim(), previousLastUpdated);
                                } else {
                                    this.finish();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, this.getString(R.string.strUnTitleSaveMsg), Toast.LENGTH_SHORT).show();
                        this.finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (notePadSaveFlag) {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        //Check If there are any changes in the content , if changes display alert, else exit without alert
        if (!(this.etTitle != null && this.etTitle.getText().toString().trim().length() > 0)) {
            Toast.makeText(context, getString(R.string.strUnTitleSaveMsg), Toast.LENGTH_SHORT).show();
            this.finish();
        } else if (!(previousTitle.equals(etTitle.getText().toString().trim())
                && previousDescription.equals(etDescription.getText().toString().trim()))) {
            //Show Alert for changes
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setIcon(R.drawable.ic_warning);

            builder.setPositiveButton(getString(R.string.strButtonSave), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    saveNotePad(etTitle.getText().toString().trim(), etDescription.getText().toString().trim(), previousLastUpdated);
                }
            });
            builder.setNegativeButton(getString(R.string.strButtonDiscard), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    discardedByBackpress = true;
                    finish();
                }
            });

            builder.setMessage(this.getResources().getString(R.string.strNoteNotSaved, etTitle.getText().toString().trim()));
            builder.setTitle(getString(R.string.strDialogSaveTitle));

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private void saveNotePad(String title, String description, String lastUpdated) {
        try {

            notePadBean = new NoteBean(title.trim(), description.trim(), lastUpdated);
            Intent intent = getIntent();
            intent.putExtra(this.getString(R.string.strIntentKeylistNoteBean), notePadBean);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
