package com.example.liuwd8.httpapi;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class BilibiliAPI extends AppCompatActivity {
    private EditText searchText;
    private MyRecyclerViewAdapter<HttpApiJsonData> adapter;
    private List<HttpApiJsonData> data;
    private static String playStr = "播放量: %d";
    private static String commentStr = "评论: %d";
    private static String durationStr = "时长: %s";
    private static String createStr = "创建时间: %s";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) {
            try {
                int a = Integer.parseInt(searchText.getText().toString());
                HttpApiJsonData httpApiJsonData = new HttpApiJsonData();
                String str = ConnectURL("https://space.bilibili.com/ajax/top/showTop?mid=" + a);
                if (str == null) {
                    emitter.onNext(1);
                    emitter.onComplete();
                    return;
                }
                try {
                    MidResponseData midResponseData = new Gson().fromJson(str, MidResponseData.class);
                    httpApiJsonData.setMidResponseData(midResponseData);
                    str = ConnectURL("https://api.bilibili.com/pvideo?aid=" + midResponseData.getData().getAid());
                } catch (JsonSyntaxException e) {
                    emitter.onNext(2);
                    emitter.onComplete();
                    return;
                }

                if (str == null) {
                    httpApiJsonData.setAidResponseData(null);
                } else {
                    try {
                        AidResponseData aidResponseData = new Gson().fromJson(str, AidResponseData.class);
                        httpApiJsonData.setAidResponseData(aidResponseData);
                    } catch (JsonSyntaxException e) {
                        httpApiJsonData.setAidResponseData(null);
                        e.printStackTrace();
                    }
                }
                data.add(httpApiJsonData);
                emitter.onNext(0);
                emitter.onComplete();
            } catch (NumberFormatException e) {
                emitter.onNext(3);
                emitter.onComplete();
                e.printStackTrace();
            } catch (IOException e) {
                emitter.onNext(1);
                emitter.onComplete();
                e.printStackTrace();
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilibili);
        data = new ArrayList<HttpApiJsonData>();
        searchText = findViewById(R.id.searchText);
        RecyclerView recyclerView = findViewById(R.id.recycleView);

        adapter = new MyRecyclerViewAdapter<HttpApiJsonData>(BilibiliAPI.this, R.layout.item, data) {
            @Override
            public void convert(MyViewHolder holder, final HttpApiJsonData httpApiJsonData) {
                final ImageView cover = holder.getView(R.id.cover);
                final ProgressBar progressBar = holder.getView(R.id.progressBar);
                Glide.with(BilibiliAPI.this).load(httpApiJsonData.getMidResponseData().getData().getCover()).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        progressBar.setVisibility(View.GONE);
                        cover.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
                TextView content = holder.getView(R.id.content);
                content.setText(httpApiJsonData.getMidResponseData().getData().getContent());
                TextView play = holder.getView(R.id.play);
                play.setText(String.format(playStr, httpApiJsonData.getMidResponseData().getData().getPlay()));
                TextView comment = holder.getView(R.id.comment);
                comment.setText(String.format(commentStr, httpApiJsonData.getMidResponseData().getData().getCount()));
                TextView duration = holder.getView(R.id.duration);
                duration.setText(String.format(durationStr, httpApiJsonData.getMidResponseData().getData().getDuration()));
                TextView create = holder.getView(R.id.create);
                create.setText(String.format(createStr,  httpApiJsonData.getMidResponseData().getData().getCreate()));
                TextView title = holder.getView(R.id.title);
                title.setText(httpApiJsonData.getMidResponseData().getData().getTitle());
                SeekBar seekBar = holder.getView(R.id.seekBar);
                if (httpApiJsonData.getAidResponseData() == null) {
                    seekBar.setEnabled(false);
                } else {
                    seekBar.setMax(httpApiJsonData.getAidResponseData().getPreviewImage().getIndex()[httpApiJsonData.getAidResponseData().getPreviewImage().getIndex().length - 1]);
                    seekBar.setProgress(0);
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                final PreviewImage previewImage = httpApiJsonData.getAidResponseData().getPreviewImage();
                                int i = 0;
                                for (; i < previewImage.getIndex().length; ++i) {
                                    if (previewImage.getIndex()[i] > progress) {
                                        break;
                                    }
                                }
                                Log.i("sdafasdf", "" + i);
                                final int p = (i - 1) % 100;
                                Glide.with(BilibiliAPI.this).load(previewImage.getImage()[(i - 1) / 100]).into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                                        cover.setImageBitmap(Bitmap.createBitmap(bitmapDrawable.getBitmap(), (p % previewImage.getImg_x_len()) * previewImage.getImg_x_size(),(p / previewImage.getImg_y_len()) * previewImage.getImg_y_size() , previewImage.getImg_x_size(), previewImage.getImg_y_size()));
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            Glide.with(BilibiliAPI.this).load(httpApiJsonData.getMidResponseData().getData().getCover()).into(cover);
                            seekBar.setProgress(0);
                        }
                    });
                }
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Nullable
    private String ConnectURL(String str) throws IOException {
        URL url = new URL(str);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() != 200) {
            return null;
        }
        InputStream inputStream = connection.getInputStream();
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        inputStream.close();
        return sb.toString();
    }

    public void Search(View view) {
        DisposableObserver disposableObserver = new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer o) {
                switch (o) {
                    case 0:
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "数据库中不存在记录", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), searchText.getText().toString() + "不是一个整数", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(disposableObserver);
        compositeDisposable.add(disposableObserver);
    }
}
