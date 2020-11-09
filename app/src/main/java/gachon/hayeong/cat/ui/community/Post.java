package gachon.hayeong.cat.ui.community;

import android.net.Uri;

public class Post {
    String nickName;
    int userImage;
    Uri postImage;
    int likeNum;
    String contents;

    public Post(int userImage, String nickName){
        this.userImage = userImage;
        this.nickName = nickName;
    }

    public Post(String nickName, int userImage, Uri postImage, int likeNum, String contents){
        this.nickName = nickName;
        this.userImage = userImage;
        this.postImage = postImage;
        this.likeNum = likeNum;
        this.contents = contents;
    }

    public String getNickName(){
        return nickName;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public int getUserImage(){
        return userImage;
    }

    public void setUserImage(){
        this.userImage = userImage;
    }

    public Uri getPostImage(){
        return postImage;
    }

    public void setPostImage(){
        this.postImage = postImage;
    }

    public int getLikeNum(){
        return likeNum;
    }

    public void setLikeNum(int likeNum){
        this.likeNum = likeNum;
    }

    public String getContents(){
        return contents;
    }

    public void setContents(String contents){
        this.contents = contents;
    }
}
