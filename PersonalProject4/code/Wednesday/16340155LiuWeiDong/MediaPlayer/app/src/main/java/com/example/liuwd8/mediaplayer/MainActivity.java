package com.example.liuwd8.mediaplayer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity {
    private ImageView albumImage;
    private TextView name;
    private TextView singer;
    private TextView timeNow;
    private TextView timeTotal;
    private SeekBar progress;
    private SimpleDateFormat simpleDateFormat;
    private ImageView play;
    private IBinder mBinder;
    private ObjectAnimator animator;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private DisposableObserver disposableObserver = new DisposableObserver<Integer>() {
        @Override
        public void onNext(Integer integer) {
            timeNow.setText(simpleDateFormat.format(new Date(integer)));
            progress.setProgress(integer);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
    private Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
            while (true) {
                try {
                    Parcel data = Parcel.obtain();
                    Parcel replay = Parcel.obtain();
                    mBinder.transact(5, data, replay, 0);
                    emitter.onNext(replay.readInt());
                    data.recycle();
                    replay.recycle();
                    Thread.sleep(800);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onComplete();
                }
            }
        }
    });

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName cn, IBinder service) {
            mBinder = service;
            Parcel data = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            try {
                mBinder.transact(0, data, replay, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            MyMusicInfo musicInfo = (MyMusicInfo) replay.readSerializable();
            if (musicInfo != null) {
                name.setText(musicInfo.name);
                singer.setText(musicInfo.singer);
                timeTotal.setText(simpleDateFormat.format(new Date(musicInfo.duration)));
                progress.setMax(musicInfo.duration);
                progress.setProgress(musicInfo.position);
                timeNow.setText(simpleDateFormat.format(new Date(musicInfo.position)));
            }
            Bitmap album = (Bitmap) replay.readParcelable(getClass().getClassLoader());
            if (album != null) {
                albumImage.setImageBitmap(album);
            }
            if (replay.readInt() == 1) {
                play.setImageResource(R.mipmap.pause);
                animator.start();
            }
            replay.recycle();
            data.recycle();
            observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
            compositeDisposable.add(disposableObserver);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumImage = findViewById(R.id.albumImage);
        name = findViewById(R.id.name);
        singer = findViewById(R.id.singer);
        timeNow = findViewById(R.id.timeNow);
        timeTotal = findViewById(R.id.timeTotal);
        simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
        play = findViewById(R.id.play);
        progress = findViewById(R.id.percentage);
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Parcel data = Parcel.obtain();
                    data.writeInt(progress);
                    try {
                        mBinder.transact(4, data, null, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    data.recycle();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        animator = ObjectAnimator.ofFloat(albumImage, "rotation", 0, 360);
        animator.setDuration(30000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            animator.end();
            Parcel parcelData = Parcel.obtain();
            Parcel replay = Parcel.obtain();
            parcelData.writeParcelable(uri, 0);
            try {
                mBinder.transact(3, parcelData, replay, 0);
            }catch (Exception e) {
                e.printStackTrace();
            }
            MyMusicInfo musicInfo = (MyMusicInfo) replay.readSerializable();
            if (musicInfo != null) {
                timeTotal.setText(simpleDateFormat.format(new Date(musicInfo.getDuration())));
                progress.setMax(musicInfo.getDuration());
                timeNow.setText(getResources().getString(R.string.defaultStartTime));
                name.setText(musicInfo.name);
                singer.setText(musicInfo.singer);
            }
            Bitmap album = (Bitmap) replay.readParcelable(getClass().getClassLoader());
            if (album == null) {
                albumImage.setImageResource(R.mipmap.img);
            } else {
                albumImage.setImageBitmap(album);
            }
            parcelData.recycle();
            replay.recycle();
            play.setImageResource(R.mipmap.play);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    public void playMusic(View view) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            mBinder.transact(1, data, reply, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (reply.readInt() == 1) {
            play.setImageResource(R.mipmap.pause);
            if (animator.isPaused()) {
                animator.resume();
            } else {
                animator.start();
            }
        } else {
            play.setImageResource(R.mipmap.play);
            animator.pause();
        }
        data.recycle();
        reply.recycle();
    }

    public void stopMusic(View view) {
        Parcel data = Parcel.obtain();
        try {
            mBinder.transact(2, data, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        play.setImageResource(R.mipmap.play);
        animator.end();
        progress.setProgress(0);
        timeNow.setText(getResources().getString(R.string.defaultStartTime));
        data.recycle();
    }

    public void exitPlayer(View view) {
        compositeDisposable.clear();
        unbindService(serviceConnection);
        exit(0);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
