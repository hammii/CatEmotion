package com.example.catemotion;

public class UserList {
    private String email;
    private String id;
    private String fileUrl;

    public UserList(){}

    public UserList(String email, String id, String fileUrl){
        this.email = email;
        this.id = id;
        this.fileUrl = fileUrl;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getNickName(){
        return id;
    }

    public void setNickName(String id){
        this.id = id;
    }

    public String getFileUrl(){
        return fileUrl;
    }

    public void setFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
    }
}
