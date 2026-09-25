package com.example.hello_android3;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String dateOfBirth;
    private String country;
    private String phoneNumber;
    private String securityQuestion;
    private String securityAnswer;
    private String profilePhoto;
    private String gender;
    private Integer admin;
    private Integer banned;
    private Integer firstLogin;
    // Constructor
    public User(int id, String firstName, String lastName, String email, String password, String dateOfBirth,
                String country, String phoneNumber, String securityQuestion, String securityAnswer,
                String profilePhoto, String gender,Integer admin,Integer banned,Integer firstLogin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.profilePhoto = profilePhoto;
        this.gender = gender;
        this.admin=admin;
        this.banned= banned;
        this.firstLogin=firstLogin;
    }

    // Getters and setters for all fields
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }
    public Integer getFirstLogin(){
        return firstLogin;
    }
    public void setFirstLogin(Integer firstLogin){
        this.firstLogin=firstLogin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getAdmin() {
        return admin;
    }
    public int getBanned() {
        return banned;
    }


    public void setBanned(int banned) {
        this.banned = banned;
    }
}
