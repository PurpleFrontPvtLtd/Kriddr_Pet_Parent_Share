package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pf-05 on 1/31/2018.
 */

public class UserModel implements Parcelable {

    String owner_id;
    String owner_name;
    String mobile;
    String email;
    String address;
    String preferred_contact;
    String status;
    String photo="";
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String img_url) {
        this.photo = img_url;
    }



    public UserModel(){

    }
    protected UserModel(Parcel in) {
        owner_id = in.readString();
        owner_name = in.readString();
        mobile = in.readString();
        email = in.readString();
        address = in.readString();
        preferred_contact = in.readString();
        status = in.readString();
        photo = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreferred_contact() {
        return preferred_contact;
    }

    public void setPreferred_contact(String preferred_contact) {
        this.preferred_contact = preferred_contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(owner_id);
        parcel.writeString(owner_name);
        parcel.writeString(mobile);
        parcel.writeString(email);
        parcel.writeString(address);
        parcel.writeString(preferred_contact);
        parcel.writeString(status);
        parcel.writeString(photo);
    }
}
