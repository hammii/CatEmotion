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

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.CustomViewHolder> {
    private ArrayList<Post> arrayList;
    private Context context;

    public CustomAdapter2(ArrayList<Post> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter2.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail2, parent, false);
        CustomAdapter2.CustomViewHolder holder = new CustomAdapter2.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter2.CustomViewHolder holder, int position){
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getUserImage())
                .into(holder.iv_postImage);

        holder.tv_nickName.setText(arrayList.get(position).getUserName());
        holder.tv_postContents.setText(arrayList.get(position).getPostContents());
        holder.tv_likeCount.setText(String.valueOf(arrayList.get(position).getLikeCount()));

    }

    @Override
    public int getItemCount(){
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_postImage;
        TextView tv_nickName;
        TextView tv_postContents;
        TextView tv_likeCount;


        public CustomViewHolder(@NonNull View itemView){
            super(itemView);

            this.iv_postImage = itemView.findViewById(R.id.iv_postImage);
            this.tv_nickName = itemView.findViewById(R.id.tv_nickName);
            this.tv_postContents = itemView.findViewById(R.id.tv_postContents);
            this.tv_likeCount = itemView.findViewById(R.id.tv_likeCount);

        }
    }

}
