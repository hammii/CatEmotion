package com.example.catemotion;

public class Post {
    private String postName;
    private String userNickName;
    private String contents;
    private long like_count;

    public Post(){

    }

    public String getPostName(){
        return postName;
    }

    public void setPostName(String postName){
        this.postName = postName;
    }

    public String getUserNickName(){
        return userNickName;
    }

    public void setUserNickName(String nickName){
        this.userNickName = userNickName;
    }

    public String getContents()
    {
        return contents;
    }

    public long getLike_count(){
        return like_count;
    }

    public void setLike_count(long like_count){
        this.like_count = like_count;
    }
}
