package com.Model;

import java.util.List;

public class PetDetailsModel {
    List<Client_info_Model> pet_details;
    List<DocumentModel> documents_list;
    List<NotesModel> notes_list;
    String shared_with;

    public List<Client_info_Model> getPet_details() {
        return pet_details;
    }

    public void setPet_details(List<Client_info_Model> pet_details) {
        this.pet_details = pet_details;
    }

    public List<DocumentModel> getDocuments_list() {
        return documents_list;
    }

    public void setDocuments_list(List<DocumentModel> documents_list) {
        this.documents_list = documents_list;
    }

    public List<NotesModel> getNotes_list() {
        return notes_list;
    }

    public void setNotes_list(List<NotesModel> notes_list) {
        this.notes_list = notes_list;
    }

    public String getShared_with() {
        return shared_with;
    }

    public void setShared_with(String shared_with) {
        this.shared_with = shared_with;
    }
}
