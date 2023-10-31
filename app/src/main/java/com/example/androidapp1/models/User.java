package com.example.androidapp1.models;

import com.example.androidapp1.service.UsersFirebaseManager;

import java.util.ArrayList;

public class User {
    private String username, email, pass, nickname;
    private String id;
    private ArrayList<Role> roles;

    public User(){
        roles = new ArrayList<>();
    }

    public User(String nickname, String email, String pass) {
        this.nickname = nickname;
        this.email = email;
        this.pass = pass;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", pass='" + pass + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void changeName(String newName){
        UsersFirebaseManager usersFirebaseManager = new UsersFirebaseManager();
        this.nickname = newName;
        usersFirebaseManager.updateUser(this.id, this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }
}
