package com.example.liuyh73.glory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;

public class SuperAwesomeCardFragment extends Fragment {
    private static final String ARG_AVATAR = "avatar";
    private static final String ARG_SKIN = "skin";
    private static final String ARG_POSITION = "position";
    private String avatar;
    private String skin;
    private int position;

    public static SuperAwesomeCardFragment newInstance(String _skin, String _avatar, int _position) {
        SuperAwesomeCardFragment f = new SuperAwesomeCardFragment();
        Bundle b = new Bundle();
        b.putString(ARG_SKIN, _skin);
        b.putString(ARG_AVATAR, _avatar);
        b.putInt(ARG_POSITION, _position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        skin = bundle.getString(ARG_SKIN);
        avatar = bundle.getString(ARG_AVATAR);
        position = bundle.getInt(ARG_POSITION, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.view_page_item,container,false);
        ViewCompat.setElevation(rootView, 50);
        rootView.setTag(getResources().getString(R.string.preTag) + position);
        ImageView imageView = rootView.findViewById(R.id.detailSkinAvatar);
        Glide.with(this).load(avatar).into(imageView);
        final RelativeLayout relativeLayout = rootView.findViewById(R.id.detailShowSkin);
        Glide.with(this).load(skin).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                relativeLayout.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
        return rootView;
    }
}
