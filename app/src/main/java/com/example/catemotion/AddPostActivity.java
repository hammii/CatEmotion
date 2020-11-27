package com.example.catemotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//참고 페이지: https://blog.naver.com/cosmosjs/220975116725?proxyReferer=https%3A%2F%2Fsearch.naver.com%2Fsearch.naver%3Fwhere%3Dnexearch%26sm%3Dtop_hty%26fbm%3D0%26ie%3Dutf8%26query%3D%25EC%2595%2588%25EB%2593%259C%25EB%25A1%259C%25EC%259D%25B4%25EB%2593%259C%2B%25EC%258A%25A4%25ED%258A%259C%25EB%2594%2594%25EC%2598%25A4%2Bstorage%2B%25ED%258C%258C%25EC%259D%25BC%2B%25EC%2597%2585%25EB%25A1%259C%25EB%2593%259C
public class AddPostActivity extends AppCompatActivity {
    private ImageView iv_upload_image;
    private EditText et_imageLink;
    private EditText et_explain;
    private Button btn_search_image;
    private Button btn_add;
    private Button btn_cancel;
    Post post;

    private static final String TAG = "AddPostActivity";
    private Uri filePath;

    private String id;
    private String userImage;

    private DatabaseReference ref;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private FirebaseUser user;

    private boolean successFileUpload = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionBar.setDisplayHomeAsUpEnabled(true);    // 뒤로가기

        iv_upload_image = (ImageView) findViewById(R.id.iv_upload_image);
        et_explain = (EditText) findViewById(R.id.et_explain);
        btn_search_image = (Button) findViewById(R.id.btn_search_image);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        post = new Post();

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btn_search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath == null) {
                    Toast.makeText(getApplicationContext(), "이미지를 업로드 해주세요.", Toast.LENGTH_SHORT).show();
                } else if (et_explain.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "설명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        et_explain.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_explain.getWindowToken(), 0);
            }
        },100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            filePath = data.getData();
            Log.d(TAG, "uri: " + String.valueOf(filePath));
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv_upload_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        if (filePath != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date now = new Date();
            long millTime = DateToMill(format.format(now));
            String filename = user.getDisplayName() + "_" + millTime;

            StorageReference storageRef = storage.getReferenceFromUrl("gs://catemotion-3c68c.appspot.com/").child("PostImage/" + filename);
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "업로드가 완료되었습니다!", Toast.LENGTH_SHORT).show();

                            ref = database.getReference();

                            //Post 정보를 Firebase에 등록하는 코드.
                            DatabaseReference userListRef = ref.child("Post");
                            DatabaseReference idRef = userListRef.child(user.getDisplayName() + "_" + millTime);

                            Map<String, Object> postlistUpdates = new HashMap<>();
                            postlistUpdates.put("likeCount", 0);
                            postlistUpdates.put("postContents", et_explain.getText().toString().trim());
                            postlistUpdates.put("postImage", filePath.toString());
                            if (user.getPhotoUrl() != null) {
                                postlistUpdates.put("userImage", user.getPhotoUrl().toString());
                            }
                            //postlistUpdates.put("userImage", post.getUserImage());
                            postlistUpdates.put("userName", user.getDisplayName());
                            postlistUpdates.put("uploadDate", format.format(now));

                            idRef.updateChildren(postlistUpdates);

                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(getContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        Boolean start = false;

                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            if (!start) {
                                Toast.makeText(getApplicationContext(), "업로드 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
                                start = true;
                            }
                        }
                    });
        } else {
            //Toast.makeText(getContext(), "이미지를 업로드 해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    public long DateToMill(String date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);

        Date trans_date = null;
        try {
            trans_date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return trans_date.getTime();
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