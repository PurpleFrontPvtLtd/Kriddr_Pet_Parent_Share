package com.Model;

import java.util.List;

public class NotificationModel {
    String reaction_id;
    String type;
    String notification;
    String photo;

    public List<Nfy_Variable_Model> getVariables() {
        return variables;
    }

    public void setVariables(List<Nfy_Variable_Model> variables) {
        this.variables = variables;
    }

    List<Nfy_Variable_Model> variables;
    public String getReacted_pet_name() {
        return reacted_pet_name;
    }

    public void setReacted_pet_name(String reacted_pet_name) {
        this.reacted_pet_name = reacted_pet_name;
    }

    String reacted_pet_name;
    String created;

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    String read_status;

    public String getReaction_id() {
        return reaction_id;
    }

    public void setReaction_id(String reaction_id) {
        this.reaction_id = reaction_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
