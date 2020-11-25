package com.example.catemotion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.example.catemotion.databinding.DialogShowvideoBinding;

public class ShowImageDialog extends Dialog {

    DialogShowvideoBinding binding;

    private Uri imageUri;

    private Activity activity;
    Context context;

    ShowImageDialog(Context context, Uri imageUri, Activity activity){
        super(context);
        this.imageUri = imageUri;
        this.activity = activity;
        this.context = context;
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_showvideo, null, false);
        setContentView(binding.getRoot());
        binding.imageView.setImageURI(this.imageUri);
        Log.e("Uri", this.imageUri.toString());

//        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                binding.videoView.start();
//            }
//        });
//        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                ShowVideoDialog.this.cancel();
//            }
//        });
    }

}
