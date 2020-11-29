package com.example.catemotion;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Pattern;

//회원가입 관련 코드 참고 사이트 : http://blog.naver.com/PostView.nhn?blogId=cosmosjs&logNo=220987385077&categoryNo=0&parentCategoryNo=56&viewDate=&currentPage=1&postListTopCurrentPage=1&from=section

public class SignUpActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    public Boolean duplicateComplete = false;
    public ArrayList<String> userIdList = new ArrayList<String>();

    private ImageView iv_userImage;
    private EditText et_email;
    private EditText et_password;
    private EditText et_password_check;
    private EditText et_nickname;
    private Button btn_register;
    private Button btn_duplicate;

    private ArrayList<UserList> arrayList;
    private UserList userList;
    private String TAG = "SignUpFragment";
    private boolean btn_uploadImage_pressed;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference ref;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);    // 뒤로가기

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_check = (EditText) findViewById(R.id.et_password_check);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_duplicate = (Button) findViewById(R.id.btn_duplicate);

        arrayList = new ArrayList<>();
        userList = new UserList();
        btn_uploadImage_pressed = false;

        firebaseAuth = FirebaseAuth.getInstance();

        // 등록하기 버튼
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // 중복확인 버튼
        btn_duplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUsableID(et_nickname.toString());
            }
        });
    }

    private void isUsableID(final String inputID) {
        databaseReference = FirebaseDatabase.getInstance().getReference("UserList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 부모가 User 인데 부모 그대로 가져오면 User 각각의 데이터 이니까 자식으로 가져와서 담아줌
                if (dataSnapshot.hasChildren()) {   //DB에 User가 있는 경우
                    //DB에 있는 정보 가져오기
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        UserList user = snapshot.getValue(UserList.class);
                        userIdList.add(snapshot.getKey());

//                        assert user != null;
//                        userIdList.add(user.getNickName());
                    }

                    for (int i = 0; i < userIdList.size(); i++) {
                        if (userIdList.get(i).equals(inputID)) {
                            userIdList.clear();
                            Toast.makeText(getApplicationContext(), "이미 사용중인 ID입니다. 다른 ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        } else {
                            userIdList.clear();
                            Toast.makeText(getApplicationContext(), "사용 가능한 ID입니다.", Toast.LENGTH_SHORT).show();
                            duplicateComplete = true;
                            break;
                        }
                    }
                } else {
                    userIdList.clear();
                    Toast.makeText(getApplicationContext(), "사용 가능한 ID입니다.", Toast.LENGTH_SHORT).show();
                    duplicateComplete = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("UserInfoActivity", "loadPost:onCancelled", error.toException());
            }
        });
    }

    private void registerUser() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password_check = et_password_check.getText().toString().trim();
        String nickname = et_nickname.getText().toString().trim();

        if (email.equals("")) {
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (password_check.equals("")) {
            Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(password_check)) {
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if (nickname.equals("")) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!duplicateComplete) {
            Toast.makeText(getApplicationContext(), "중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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
                            UserList userList = new UserList(email, nickname, null);
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
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            Toast.makeText(getApplicationContext(), " 회원가입 성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}