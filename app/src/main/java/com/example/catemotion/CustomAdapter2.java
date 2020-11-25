package com.example.catemotion;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.CustomViewHolder> {
    private ArrayList<Post> arrayList;
    private Context context;
    String count;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    public CustomAdapter2(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdapter2.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail2, parent, false);
        CustomAdapter2.CustomViewHolder holder = new CustomAdapter2.CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter2.CustomViewHolder holder, int position) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference categoryRef = rootRef.child("PostImage");

        long uploadDate = DateToMill(arrayList.get(position).getUploadDate());
        String imageName = arrayList.get(position).getUserName() + "_" + uploadDate;

        StorageReference imgRef = categoryRef.child(imageName);

        if(imgRef != null){
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(holder.itemView)
                            .load(uri)
                            .into(holder.iv_postImage);

                }
            });
        }


//        Glide.with(holder.itemView)
//                .load(arrayList.get(position).getPostImage())
//                .into(holder.iv_postImage);

        holder.tv_nickName.setText(arrayList.get(position).getUserName());
        holder.tv_postContents.setText(arrayList.get(position).getPostContents());
        holder.tv_likeCount.setText(String.valueOf(arrayList.get(position).getLikeCount()));

        //좋아요 버튼 구현하기
        holder.iv_likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = holder.tv_likeCount.getText().toString().trim();
                boolean countOrNot = arrayList.get(position).getLikeOrNot();
                int countToInt = Integer.parseInt(count);

                if (countOrNot == true) {
                    countToInt--;
                    arrayList.get(position).setLikeOrNot(false);
                    arrayList.get(position).setLikeCount(countToInt);
                } else {
                    countToInt++;
                    arrayList.get(position).setLikeOrNot(true);
                    arrayList.get(position).setLikeCount(countToInt);
                }

                holder.tv_likeCount.setText(Integer.toString(countToInt));

                //Firebase에 변경된 좋아요 수 반영하기
                ref = database.getReference();
                DatabaseReference postRef = ref.child("Post");
                long millTime = DateToMill(arrayList.get(position).getUploadDate());
                DatabaseReference countRef = postRef.child(arrayList.get(position).getUserName() + "_" + millTime);

                Map<String, Object> countUpdates = new HashMap<>();
                countUpdates.put("likeCount", arrayList.get(position).getLikeCount());

                countRef.updateChildren(countUpdates);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_postImage;
        TextView tv_nickName;
        TextView tv_postContents;
        TextView tv_likeCount;
        ImageView iv_likeImage;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iv_postImage = itemView.findViewById(R.id.iv_postImage);
            this.tv_nickName = itemView.findViewById(R.id.tv_nickName);
            this.tv_postContents = itemView.findViewById(R.id.tv_postContents);
            this.tv_likeCount = itemView.findViewById(R.id.tv_likeCount);
            this.iv_likeImage = itemView.findViewById(R.id.iv_likeImage);
        }
    }

    public long DateToMill(String date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        Date trans_date = null;
        try {
            trans_date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return trans_date.getTime();
    }
}
