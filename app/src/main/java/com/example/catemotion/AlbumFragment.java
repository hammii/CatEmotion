package com.example.catemotion;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Vector;

public class AlbumFragment extends Fragment {
    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    private Context mContext;
    private Activity mActivity;
    private RecyclerView menuListView;
    private View view;

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, null);

        // 상단바 텍스트 변경
        mActivity.setTitle("Album");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        menuListView = (RecyclerView) view.findViewById(R.id.recyclerview);

        menuListView.setLayoutManager(new GridLayoutManager(mContext, 3));
        AFAdapter adapter = new AFAdapter(mActivity);
        menuListView.setAdapter(adapter);

//        getVideo();
        adapter.setUp(getVideo());
        adapter.notifyDataSetChanged();
    }

    public static Bitmap rotateImage(Bitmap img, Integer degree) {
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(degree);

        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), rotateMatrix, true);
        img.recycle();

        return rotatedImg;
    }

    private Vector<Menu> getVideo() {
        String[] proj = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        Cursor cursor = mContext.getContentResolver().query(uri, proj, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%CatEmotion%"}, null);
//        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null,null);

        Vector<Menu> menus = new Vector<>();

        assert cursor != null;
        while (cursor.moveToNext()) {
            Log.e("Album", String.valueOf(cursor.getCount()));

            String title = cursor.getString(0);
            Log.e("title", title);

            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);

            String data = cursor.getString(2);
            Log.e("data", data);

            menus.add(new Menu(title, bitmap, Uri.parse(data)));
        }
        cursor.close();
        return menus;
    }

}
