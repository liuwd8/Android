package com.example.liuyh73.glory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

public abstract class RecyclerAdapter extends RecyclerView.Adapter<MyReccleViewHolder> {
    private List<Hero> mDatas;
    private Context mContext;
    private OnItemPhotoChangedListener mOnItemPhotoChangedListener;

    public void convert(final MyReccleViewHolder holder, final Hero data, final int position){
        final ImageView imageView = holder.getView(R.id.hero_photo);
        Glide.with(mContext).load(data.getSkins().get(0).getImg_url()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                bitmap = BlurBitmapUtil.cropBitmap(bitmap);
                imageView.setBackground(new BitmapDrawable(imageView.getResources(),bitmap));
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.setClickedHero(data);
                Intent intent = new Intent(mContext,DetailActivity.class);
                mContext.startActivity(intent);
            }
        });
        //英雄名称
        final TextView textView1,textView2,textView3,textView4;
        textView1 = holder.getView(R.id.hero_name);
        textView1.setText(data.getName());
        //关系信息
        textView2 = holder.getView(R.id.hero_relation);
        textView3 = holder.getView(R.id.hero_name_1);
        textView4 = holder.getView(R.id.hero_name_2);
        final MyDB db = new MyDB(mContext);
        final Relation relation = db.getRelationsByHeroId(data.getHero_id());
        final Button button1,button2,button3;
        button1 = holder.getView(R.id.partnerHeroBtn);
        button2 = holder.getView(R.id.compressHeroBtn);
        button3 = holder.getView(R.id.beCompressedHeroBtn);
        final ImageView imageView1,imageView2;
        imageView1 = holder.getView(R.id.relationIcon1);
        imageView2 = holder.getView(R.id.relationIcon2);
        //默认信息
        button1.setBackgroundResource(R.drawable.shape_button);
        button2.setBackgroundColor(0);
        button3.setBackgroundColor(0);
        String partener1 = relation.getPartner1();
        String partener2 = relation.getPartner2();
        textView3.setText(partener1);
        textView4.setText(partener2);
        Glide.with(mContext).load(db.getHeroInfoByName(partener1).getSkins().get(0).getAvatar_url()).into(imageView1);
        Glide.with(mContext).load(db.getHeroInfoByName(partener2).getSkins().get(0).getAvatar_url()).into(imageView2);
        textView2.setText(relation.getPartner1description());
        //最佳拍档
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1.setBackgroundResource(R.drawable.shape_button);
                button2.setBackgroundColor(0);
                button3.setBackgroundColor(0);
                String partener1 = relation.getPartner1();
                String partener2 = relation.getPartner2();
                textView3.setText(partener1);
                textView4.setText(partener2);
                Glide.with(mContext).load(db.getHeroInfoByName(partener1).getSkins().get(0).getAvatar_url()).into(imageView1);
                Glide.with(mContext).load(db.getHeroInfoByName(partener2).getSkins().get(0).getAvatar_url()).into(imageView2);
                textView2.setText(relation.getPartner1description());
            }
        });
        //压制英雄
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2.setBackgroundResource(R.drawable.shape_button);
                button1.setBackgroundColor(0);
                button3.setBackgroundColor(0);
                String partener1 = relation.getRepress1();
                String partener2 = relation.getRepress2();
                textView3.setText(partener1);
                textView4.setText(partener2);
                Glide.with(mContext).load(db.getHeroInfoByName(partener1).getSkins().get(0).getAvatar_url()).into(imageView1);
                Glide.with(mContext).load(db.getHeroInfoByName(partener2).getSkins().get(0).getAvatar_url()).into(imageView2);
                textView2.setText(relation.getRepress1description());
            }
        });
        //被压制英雄
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button3.setBackgroundResource(R.drawable.shape_button);
                button2.setBackgroundColor(0);
                button1.setBackgroundColor(0);
                String partener1 = relation.getRepressed1();
                String partener2 = relation.getRepressed2();
                textView3.setText(partener1);
                textView4.setText(partener2);
                Glide.with(mContext).load(db.getHeroInfoByName(partener1).getSkins().get(0).getAvatar_url()).into(imageView1);
                Glide.with(mContext).load(db.getHeroInfoByName(partener2).getSkins().get(0).getAvatar_url()).into(imageView2);
                textView2.setText(relation.getRepressed1description());
            }
        });
        //头像1
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText(relation.getDescriptionByName(textView3.getText().toString()));
            }
        });
        //头像2
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText(relation.getDescriptionByName(textView4.getText().toString()));
            }
        });
    }

    RecyclerAdapter(List<Hero> mDatas, Context context) {
        this.mDatas = mDatas;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DLog.d("RecyclerAdapter", "RecyclerAdapter onAttachedToRecyclerView");
        super.onAttachedToRecyclerView(recyclerView);
    }

    public abstract int getLayoutId(int viewType);
    @Override
    public MyReccleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DLog.d("RecyclerAdapter", "RecyclerAdapter onCreateViewHolder" + " width = " + parent.getWidth());
        return MyReccleViewHolder.get(parent,getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(final MyReccleViewHolder holder, final int position) {
        DLog.d("RecyclerAdapter", "RecyclerAdapter onBindViewHolder" + "--> position = " + position);
        //holder.setImage(mDatas.get(holder.getAdapterPosition()));
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemPhotoChangedListener(OnItemPhotoChangedListener mOnItemPhotoChangedListener) {
        this.mOnItemPhotoChangedListener = mOnItemPhotoChangedListener;
    }

    public interface OnItemPhotoChangedListener {
        /**
         * 局部更新后需要替换背景图片
         */
        void onItemPhotoChanged();
    }
}