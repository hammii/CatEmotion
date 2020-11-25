package com.example.catemotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private Button btn_camera;
    private Button btn_album;
    private Button btn_ranking;
    private Button btn_community;
    private Button btn_settings;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private AlbumFragment albumFragment = new AlbumFragment();
    private RankingFragment rankingFragment = new RankingFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 타이틀바 그림자 없애기
        getSupportActionBar().setElevation(0);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, communityFragment).commit();

        btn_camera = (Button)findViewById(R.id.btn_camera);
        btn_album = (Button)findViewById(R.id.btn_album);
        btn_ranking = (Button)findViewById(R.id.btn_ranking);
        btn_community = (Button)findViewById(R.id.btn_community);
        btn_settings = (Button)findViewById(R.id.btn_settings);
        btn_community.setBackground(getDrawable(R.drawable.ic_baseline_home_24));

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카메라 액티비티 시작
                startActivity(new Intent(MainActivity.this, ClassifierActivity.class));
            }
        });

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 채워진 이미지로 변경
                btn_album.setBackground(getDrawable(R.drawable.ic_baseline_photo_library_24));

                // 나머지 버튼들은 outline 이미지로 변경
                btn_community.setBackground(getDrawable(R.drawable.ic_outline_home_24));
                btn_ranking.setBackground(getDrawable(R.drawable.ic_outline_insert_chart_24));
                btn_settings.setBackground(getDrawable(R.drawable.ic_outline_settings_24));

                replaceFragment(AlbumFragment.newInstance());
            }
        });

        btn_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 채워진 이미지로 변경
                btn_ranking.setBackground(getDrawable(R.drawable.ic_baseline_insert_chart_24));

                // 나머지 버튼들은 outline 이미지로 변경
                btn_community.setBackground(getDrawable(R.drawable.ic_outline_home_24));
                btn_album.setBackground(getDrawable(R.drawable.ic_outline_photo_library_24));
                btn_settings.setBackground(getDrawable(R.drawable.ic_outline_settings_24));

                replaceFragment(RankingFragment.newInstance());
            }
        });

        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 채워진 이미지로 변경
                btn_community.setBackground(getDrawable(R.drawable.ic_baseline_home_24));

                // 나머지 버튼들은 outline 이미지로 변경
                btn_ranking.setBackground(getDrawable(R.drawable.ic_outline_insert_chart_24));
                btn_album.setBackground(getDrawable(R.drawable.ic_outline_photo_library_24));
                btn_settings.setBackground(getDrawable(R.drawable.ic_outline_settings_24));

                replaceFragment(CommunityFragment.newInstance());
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 채워진 이미지로 변경
                btn_settings.setBackground(getDrawable(R.drawable.ic_baseline_settings_24));

                // 나머지 버튼들은 outline 이미지로 변경
                btn_community.setBackground(getDrawable(R.drawable.ic_outline_home_24));
                btn_ranking.setBackground(getDrawable(R.drawable.ic_outline_insert_chart_24));
                btn_album.setBackground(getDrawable(R.drawable.ic_outline_photo_library_24));

                replaceFragment(SettingsFragment.newInstance());
            }
        });
    }

    //Fragment에서 다른 Fragment로 이동시 필요한 함수
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment).commit();
    }

//    //상단바 메뉴 관련 코드
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//
//        switch (item.getItemId()){
//            case R.id.btn_account:
//                replaceFragment(LoginFragment.newInstance());
//                return true;
//            case R.id.btn_app_info:
//                replaceFragment(AppInfoFragment.newInstance());
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}