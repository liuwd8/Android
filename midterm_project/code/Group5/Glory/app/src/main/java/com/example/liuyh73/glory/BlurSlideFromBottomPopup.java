package com.example.liuyh73.glory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

/**
 * 从底部滑上来的popup
 */
public class BlurSlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private Hero showHero;
    private View popupView;
    private Context context;
    private TextView name;
    private TextView title;
    private TextView type;
    private ImageView avater;
    private BubbleSeekBar p1;
    private BubbleSeekBar p2;
    private BubbleSeekBar p3;
    private BubbleSeekBar p4;

    public BlurSlideFromBottomPopup(Context context, Hero _showHero) {
        super(context);
        this.context = context;
        Log.i("showHero", String.valueOf(_showHero.getLive()));

        showHero = _showHero;
        bindItem();
        bindEvent();
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0, 500);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0, 1f, 500);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup);
    }

    private void bindEvent() {
        findViewById(R.id.popup_save).setOnClickListener(this);
        findViewById(R.id.popup_add).setOnClickListener(this);
    }

    public void bindItem() {
        name = findViewById(R.id.popup_name);
        title = findViewById(R.id.popup_title);
        type = findViewById(R.id.popup_type);
        avater = findViewById(R.id.popup_avater);
        p1 = findViewById(R.id.popup_seekBar1);
        p2 = findViewById(R.id.popup_seekBar2);
        p3 = findViewById(R.id.popup_seekBar3);
        p4 = findViewById(R.id.popup_seekBar4);

        name.setText(showHero.getName());
        title.setText(showHero.getTitle());
        type.setText(showHero.getHero_type());
        DownloadImage.setViewImage(avater, showHero.getAvaterUrl());

        p1.setProgress(showHero.getLive());
        p2.setProgress(showHero.getAttack());
        p3.setProgress(showHero.getSkill());
        p4.setProgress(showHero.getDifficulty());
    }

    public interface onBtnClick{
        void saveBtnClick(Hero hero);
        void addBtnClick(Hero hero);
    }

    private onBtnClick onBtnClick;

    public void onBtnClick(onBtnClick myBtnClickListener) {
        this.onBtnClick = myBtnClickListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_save:
                showHero.setLive(p1.getProgress());
                showHero.setAttack(p2.getProgress());
                showHero.setSkill(p3.getProgress());
                showHero.setDifficulty(p4.getProgress());
                onBtnClick.saveBtnClick(showHero);
                break;
            case R.id.popup_add:
                showHero.setLive(p1.getProgress());
                showHero.setAttack(p2.getProgress());
                showHero.setSkill(p3.getProgress());
                showHero.setDifficulty(p4.getProgress());
                onBtnClick.addBtnClick(showHero);
                break;
            default:
                break;
        }
    }
}