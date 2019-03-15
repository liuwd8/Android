package com.example.liuyh73.glory;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class DownloadImageTask extends AsyncTask<String, Integer, Boolean> {
    private Bitmap downloadImage;

    @Override
    protected Boolean doInBackground(String... params) {
        // TODO Auto-generated method stub
        System.out.println("[downloadImageTask->]doInBackground "
                + params[0]);
        downloadImage = HttpUtils.getNetWorkBitmap(params[0]);
        return true;
    }

    // 下载完成回调
    @Override
    protected void onPostExecute(Boolean result) {
        // TODO Auto-generated method stub
        System.out.println("result = " + result);
        super.onPostExecute(result);
    }

    // 更新进度回调
    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        super.onProgressUpdate(values);
    }
}
