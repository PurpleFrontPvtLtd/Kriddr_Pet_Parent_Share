package com.Model;

import java.util.List;

/**
 * Created by Niranjan Reddy on 12-04-2018.
 */

public class PetActivityListResponse {
    List<PetActivityCreatedDtlModel> response;

    public List<PetActivityCreatedDtlModel> getResponse() {
        return response;
    }

    public void setResponse(List<PetActivityCreatedDtlModel> response) {
        this.response = response;
    }
}
