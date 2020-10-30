package gachon.hayeong.cat.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import gachon.hayeong.cat.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private ArrayList<Post> arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Post> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position){
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getUserImage())
                .into(holder.iv_userImage);

        //Need code for vv_postVideo --> VideoView

        Glide.with(holder.itemView)
                .load(arrayList.get(position).getPostImage())
                .into(holder.iv_postImage);

        holder.tv_userName.setText(arrayList.get(position).getUserName());
        holder.tv_likeCount.setText(String.valueOf(arrayList.get(position).getLikeCount()));
        holder.tv_postContents.setText(arrayList.get(position).getPostContents());


    }

    @Override
    public int getItemCount(){
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_userImage;
        TextView tv_userName;
        //VideoView vv_postVideo;
        ImageView iv_postImage;
        TextView tv_likeCount;
        TextView tv_postContents;
        TextView tv_uploadDate;

        public CustomViewHolder(@NonNull View itemView){
            super(itemView);
            this.iv_userImage = itemView.findViewById(R.id.iv_userImage);
            this.tv_userName = itemView.findViewById(R.id.tv_userName);
            //this.vv_postVideo = itemView.findViewById(R.id.vv_postVideo);
            this.iv_postImage = itemView.findViewById(R.id.iv_postImage);
            this.tv_likeCount = itemView.findViewById(R.id.tv_likeCount);
            this.tv_postContents = itemView.findViewById(R.id.tv_postContents);
            this.tv_uploadDate = itemView.findViewById(R.id.tv_upload_date);
        }
    }

}
