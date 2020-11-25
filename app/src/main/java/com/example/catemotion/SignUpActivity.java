package com.example.catemotion;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUpActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 액션바 숨기기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        et_password_check = (EditText)findViewById(R.id.et_password_check);
        et_nickname = (EditText)findViewById(R.id.et_nickname);
        btn_register = (Button)findViewById(R.id.btn_register);
//        iv_userImage = (ImageView)findViewById(R.id.iv_userImage);
//        btn_upload_userImage = (Button)findViewById(R.id.btn_upload_userImage);

        arrayList = new ArrayList<>();
        userList = new UserList();
        btn_uploadImage_pressed = false;

        firebaseAuth = FirebaseAuth.getInstance();

        // 사진 수정하기 버튼
//        btn_upload_userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
//
//                btn_uploadImage_pressed = true;
//            }
//        });

        // 등록하기 버튼
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser(){
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password_check = et_password_check.getText().toString().trim();
        String nickname = et_nickname.getText().toString().trim();

        if(email.equals("")){
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(password.equals("")){
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(password_check.equals("")){
            Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!password.equals(password_check)){
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(nickname.equals("")){
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            //닉네임 중복 테스트 코드 필요
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
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
//                            Bundle result = new Bundle();
                            //result.putString("bundleKey", "123");
//                            result.putString("email", email);
//                            result.putString("id", nickname);
//                            result.putString("filePath", filePath.toString());
//                            getParentFragmentManager().setFragmentResult("requestKey", result);

//                            if(btn_uploadImage_pressed == true) {
//                                uploadUserImage();
//
//                                profileUpdates = new UserProfileChangeRequest
//                                        .Builder()
//                                        .setPhotoUri(filePath)
//                                        .build();
//                                user.updateProfile(profileUpdates)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()){
//                                                    Log.d(TAG, "User profile updated.");
//                                                }
//                                            }
//                                        });
//
//                            }

                            Toast.makeText(getApplicationContext(), " 회원가입 성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
//            filePath = data.getData();
//            Log.d(TAG, "uri: " + String.valueOf(filePath));
//            try{
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), filePath);
//                iv_userImage.setImageBitmap(bitmap);
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//    }

}