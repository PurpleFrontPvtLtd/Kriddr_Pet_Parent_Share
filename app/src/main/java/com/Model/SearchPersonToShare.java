package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchPersonToShare implements Parcelable {
    String id;
    String name;
    String mobile;
    String type;
    String photo;

    protected SearchPersonToShare(Parcel in) {
        id = in.readString();
        name = in.readString();
        mobile = in.readString();
        type = in.readString();
        photo = in.readString();
        result = in.readString();
        share_status = in.readString();
    }

    public static final Creator<SearchPersonToShare> CREATOR = new Creator<SearchPersonToShare>() {
        @Override
        public SearchPersonToShare createFromParcel(Parcel in) {
            return new SearchPersonToShare(in);
        }

        @Override
        public SearchPersonToShare[] newArray(int size) {
            return new SearchPersonToShare[size];
        }
    };

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    String result;

    public String getShare_status() {
        return share_status;
    }

    public void setShare_status(String share_status) {
        this.share_status = share_status;
    }

    String share_status;



    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(type);
        dest.writeString(photo);
        dest.writeString(result);
        dest.writeString(share_status);
    }
}
