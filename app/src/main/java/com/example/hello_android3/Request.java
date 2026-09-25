package com.example.hello_android3;

public class Request {
    private int id;
    private String name;
    private String description;
    private String photo;
    private String type;
    private String status;
    private String postingDate;
    private String country;
    private int poster;
    private int actionBy;

    public Request() {
        // Default constructor
    }

    public Request(int id, String name, String description, String photo, String type, String status, String postingDate, String country, int poster, int actionBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.type = type;
        this.status = status;
        this.postingDate = postingDate;
        this.country = country;
        this.poster = poster;
        this.actionBy = actionBy;
    }

    // Getters and setters for each field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(String postingDate) {
        this.postingDate = postingDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPoster() {
        return poster;
    }

    public void setPoster(int poster) {
        this.poster = poster;
    }

    public int getActionBy() {
        return actionBy;
    }

    public void setActionBy(int actionBy) {
        this.actionBy = actionBy;
    }
}
