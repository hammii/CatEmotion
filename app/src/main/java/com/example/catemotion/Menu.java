package com.example.catemotion;

import android.graphics.Bitmap;
import android.net.Uri;

public class Menu {
    private String title;
    private Bitmap img;
    private Uri uri;

    public Menu(String title, Bitmap img, Uri uri){
        this.title = title;
        this.img = img;
        this.uri = uri;
    }

    public String getTitle(){
        return title;
    }

    public Bitmap getImg(){
        return img;
    }

    public Uri getUri(){
        return uri;
    }
}
