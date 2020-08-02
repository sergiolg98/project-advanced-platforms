package com.project.models;

public class ImageModel {

    private String userId;
    private String imageBase64;
    private String username;
    private String description;

    public ImageModel(String userId, String imageBase64, String username, String description) {
        this.userId = userId;
        this.imageBase64 = imageBase64;
        this.username = username;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
