package gachon.hayeong.cat.ui.album;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import gachon.hayeong.cat.R;

public class AlbumFragment extends Fragment {
    private Context mContext;
    private Activity mActivity;

    private AlbumViewModel albumViewModel;

    public void onAttach(Context context) {
        mContext = context;
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        albumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_album, container, false);

//        final TextView textView = root.findViewById(R.id.text_album);
//        albumViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        RecyclerView menuListView = (RecyclerView) root.findViewById(R.id.menuListView);
        menuListView.setLayoutManager(new GridLayoutManager(mContext, 2));
        AFAdapter adapter = new AFAdapter(mActivity);
        menuListView.setAdapter(adapter);
        getVideo();
        adapter.setUp(getVideo());

        return root;
    }

    private Vector<Menu> getVideo() {
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };

//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CatEmotion/";
//        Uri fileUri = Uri.parse(path);

//        Uri uri = MediaStore.Files.getContentUri("external");

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        Cursor cursor = mContext.getContentResolver().query(uri, proj, MediaStore.Video.Media.DATA + " like ? ", new String[] {"%CatEmotion%"}, null);
        Vector<Menu> menus = new Vector<>();

        assert cursor != null;
        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            Log.e("title", title);

            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(), id, MediaStore.Video.Thumbnails.MINI_KIND, null);

            // 썸네일 크기 변경할 때.
            //Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, width, height);
            String data = cursor.getString(2);
            Log.e("data", data);

            menus.add(new Menu(title, bitmap, Uri.parse(data)));
        }

        cursor.close();
        return menus;
    }

}