package gachon.hayeong.cat.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import gachon.hayeong.cat.R;

public class AppInfoFragment extends Fragment {
    public static AppInfoFragment newInstance(){
        return new AppInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_app_info, null);

        return view;
    }
}
