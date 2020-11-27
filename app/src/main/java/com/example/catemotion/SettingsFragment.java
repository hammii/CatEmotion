package com.example.catemotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        setHasOptionsMenu(true);

        // 상단바 텍스트 변경
        mActivity.setTitle("Settings");

        TextView tv_user = view.findViewById(R.id.tv_user);
        TextView tv_app_info = view.findViewById(R.id.tv_app_info);

        // 계정 관리
        tv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).replaceFragment(ProfileFragment.newInstance());
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if(firebaseAuth.getCurrentUser() != null){
                    ((MainActivity) getActivity()).replaceFragment(ProfileFragment.newInstance());
                } else {
                    ((MainActivity) getActivity()).replaceFragment(LoginFragment.newInstance());
                }

            }
        });

//        tv_app_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).replaceFragment(AppInfoFragment.newInstance());
//            }
//        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.inform, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // 앱 정보
//        ((MainActivity) getActivity()).replaceFragment(AppInfoFragment.newInstance());
        startActivity(new Intent(mActivity, AppInfoActivity.class));

        return super.onOptionsItemSelected(item);
    }
}