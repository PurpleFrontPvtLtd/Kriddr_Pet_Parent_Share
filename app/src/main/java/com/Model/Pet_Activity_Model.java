package com.Model;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Niranjan Reddy on 06-04-2018.
 */

public class Pet_Activity_Model {


    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("image_selected")
    private String image_selected;
    private Bitmap act_sel_bmp,act_un_sel_bmp;
/*
    public Bitmap getAct_sel_bmp() {
        return act_sel_bmp;
    }

    public void setAct_sel_bmp(Bitmap act_sel_bmp) {
        this.act_sel_bmp = act_sel_bmp;
    }

    public Bitmap getAct_un_sel_bmp() {
        return act_un_sel_bmp;
    }

    public void setAct_un_sel_bmp(Bitmap act_un_sel_bmp) {
        this.act_un_sel_bmp = act_un_sel_bmp;
    }*/

    boolean isSelected;

    String file_sel,file_unSel;



    public String getFile_sel() {
        return file_sel;
    }

    public void setFile_sel(String file_sel) {
        this.file_sel = file_sel;
    }

    public String getFile_unSel() {
        return file_unSel;
    }

    public void setFile_unSel(String file_unSel) {
        this.file_unSel = file_unSel;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_selected() {
        return image_selected;
    }

    public void setImage_selected(String image_selected) {
        this.image_selected = image_selected;
    }
}
