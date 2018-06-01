package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Niranjan Reddy on 17-03-2018.
 */

public class Pet_Search_Details_Model implements Parcelable {
    String owner_id,owner_name,pet_id,pet_name,photo,follow_status,comment,created;


    protected Pet_Search_Details_Model(Parcel in) {
        owner_id = in.readString();
        owner_name = in.readString();
        pet_id = in.readString();
        pet_name = in.readString();
        photo = in.readString();
        follow_status = in.readString();
        comment = in.readString();
        created = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(owner_id);
        dest.writeString(owner_name);
        dest.writeString(pet_id);
        dest.writeString(pet_name);
        dest.writeString(photo);
        dest.writeString(follow_status);
        dest.writeString(comment);
        dest.writeString(created);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pet_Search_Details_Model> CREATOR = new Creator<Pet_Search_Details_Model>() {
        @Override
        public Pet_Search_Details_Model createFromParcel(Parcel in) {
            return new Pet_Search_Details_Model(in);
        }

        @Override
        public Pet_Search_Details_Model[] newArray(int size) {
            return new Pet_Search_Details_Model[size];
        }
    };

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFollow_status() {
        return follow_status;
    }

    public void setFollow_status(String follow_status) {
        this.follow_status = follow_status;
    }


}
