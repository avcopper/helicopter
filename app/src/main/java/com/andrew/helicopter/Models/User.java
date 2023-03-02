package com.andrew.helicopter.Models;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String login;
    private String email;
    private String password;
    private String role;

    public User() {
    }

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = "users";
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

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
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
