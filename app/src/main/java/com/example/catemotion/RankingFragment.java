package com.example.catemotion;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RankingFragment extends Fragment {
    public static RankingFragment newInstance() {
        return new RankingFragment();
    }

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

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ranking, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

//        databaseReference = database.getReference("Post");
        databaseReference = database.getReference("Post").orderByChild("likeCount");

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

        adapter = new CustomAdapter2(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
