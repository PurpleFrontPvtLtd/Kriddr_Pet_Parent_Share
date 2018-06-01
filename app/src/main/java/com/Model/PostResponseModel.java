package com.Model;

import java.util.List;

/**
 * Created by Niranjan Reddy on 17-03-2018.
 */

public class PostResponseModel {
     List<PostDetailModel> posts_details;
    CountModel count_details;

    public CountModel getCount_details() {
        return count_details;
    }

    public void setCount_details(CountModel count_details) {
        this.count_details = count_details;
    }

    public List<PostDetailModel> getPosts_details() {
        return posts_details;
    }

    public void setPosts_details(List<PostDetailModel> posts_details) {
        this.posts_details = posts_details;
    }
}
