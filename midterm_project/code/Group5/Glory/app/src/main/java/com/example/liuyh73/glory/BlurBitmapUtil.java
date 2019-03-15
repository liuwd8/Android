package com.example.liuyh73.glory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

public class BlurBitmapUtil {
    private static Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";
    //图片缩放比例
    private static final float BITMAP_SCALE = 0.4f;

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);
        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
    /**
     * 设置背景高斯模糊
     */
    public static void setBlur(Bitmap resBmp, LinearLayout mContainer, Context mRecyclerViewContext) {
        // 将该Bitmap高斯模糊后返回到resBlurBmp
        Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mRecyclerViewContext, resBmp, 20f);
        // 再将resBlurBmp转为Drawable
        Drawable resBlurDrawable = new BitmapDrawable(mRecyclerViewContext.getResources(),resBlurBmp);
        // 获取前一页的Drawable
        Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);

        /* 以下为淡入淡出效果 */
        Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
        TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
        mContainer.setBackground(transitionDrawable);
        transitionDrawable.startTransition(500);

        // 存入到cache中
        mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
    }

    public static Bitmap cropBitmap(Bitmap bitmap) {//从中间截取一个16:9图片
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = 900;
        return Bitmap.createBitmap(bitmap, (w - cropWidth) / 2+200, 0, cropWidth, h);
    }
}