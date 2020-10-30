package gachon.hayeong.cat.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import gachon.hayeong.cat.MainActivity;
import gachon.hayeong.cat.R;

public class AddPostFragment extends Fragment {
    public static AddPostFragment newInstance(){
        return new AddPostFragment();
    }

    private EditText et_imageLink;
    private EditText et_explain;
    private Button btn_search_image;
    private Button btn_add;
    private Button btn_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, null);

        et_imageLink = (EditText) view.findViewById(R.id.et_imageLink);
        et_explain = (EditText) view.findViewById(R.id.et_explain);
        btn_search_image = (Button) view.findViewById(R.id.btn_search_image);
        btn_add = (Button) view.findViewById(R.id.btn_add);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_imageLink.getText().toString().equals("") || et_imageLink.getText().toString() == null){
                    Toast.makeText(getContext(), "비디오를 첨부해주세요.", Toast.LENGTH_SHORT).show();
                } else if(et_explain.getText().toString().equals("") || et_explain.getText().toString() == null){
                    Toast.makeText(getContext(), "설명을 작성해주세요.", Toast.LENGTH_SHORT).show();
                } else{
                    //첨부하는 코드
                    Toast.makeText(getContext(), "업로드 성공!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(CommunityFragment.newInstance());
            }
        });

        return view;

    }
}