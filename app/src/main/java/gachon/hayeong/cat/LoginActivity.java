package gachon.hayeong.cat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import gachon.hayeong.cat.data.DTO.UserDTO;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mListener;
    private GoogleSignInClient mSignInClient;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 파이어베이스 인증 객체 선언
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 DB 객체 선언
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mListener);
    }

    @Override
    public void onStop() {
        firebaseAuth.removeAuthStateListener(mListener);
        super.onStop();
    }

    private void init() {
        // Google Sign In Client를 초기화
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton auth_btn_google = (SignInButton) findViewById(R.id.auth_btn_google);
        auth_btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);   //어떤 구글 아이디로 로그인할지 정하는 화면으로 넘어감
            }
        });

        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {    //이미 로그인 되어있는 상태
                    isFirstLogin();
                    Log.d("First?---->", firebaseAuth.getCurrentUser().toString());
                } else {
                    Log.d("First?---->", firebaseAuth.getCurrentUser().toString());
                }
            }
        };
    }

    private void signInWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {    // 인증 성공.
                    Toast.makeText(getApplicationContext(), "구글 로그인 성공", Toast.LENGTH_SHORT).show();
                    isFirstLogin();
                    finish();
                } else {      // 인증 실패.
                    Toast.makeText(getApplicationContext(), "구글 로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signOut() {
        firebaseAuth.signOut();
        mSignInClient.signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //startActivityForResult로 보냈을 때 구글로그인에서 계정 선택 후 종료-> 다시 돌아올 때 정보를 받아서 처리하는애 ~
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);
                signInWithGoogle(acct);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void isFirstLogin() {
        final String UID = firebaseAuth.getUid();

//        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        finish();

        databaseReference.child("UserList").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDTO user = snapshot.getValue(UserDTO.class);

                if (user != null) {     //파이어베이스 DB에 구글 로그인 한 유저의 UID 정보가 등록되어있다면
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UserInfoActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}