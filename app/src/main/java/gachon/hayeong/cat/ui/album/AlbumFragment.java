package gachon.hayeong.cat.ui.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import gachon.hayeong.cat.R;

public class AlbumFragment extends Fragment {

    private AlbumViewModel albumViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        albumViewModel =
                ViewModelProviders.of(this).get(AlbumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_album, container, false);
        final TextView textView = root.findViewById(R.id.text_album);
        albumViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}