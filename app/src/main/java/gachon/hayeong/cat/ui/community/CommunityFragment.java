package gachon.hayeong.cat.ui.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import gachon.hayeong.cat.R;

public class CommunityFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;

    public static CommunityFragment newInstance(){
        return new CommunityFragment();
    }

    private Button btn_add_post;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    View view;

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_community, null);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycleview);

        btn_add_post = (Button)view.findViewById(R.id.btn_add_post);
        btn_add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPostActivity.class);
                startActivity(intent);
//                ((MainActivity)getActivity()).replaceFragment(AddPostFragment.newInstance());
            }
        });

        init();
        getData();

        return view;
    }

    private void init(){
        RecyclerView recyclerView = view.findViewById(R.id.recycleview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

    }

    private void getData(){
        Post data = new Post(R.drawable.heart, "하트");
        adapter.addItem(data);
    }
}