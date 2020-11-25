package com.example.catemotion;

public class Post {
    private String userImage;
    private String userName;
    //private String postVideo;
    private String postImage;
    private int likeCount;
    private String postContents;
    private String uploadDate;
    private boolean likeOrNot;

    public Post(){
        likeOrNot = false;
    }

    public Post(String userName, String userImage, String postImage, String postContents, String uploadDate){
        likeOrNot = false;

        this.userName = userName;
        this.userImage = userImage;
        this.postImage = postImage;
        this.postContents = postContents;
        this.uploadDate = uploadDate;
    }

    public String getUserImage(){
        return userImage;
    }

    public void setUserImage(String userImage){
        this.userImage = userImage;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    //public String getPostVideo(){
    //    return postVideo;
    //}

    //public void setPostVideo(String postVideo){
    //    this.postVideo = postVideo;
    //}

    public String getPostImage(){
        return postImage;
    }

    public void setPostImage(String postImage){
        this.postImage = postImage;
    }

    public int getLikeCount(){
        return likeCount;
    }

    public void setLikeCount(int likeCount){
        this.likeCount = likeCount;
    }

    public boolean getLikeOrNot(){
        return likeOrNot;
    }

    public void setLikeOrNot(boolean likeOrNot){
        this.likeOrNot = likeOrNot;
    }

    public String getPostContents(){
        return postContents;
    }

    public void setPostContents(String postContents){
        this.postContents = postContents;
    }

    public String getUploadDate(){
        return uploadDate;
    }

    public void setUploadDate(String uploadDate){
        this.uploadDate = uploadDate;
    }

}
