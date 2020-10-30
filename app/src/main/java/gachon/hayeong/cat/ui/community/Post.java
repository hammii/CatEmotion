package gachon.hayeong.cat.ui.community;

import java.util.Date;

public class Post {
    private String userImage;
    private String userName;
    //private String postVideo;
    private String postImage;
    private int likeCount;
    private String postContents;
    private Date uploadDate;

    public Post(){}

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

    public String getPostContents(){
        return postContents;
    }

    public void setPostContents(){
        this.postContents = postContents;
    }

    public Date getUploadDate(){
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate){
        this.uploadDate = uploadDate;
    }
}
