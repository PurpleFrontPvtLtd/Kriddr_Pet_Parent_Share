package com.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niranjan Reddy on 17-03-2018.
 */

public class PostDetailModel implements Parcelable {

    String pet_posts_id;
    String description;
    String image;
    String created;
    String pet_name;
    String pet_photo;
    String pet_id;
    String likes_counts;
    String comments_counts;
    String share_counts;
    String like_status;
    String created_time;

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    String owner_id;
    ArrayList<SharedByModel> shared_by;

    public static final Creator<PostDetailModel> CREATOR = new Creator<PostDetailModel>() {
        @Override
        public PostDetailModel createFromParcel(Parcel in) {
            return new PostDetailModel(in);
        }

        @Override
        public PostDetailModel[] newArray(int size) {
            return new PostDetailModel[size];
        }
    };

    public ArrayList<SharedByModel> getShared_by() {
        return shared_by;
    }

    public void setShared_by(ArrayList<SharedByModel> shared_by) {
        this.shared_by = shared_by;
    }

    protected PostDetailModel(Parcel in) {
        pet_posts_id = in.readString();
        description = in.readString();
        image = in.readString();
        created = in.readString();
        pet_name = in.readString();
        pet_photo = in.readString();
        pet_id = in.readString();
        likes_counts = in.readString();
        comments_counts = in.readString();
        share_counts = in.readString();
        like_status = in.readString();
        created_time = in.readString();
    }


    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }



    public PostDetailModel() {

    }


    public String getLikes_counts() {
        return likes_counts;
    }

    public void setLikes_counts(String likes_counts) {
        this.likes_counts = likes_counts;
    }

    public String getComments_counts() {
        return comments_counts;
    }

    public void setComments_counts(String comments_counts) {
        this.comments_counts = comments_counts;
    }

    public String getShare_counts() {
        return share_counts;
    }

    public void setShare_counts(String share_counts) {
        this.share_counts = share_counts;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getPet_posts_id() {
        return pet_posts_id;
    }

    public void setPet_posts_id(String pet_posts_id) {
        this.pet_posts_id = pet_posts_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_photo() {
        return pet_photo;
    }

    public void setPet_photo(String pet_photo) {
        this.pet_photo = pet_photo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pet_posts_id);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(created);
        dest.writeString(pet_name);
        dest.writeString(pet_photo);
        dest.writeString(pet_id);
        dest.writeString(likes_counts);
        dest.writeString(comments_counts);
        dest.writeString(share_counts);
        dest.writeString(like_status);

        dest.writeString(created_time);
    }
}
