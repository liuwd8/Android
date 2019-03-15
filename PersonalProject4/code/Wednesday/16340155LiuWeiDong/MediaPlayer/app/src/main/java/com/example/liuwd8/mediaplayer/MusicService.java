package com.example.liuwd8.mediaplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    public final IBinder binder = new MyBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isEnableInit = true;
    private Bitmap album;

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 0: // onActivityCreate
                    if (isEnableInit) {
                        initMediaPlayer(Environment.getExternalStorageDirectory() + "/data/山高水长.mp3");
                    }
                    if (reply != null) {
                        reply.writeSerializable(musicInfo);
                        reply.writeParcelable(album, 0);
                        reply.writeInt(mediaPlayer.isPlaying() ? 1 : 0);
                    }
                    break;
                case 1://clickPlay
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                    if (reply != null) {
                        reply.writeInt(mediaPlayer.isPlaying() ? 1 : 0);
                    }
                    break;
                case 2://clickStop
                    mediaPlayer.stop();
                    try {
                        mediaPlayer.prepare();
                        mediaPlayer.seekTo(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3://clickFile
                    mediaPlayer.stop();
                    mediaPlayer.seekTo(0);
                    initMediaPlayer(MusicService.this, (Uri) data.readParcelable(getClass().getClassLoader()));
                    if (reply != null) {
                        reply.writeSerializable(musicInfo);
                        reply.writeParcelable(album, 0);
                    }
                case 4:
                    mediaPlayer.seekTo(data.readInt());
                    break;
                case 5:
                    if (reply != null) {
                        reply.writeInt(mediaPlayer.getCurrentPosition());
                    }
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
    public MyMusicInfo musicInfo = new MyMusicInfo();

    public MusicService() {}

    public void parseMusic(String filePath) {
        musicInfo.duration = mediaPlayer.getDuration();
        musicInfo.position = mediaPlayer.getCurrentPosition();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(filePath);
        parseMusicByMMR(mmr);
    }

    public void parseMusic(Context context, Uri uri) {
        musicInfo.duration = mediaPlayer.getDuration();
        musicInfo.position = mediaPlayer.getCurrentPosition();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        parseMusicByMMR(mmr);
    }

    private void parseMusicByMMR(MediaMetadataRetriever mmr) {
        String str = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        if (str == null || str.equals("")) {
            musicInfo.name = "未知";
        } else {
            musicInfo.name = str;
        }
        str = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (str == null || str.equals("")) {
            musicInfo.singer = "未知";
        } else {
            musicInfo.singer = str;
        }
        byte[] mp3Date = mmr.getEmbeddedPicture();
        if (mp3Date != null) {
            album = BitmapFactory.decodeByteArray(mp3Date, 0, mp3Date.length);
        } else {
            album = null;
        }
        mmr.release();
    }

    public void initMediaPlayer(String filePath) {
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            isEnableInit = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseMusic(filePath);
    }

    public void initMediaPlayer(Context context, Uri _uri) {
        try {
            mediaPlayer.setDataSource(context, _uri);
            mediaPlayer.prepare();
            isEnableInit = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseMusic(context, _uri);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }
}
