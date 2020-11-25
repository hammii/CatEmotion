package com.example.catemotion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

//회원가입 관련 코드 참고 사이트 : http://blog.naver.com/PostView.nhn?blogId=cosmosjs&logNo=220987385077&categoryNo=0&parentCategoryNo=56&viewDate=&currentPage=1&postListTopCurrentPage=1&from=section

public class SignUpFragment extends Fragment {
    public static SignUpFragment newInstance(){
        return new SignUpFragment();
    }

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private FirebaseAuth firebaseAuth;

    private ImageView iv_userImage;
    private EditText et_email;
    private EditText et_password;
    private EditText et_password_check;
    private EditText et_nickname;
    private Button btn_register;
    private Button btn_upload_userImage;

    private ArrayList<UserList> arrayList;
    private UserList userList;
    private String TAG = "SignUpFragment";
    private Uri filePath;
    private boolean btn_uploadImage_pressed;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_signup, null);

        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        et_password_check = (EditText)view.findViewById(R.id.et_password_check);
        et_nickname = (EditText)view.findViewById(R.id.et_nickname);
        btn_register = (Button)view.findViewById(R.id.btn_register);
        iv_userImage = (ImageView)view.findViewById(R.id.iv_userImage);
        btn_upload_userImage = (Button)view.findViewById(R.id.btn_upload_userImage);

        arrayList = new ArrayList<>();
        userList = new UserList();
        btn_uploadImage_pressed = false;

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            ((MainActivity) getActivity()).replaceFragment(ProfileFragment.newInstance());
        }

        btn_upload_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

                btn_uploadImage_pressed = true;
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        return view;
    }

    private void registerUser(){
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password_check = et_password_check.getText().toString().trim();
        String nickname = et_nickname.getText().toString().trim();

        if(email.equals("")){
            Toast.makeText(getContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(password.equals("")){
            Toast.makeText(getContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(password_check.equals("")){
            Toast.makeText(getContext(), "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!password.equals(password_check)){
            Toast.makeText(getContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(nickname.equals("")){
            Toast.makeText(getContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            //닉네임 중복 테스트 코드 필요
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ref = database.getReference();
                            DatabaseReference userListRef = ref.child("UserList");
                            DatabaseReference idRef = userListRef.child(nickname);

                            //Firebase에 데이터 추가하는 한가지 방법
                            //Map<String, Object> userListUpdates = new HashMap<>();
                            //userListUpdates.put("email", email);
                            //userListUpdates.put("id", nickname);
                            //userListUpdates.put("fileUrl", filePath.toString());

                            //idRef.updateChildren(userListUpdates);

                            //두번째 방법
                            UserList userList = new UserList(email, nickname, filePath.toString());
                            idRef.setValue(userList);

                            //DisplayName을 닉네임으로 설정.
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                    .Builder()
                                    .setDisplayName(nickname)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            //Fragment간의 데이터 전달
                            Bundle result = new Bundle();
                            //result.putString("bundleKey", "123");
                            result.putString("email", email);
                            result.putString("id", nickname);
                            result.putString("filePath", filePath.toString());
                            getParentFragmentManager().setFragmentResult("requestKey", result);

                            if(btn_uploadImage_pressed == true) {
                                uploadUserImage();

                                profileUpdates = new UserProfileChangeRequest
                                        .Builder()
                                        .setPhotoUri(filePath)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });

                            }

                            Toast.makeText(getContext(), " 회원가입 성공!", Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).replaceFragment(ProfileFragment.newInstance());

                        } else {
                            Toast.makeText(getContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri: " + String.valueOf(filePath));
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                iv_userImage.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void uploadUserImage(){
        if(filePath != null){
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //SimpleDateFormat format = new SimpleDateFormat("yyyyMMHH_mmss");
            //Date now = new Date();
            //String filename = format.format(now) + ".png";
            String filename = user.getDisplayName() + ".png";

            StorageReference storageRef = storage.getReferenceFromUrl("gs://catemotion-3c68c.appspot.com/").child("UserImage/"+filename);
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(getContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            //Toast.makeText(getContext(), "이미지를 업로드 해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}
