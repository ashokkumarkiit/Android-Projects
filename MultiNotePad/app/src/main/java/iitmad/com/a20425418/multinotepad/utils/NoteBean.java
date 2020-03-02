package iitmad.com.a20425418.multinotepad.utils;

/**
 * Created by Ashok Kumar - A20425418 on 9/22/18.
 * Illinois Institute of Technology
 * fashokkumar@hawk.iit.edu
 */

import android.os.Parcel;
import android.os.Parcelable;

public class NoteBean implements Parcelable {

    public String noteTitle;
    public String noteDescription;
    public String noteLastUpdated;

    public NoteBean() {
    }

    public NoteBean(String noteTitle, String noteDescription, String noteLastUpdated) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.noteLastUpdated = noteLastUpdated;
    }

    protected NoteBean(Parcel in) {
        noteTitle = in.readString();
        noteDescription = in.readString();
        noteLastUpdated = in.readString();
    }

    public static final Creator<NoteBean> CREATOR = new Creator<NoteBean>() {
        @Override
        public NoteBean createFromParcel(Parcel in) {
            return new NoteBean(in);
        }

        @Override
        public NoteBean[] newArray(int size) {
            return new NoteBean[size];
        }
    };

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public String getNoteLastUpdated() {
        return noteLastUpdated;
    }

    public void setNoteLastUpdated(String noteLastUpdated) {
        this.noteLastUpdated = noteLastUpdated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteTitle);
        dest.writeString(noteDescription);
        dest.writeString(noteLastUpdated);
    }
}
