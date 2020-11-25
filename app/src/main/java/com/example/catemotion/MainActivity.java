package com.example.catemotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button btn_home;
    private Button btn_album;
    private Button btn_ranking;
    private Button btn_community;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private AlbumFragment albumFragment = new AlbumFragment();
    private RankingFragment rankingFragment = new RankingFragment();
    private CommunityFragment communityFragment = new CommunityFragment();
    private SettingsFragment settingsFragment = new SettingsFragment();

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, communityFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(MainActivity.this, ClassifierActivity.class));

//                        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_album:
                        transaction.replace(R.id.frame_layout, albumFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_ranking:
                        transaction.replace(R.id.frame_layout, rankingFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_community:
                        transaction.replace(R.id.frame_layout, communityFragment).commitAllowingStateLoss();
                        break;
                    case R.id.navigation_settings:
                        transaction.replace(R.id.frame_layout, settingsFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });

//        btn_home = (Button)findViewById(R.id.btn_home);
//        btn_album = (Button)findViewById(R.id.btn_album);
//        btn_ranking = (Button)findViewById(R.id.btn_ranking);
//        btn_community = (Button)findViewById(R.id.btn_community);
//
//        btn_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                replaceFragment(HomeFragment.newInstance());
//            }
//        });
//
//        btn_album.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                replaceFragment(AlbumFragment.newInstance());
//            }
//        });
//
//        btn_ranking.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                replaceFragment(RankingFragment.newInstance());
//            }
//        });
//
//        btn_community.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                replaceFragment(CommunityFragment.newInstance());
//            }
//        });
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