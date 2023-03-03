package com.andrew.helicopter.Models;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String login;
    private String email;
    private String password;
    private String role;
    private boolean isVisibleSectionDetail;
    private boolean isVisibleSectionWorks;
    private boolean isVisibleSectionTimes;

    public User() {
    }

    public User(String login, String email, String password,
                boolean isVisibleSectionDetail, boolean isVisibleSectionWorks, boolean isVisibleSectionTimes)
    {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = "users";
        this.isVisibleSectionDetail = isVisibleSectionDetail;
        this.isVisibleSectionWorks = isVisibleSectionWorks;
        this.isVisibleSectionTimes = isVisibleSectionTimes;
    }

    public boolean isAdmin() {
        return Objects.equals(getRole(), "admin");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVisibleSectionDetail() {
        return isVisibleSectionDetail;
    }

    public void setVisibleSectionDetail(boolean visibleSectionDetail) {
        isVisibleSectionDetail = visibleSectionDetail;
    }

    public boolean isVisibleSectionWorks() {
        return isVisibleSectionWorks;
    }

    public void setVisibleSectionWorks(boolean visibleSectionWorks) {
        isVisibleSectionWorks = visibleSectionWorks;
    }

    public boolean isVisibleSectionTimes() {
        return isVisibleSectionTimes;
    }

    public void setVisibleSectionTimes(boolean visibleSectionTimes) {
        isVisibleSectionTimes = visibleSectionTimes;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", isVisibleSectionDetail=" + isVisibleSectionDetail +
                ", isVisibleSectionWorks=" + isVisibleSectionWorks +
                ", isVisibleSectionTimes=" + isVisibleSectionTimes +
                '}';
    }

    /**
     * Возвращает объект в виде json-строки
     * @return
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);

//        return "{" +
//                "\"login\": \"" + login + "\"," +
//                "\"email\": \"" + email + "\"," +
//                "\"password\": \"" + password + "\"," +
//                "\"role\": \"" + role + "\"" +
//                "}";
    }
}
