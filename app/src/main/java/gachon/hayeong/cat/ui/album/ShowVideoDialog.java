package gachon.hayeong.cat.ui.album;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import gachon.hayeong.cat.R;
import gachon.hayeong.cat.databinding.DialogShowvideoBinding;

public class ShowVideoDialog extends Dialog {

    DialogShowvideoBinding binding;

    private Uri videoUri;

    private Activity activity;
    Context context;

    ShowVideoDialog(Context context, Uri videoUri, Activity activity) {
        super(context);
        this.videoUri = videoUri;
        this.activity = activity;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_showvideo, null, false);
        setContentView(binding.getRoot());
//        String uriStr = this.videoUri.toString();
//        uriStr = uriStr.substring(16, uriStr.length() - 12);
        binding.videoView.setVideoURI(this.videoUri);
        Log.e("uri",this.videoUri.toString());

        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                binding.videoView.start();
            }
        });
        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ShowVideoDialog.this.cancel();
            }
        });

    }
}