package com.example.catemotion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    public Boolean duplicateComplete = false;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public ArrayList<String> userIdList = new ArrayList<String>();

    private EditText userID;
    private Button duplicate_confirm_button;
    private Button id_ok_button;
    private TextView stateInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userID = (EditText) findViewById(R.id.userID);
        duplicate_confirm_button = (Button) findViewById(R.id.duplicate_confirm_button);
        id_ok_button = (Button) findViewById(R.id.id_ok_button);
        stateInfo = (TextView) findViewById(R.id.stateInfo);

        userID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                duplicateComplete = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                duplicateComplete = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        duplicate_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUsableID(userID.toString());
            }
        });

        id_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (duplicateComplete) {
                    //DB에 user정보 저장 (UID는 가져와서, 입력받은id, getCurrentUser-> email, name)
                    String id = userID.getText().toString();
                    addUserInfoToDB(id);

                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "중복 확인을 해주세요", Toast.LENGTH_SHORT).show();
                }
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
                        UserList user = snapshot.getValue(UserList.class);

                        userIdList.add(user.getNickName());
                    }

                    for (int i = 0; i < userIdList.size(); i++) {
                        if (userIdList.get(i).equals(inputID)) {
                            userIdList.clear();
                            stateInfo.setText("이미 사용중인 ID입니다. 다른 ID를 입력해주세요.");
                            break;
                        } else {
                            userIdList.clear();
                            stateInfo.setText("사용 가능한 ID입니다.");
                            duplicateComplete = true;
                            break;
                        }
                    }
                } else {
                    userIdList.clear();
                    stateInfo.setText("사용 가능한 ID입니다.");
                    duplicateComplete = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("UserInfoActivity", "loadPost:onCancelled", error.toException());
            }
        });
    }

    private void addUserInfoToDB(String extraID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            UserList userDTO = new UserList(
                    extraID,
                    user.getEmail(),
                    null
            );

            databaseReference.child(user.getUid()).setValue(userDTO);
            startActivity(new Intent(UserInfoActivity.this, MainActivity.class));

            Toast.makeText(getApplicationContext(), "로그인 성공 ♡", Toast.LENGTH_SHORT).show();
        }
    }
}