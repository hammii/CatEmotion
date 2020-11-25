package com.example.catemotion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class CommunityFragment extends Fragment {
    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    private Button btn_add_post;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    //    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager layoutManager; //수정

    private ArrayList<Post> arrayList;
    private FirebaseDatabase database;
    //    private DatabaseReference databaseReference;
    private Query databaseReference;

    private View view;

    private Context mContext;
    private Activity mActivity;

    private FirebaseAuth firebaseAuth;

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);

        // 상단바 텍스트 변경
        mActivity.setTitle("Community");

        btn_add_post = (Button) view.findViewById(R.id.btn_add_post);
        btn_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();

                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(getContext(), "로그인 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    ((MainActivity) getActivity()).replaceFragment(AddPostFragment.newInstance());
                }

//                ((MainActivity) getActivity()).replaceFragment(AddPostFragment.newInstance());
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();


        database = FirebaseDatabase.getInstance();

//        databaseReference = database.getReference("Post");
        databaseReference = database.getReference("Post").orderByChild("uploadDate");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    arrayList.add(post);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Post", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new CustomAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}