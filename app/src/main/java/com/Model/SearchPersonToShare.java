package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchPersonToShare implements Parcelable {
    String id,name,mobile,type,photo;


    protected SearchPersonToShare(Parcel in) {
        id = in.readString();
        name = in.readString();
        mobile = in.readString();
        type = in.readString();
        photo = in.readString();
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
    }
}
