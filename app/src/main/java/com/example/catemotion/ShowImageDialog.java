package com.example.catemotion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

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

        Button cancelButton = (Button) findViewById(R.id.btn_cancel);
        Button deleteButton = (Button) findViewById(R.id.btn_delete);

        // 취소 버튼
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 삭제 버튼
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getContentResolver().delete(imageUri, null, null);
                dismiss();
            }
        });

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
