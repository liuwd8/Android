package com.example.liuyh73.glory;


import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private int mDownX = 0;
    private int mDownY = 0;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写onInterceptTouchEvent方法对事件进行拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                //记录下点击的位置
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int currX = (int) ev.getX();
                int currY = (int) ev.getY();
                int dx = currX - mDownX;
                int dy = currY - mDownY;
                //拦截条件：
                //1.下拉
                //2.垂直距离差大于水平距离差
                if (dy > 0 && (dy > Math.abs(dx))) {
                    //此时还要根据父类的处理结果来决定事件拦不拦截
                    //因为当ListView已经往下滑动过后，不应该对事件进行拦截，否则ListView不能往上滑动
                    intercepted = super.onInterceptTouchEvent(ev);
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        return intercepted;
    }
}