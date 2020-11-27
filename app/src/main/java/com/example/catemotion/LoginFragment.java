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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    private FirebaseAuth firebaseAuth;

    private EditText et_email;
    private EditText et_password;
    private Button btn_login;
    private Button btn_signup;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_login, null);
        setHasOptionsMenu(true);

        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        btn_login = (Button)view.findViewById(R.id.btn_login);
        btn_signup = (Button)view.findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            ((MainActivity) getActivity()).replaceFragment(ProfileFragment.newInstance());
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();

                if(email.equals("")){
                    Toast.makeText(getContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(password.equals("")){
                    Toast.makeText(getContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else{
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        ((MainActivity) getActivity()).replaceFragment(ProfileFragment.newInstance());
                                    } else {
                                        Toast.makeText(getContext(), "로그인 실패: 아이디나 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).replaceFragment(SignUpFragment.newInstance());
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intent);
            }
        });

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
        startActivity(new Intent(mActivity, AppInfoActivity.class));

        return super.onOptionsItemSelected(item);
    }
}
