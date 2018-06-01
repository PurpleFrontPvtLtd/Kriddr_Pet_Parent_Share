package com.Model;

import java.util.List;

/**
 * Created by Niranjan Reddy on 07-03-2018.
 */

public class Client_collection_model {
    List<Client_info_Model> response;

    public List<Client_info_Model> getResponse() {
        return response;
    }

    public void setResponse(List<Client_info_Model> response) {
        this.response = response;
    }
}
