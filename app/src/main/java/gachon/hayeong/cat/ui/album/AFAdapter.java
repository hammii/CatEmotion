package gachon.hayeong.cat.ui.album;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

import gachon.hayeong.cat.databinding.ItemMenuBinding;

public class AFAdapter extends RecyclerView.Adapter<AFAdapter.AFAHolder> {
    private Vector<Menu> menus = new Vector<>();

    Context context;
    Activity activity;

    public AFAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    @NonNull
    @Override
    public AFAHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ItemMenuBinding binding = ItemMenuBinding.inflate(LayoutInflater.from(context), viewGroup, false);
        return new AFAHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AFAHolder afaHolder, int position) {
        ItemMenuBinding binding = afaHolder.binding;
        Menu menu  = menus.get(position);
        String title = menu.getTitle();
        Bitmap img = menu.getImg();
        final Uri uri = menu.getUri();
        binding.menuTitleImgView.setImageBitmap(img);
        //다이얼로그로 동영상의 Uri를 보내며 다이얼로그를 띄우는코드.
        binding.menuTitleImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowVideoDialog dialog = new ShowVideoDialog(activity, uri, activity);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public void setUp(Vector<Menu> menus) {
        this.menus = menus;
        notifyDataSetChanged();
    }

    class AFAHolder extends RecyclerView.ViewHolder {
        ItemMenuBinding binding;

        AFAHolder(ItemMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
