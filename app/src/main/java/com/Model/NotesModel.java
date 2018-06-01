package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class NotesModel implements Parcelable {

    protected NotesModel(Parcel in) {
        pet_notes_id = in.readString();
        notes = in.readString();
        created = in.readString();
    }

    public static final Creator<NotesModel> CREATOR = new Creator<NotesModel>() {
        @Override
        public NotesModel createFromParcel(Parcel in) {
            return new NotesModel(in);
        }

        @Override
        public NotesModel[] newArray(int size) {
            return new NotesModel[size];
        }
    };

    public String getPet_notes_id() {
        return pet_notes_id;
    }

    public void setPet_notes_id(String pet_notes_id) {
        this.pet_notes_id = pet_notes_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    String pet_notes_id;
    String notes;
    String created;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pet_notes_id);
        dest.writeString(notes);
        dest.writeString(created);
    }
}
