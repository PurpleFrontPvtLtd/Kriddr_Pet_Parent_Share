package com.Model;

public class NotificationModel {
    String reaction_id;
    String type;
    String notification;
    String photo;

    public String getReacted_pet_name() {
        return reacted_pet_name;
    }

    public void setReacted_pet_name(String reacted_pet_name) {
        this.reacted_pet_name = reacted_pet_name;
    }

    String reacted_pet_name;
    String created;

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
