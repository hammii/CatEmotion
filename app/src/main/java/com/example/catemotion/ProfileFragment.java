package com.example.catemotion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }

    private FirebaseAuth firebaseAuth;

    private TextView tv_email;
    private TextView tv_id;
    private Button btn_edit_userImage;
    private Button btn_withdrawal;
    private Button btn_logout;
    private Button btn_search_image;
    private ImageView iv_userImage;

    private String email;
    private ArrayList<String> nickname= new ArrayList<>();
    private String userImage;
    private String uid;

    private String TAG = "ProfileFragment";
    private String filename;
    private Uri filePath = null;
    private boolean btn_edit_pressed = false;

    private DatabaseReference ref;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth;
    private FirebaseUser user;

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        setHasOptionsMenu(true);

        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_id = (TextView) view.findViewById(R.id.tv_id);
        btn_edit_userImage = (Button) view.findViewById(R.id.btn_edit_userImage);
        btn_logout = (Button) view.findViewById(R.id.btn_logout);
        btn_withdrawal = (Button) view.findViewById(R.id.btn_withdrawal);
        btn_search_image = (Button) view.findViewById(R.id.btn_search_image);
        iv_userImage = (ImageView) view.findViewById(R.id.iv_userImage);

        ref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Firebase에서 데이터 읽어오기.
        if(user != null) {
            email = user.getEmail();
            uid = user.getUid();

            //Firebase에 등록된 닉네임 가져오기
            DatabaseReference userRef = ref.child("UserList");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserList getUser = dataSnapshot.child(uid).getValue(UserList.class);

                    assert getUser != null;
                    nickname.add(getUser.getNickName());
                    tv_id.setText(nickname.get(0));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("ProfileFragment", "loadPost:onCancelled", error.toException());
                }
            });

            tv_email.setText(email);
        }

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference rootRef = firebaseStorage.getReference();
        StorageReference categoryRef = rootRef.child("PostImage");

        String imageName = user.getDisplayName() + ".png";

        StorageReference imgRef = categoryRef.child(imageName);

        if(imgRef != null){
            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(ProfileFragment.this)
                            .load(uri)
                            .into(iv_userImage);

                }
            });
        }

        iv_userImage.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21){
            iv_userImage.setClipToOutline(true);
        }

        btn_search_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        btn_edit_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserImage();
            }
        });


        //로그아웃 코드.
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("로그아웃 하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        ((MainActivity) getActivity()).replaceFragment(LoginFragment.newInstance());
                    }
                });
                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                alert_confirm.show();
            }
        });

        //회원탈퇴 코드.
        btn_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("회원탈퇴 하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getContext(), "회원탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                        ((MainActivity) getActivity()).replaceFragment(LoginFragment.newInstance());
                                    }
                                });
                    }
                });
                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                alert_confirm.show();
            }
        });

        return view;
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
                            ref = database.getReference();

                            //Post 정보를 Firebase에 등록하는 코드.
                            DatabaseReference userListRef = ref.child("UserList");
                            DatabaseReference idRef = userListRef.child(user.getDisplayName());

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
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

                            Map<String, Object> userListUpdates = new HashMap<>();
                            userListUpdates.put("email", user.getEmail());
                            userListUpdates.put("id", user.getDisplayName());
//                            userListUpdates.put("fileUrl", user.getPhotoUrl().toString());
                            userListUpdates.put("fileUrl", filePath.toString());

                            idRef.updateChildren(userListUpdates);

                            Toast.makeText(getContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "업로드에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            Toast.makeText(getContext(), "파일을 업로드 해주세요.", Toast.LENGTH_SHORT).show();
        }
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
