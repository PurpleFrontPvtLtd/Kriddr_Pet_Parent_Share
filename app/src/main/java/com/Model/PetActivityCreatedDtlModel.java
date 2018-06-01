package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Niranjan Reddy on 12-04-2018.
 */

public class PetActivityCreatedDtlModel implements Parcelable {
    String activity_list_id,activity,image_for_list,audio_file,details,date,month,time,image,File_act;

    protected PetActivityCreatedDtlModel(Parcel in) {
        activity_list_id = in.readString();
        activity = in.readString();
        image_for_list = in.readString();
        audio_file = in.readString();
        details = in.readString();
        date = in.readString();
        month = in.readString();
        time = in.readString();
        image = in.readString();
        File_act = in.readString();
    }

    public static final Creator<PetActivityCreatedDtlModel> CREATOR = new Creator<PetActivityCreatedDtlModel>() {
        @Override
        public PetActivityCreatedDtlModel createFromParcel(Parcel in) {
            return new PetActivityCreatedDtlModel(in);
        }

        @Override
        public PetActivityCreatedDtlModel[] newArray(int size) {
            return new PetActivityCreatedDtlModel[size];
        }
    };

    public String getFile_act() {
        return File_act;
    }

    public void setFile_act(String file_act) {
        File_act = file_act;
    }

    public String getActivity_list_id() {
        return activity_list_id;
    }

    public void setActivity_list_id(String activity_list_id) {
        this.activity_list_id = activity_list_id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getImage_for_list() {
        return image_for_list;
    }

    public void setImage_for_list(String image_for_list) {
        this.image_for_list = image_for_list;
    }

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activity_list_id);
        dest.writeString(activity);
        dest.writeString(image_for_list);
        dest.writeString(audio_file);
        dest.writeString(details);
        dest.writeString(date);
        dest.writeString(month);
        dest.writeString(time);
        dest.writeString(image);
        dest.writeString(File_act);
    }
}
