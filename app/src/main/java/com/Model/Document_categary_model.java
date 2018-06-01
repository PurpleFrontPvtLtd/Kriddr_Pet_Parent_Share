package com.Model;

import com.google.gson.annotations.SerializedName;

public class Document_categary_model {

    @SerializedName("documents_id")
    private String documents_id;
    @SerializedName("documents_name")
    private String documents_name;

    public String getDocuments_id() {
        return documents_id;
    }
    @Override
    public String toString() {
        return documents_name;
    }

    public void setDocuments_id(String documents_id) {
        this.documents_id = documents_id;
    }

    public String getDocuments_name() {
        return documents_name;
    }

    public void setDocuments_name(String documents_name) {
        this.documents_name = documents_name;
    }
}

