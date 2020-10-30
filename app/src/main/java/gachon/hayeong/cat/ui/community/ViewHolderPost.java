package gachon.hayeong.cat.ui.community;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPost extends RecyclerView.ViewHolder {
    ImageView iv_userImage;
    VideoView vv_postVideo;
    TextView tv_userNickName;
    TextView tv_contents;
    TextView tv_likeCount;

    public ViewHolderPost(@NonNull View itemView){
        super(itemView);

//        iv_userImage = itemView.findViewById(R.id.iv_userImage);
//        vv_postVideo = itemView.findViewById(R.id.iv_postVideo);
//        tv_userNickName = itemView.findViewById(R.id.tv_userName);
//        tv_contents = itemView.findViewById(R.id.tv_postContents);
//        tv_likeCount = itemView.findViewById(R.id.tv_likeCount);
    }

    public void onBind(Post data){
//        iv_userImage.setImageResource(data.getUserImage());
//        vv_postVideo.setVideoURI(data.getPostImage());
//        tv_userNickName.setText(data.getNickName());
//        tv_contents.setText(data.getContents());
//        String likeNum = Integer.toString(data.getLikeNum());
//        tv_likeCount.setText(likeNum);
    }

}