package com.example.liuyh73.glory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ryan.rv_gallery.AnimManager;
import com.ryan.rv_gallery.GalleryRecyclerView;
import com.ryan.rv_gallery.util.DLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemPhotoChangedListener,MyFab.MenuListener {
    private MyDB db;
    private GalleryRecyclerView mRecyclerView;
    private RecyclerAdapter adapter;
    private LinearLayout mContainer;
    private List<Hero> mDatas;
    private SeekBar mSeekbar;
    private ImageView loadImage;
    private SearchView searchView;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Button classBtn;
    private static Hero clickedHero;
    private List<String> mListData;
    private boolean wetherAddHero = false;
    private MediaPlayer mediaPlayer;
    private MyFab fabtn;
    //Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    loadImage.setVisibility(View.GONE);
                    mContainer.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    //虚化背景的位置
    private int mLastDraPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabtn=findViewById(R.id.fab);
        List<Integer> list=new ArrayList();
        list.add(R.mipmap.add);
        list.add(R.mipmap.delete);
        fabtn.setIcon(list);
        fabtn.setMenuListener(this);

//        playBackgroundMusic();
        //Log - Closed
        DLog.setDebug(false);
        DLog.d("MainPage: ", "MainActivity onCreate()");
        db = new MyDB(MainActivity.this);
        mListData = new ArrayList<>();
        loadImage = findViewById(R.id.mainPageLoad);
        mDatas = db.getAllPresentHeros();
        mRecyclerView = findViewById(R.id.mainPageRecyclerView);
        mContainer = findViewById(R.id.mainPageAll);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);
        classBtn = findViewById(R.id.mainPageHerosClass);

        searchView = findViewById(R.id.mainPageSearchView);
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();

        mSeekbar = findViewById(R.id.seekBar);
        mSeekbar.setMax(mDatas.size()-1);
        //延时展示
        delayDispaly(900);
        //关闭seekbar点击效果
        mSeekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //初始化Adapter
        initRecycleViewAdapter();
        adapter.setOnItemPhotoChangedListener(this);
        //初始化RecycleView
        initRecycleView();
        //添加英雄点击事件
        adapter.notifyDataSetChanged();
        //搜索
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int pos = 0;
                for (Hero hero : mDatas) {
                    if(hero.getName().equals(query)){
                        mRecyclerView.smoothScrollToPosition(pos);
                        searchView.onActionViewCollapsed();
                        return false;
                    }
                    pos++;
                }
                Toast.makeText(MainActivity.this,"The hero doesn't exit in this page.",Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        //侧边栏
        initDrawerLayout();
    }

    private void initRecycleViewAdapter() {
        adapter = new RecyclerAdapter(mDatas,MainActivity.this) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.hero_item;
            }
        };
    }

    private void initRecycleView(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView
                // 设置滑动速度（像素/s）
                .initFlingSpeed(3300)
                // 设置页边距和左右图片的可见宽度，单位dp
                .initPageParams(0, 40)
                // 设置切换动画的参数因子
                .setAnimFactor(0.1f)
                // 设置切换动画类型，目前有AnimManager.ANIM_BOTTOM_TO_TOP和目前有AnimManager.ANIM_TOP_TO_BOTTOM
                .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)
                // 设置自动播放
                .autoPlay(false)
                // 设置自动播放间隔时间 ms
                .intervalTime(1000)
                // 设置初始化的位置
                .initPosition(0)
                // 在设置完成之后，必须调用setUp()方法
                .setUp();
        // 背景高斯模糊 & 淡入淡出
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setBlurImage(false);
                    mSeekbar.setProgress(mRecyclerView.getScrolledPosition());
                }
            }
        });
        setBlurImage(false);
    }

    private void initDrawerLayout(){
        //ListView
        final mainPage_ListViewAdapter listViewAdapter = new mainPage_ListViewAdapter(MainActivity.this,R.layout.mainpage_listview_item,mListData);
        mDrawerList.setAdapter(listViewAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    String heroName = mListData.get(position);
                    if(mListData.get(0).equals("类型")){
                        List<String> tempList = db.getHerosNameByType(heroName);
                        mListData.clear();
                        mListData.add("英雄名称");
                        mListData.addAll(tempList);
                        listViewAdapter.notifyDataSetChanged();
                    } else {
                        int pos = 0;
                        for (Hero hero : mDatas) {
                            if(hero.getName().equals(heroName)){
                                mRecyclerView.smoothScrollToPosition(pos);
                                searchView.onActionViewCollapsed();
                                mDrawerLayout.closeDrawers();
                                return;
                            }
                            pos++;
                        }
                    }
                }
            }
        });
        //类别按钮点击
        classBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListData.clear();
                mListData.add("类型");
                mListData.add("战士");
                mListData.add("法师");
                mListData.add("射手");
                mListData.add("刺客");
                mListData.add("辅助");
                mListData.add("坦克");
                listViewAdapter.notifyDataSetChanged();
                mDrawerLayout.openDrawer(mDrawerList);
            }
        });
    }

    //设置背景高斯模糊
    public void setBlurImage(boolean forceUpdate) {
        final RecyclerAdapter adapter = (RecyclerAdapter) mRecyclerView.getAdapter();
        final int mCurViewPosition = mRecyclerView.getScrolledPosition();

        boolean isSamePosAndNotUpdate = (mCurViewPosition == mLastDraPosition) && !forceUpdate;

        if (adapter == null || mRecyclerView == null || isSamePosAndNotUpdate) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                DLog.d("MainPage: ", "setBlurImage.-View Position: "+ mCurViewPosition);
                CardView cardView = (CardView) mRecyclerView.getLayoutManager().findViewByPosition(mCurViewPosition);
                if (cardView == null) {
                    return;
                }
                ImageView imageView = cardView.findViewById(R.id.hero_photo);
                final Drawable drawable = imageView.getDrawable();
                //图片是否已经加载
                if(drawable == null){
                    Glide.with(MainActivity.this).load(mDatas.get(mCurViewPosition).getSkins().get(0).getImg_url()).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                            bitmap = BlurBitmapUtil.cropBitmap(bitmap);
                            BlurBitmapUtil.setBlur(bitmap,mContainer,mRecyclerView.getContext());
                        }
                    });
                } else {
                    Bitmap bitmap = BlurBitmapUtil.cropBitmap(((BitmapDrawable)drawable).getBitmap());
                    BlurBitmapUtil.setBlur(bitmap,mContainer,mRecyclerView.getContext());
                }
                // 记录上一次高斯模糊的位置
                mLastDraPosition = mCurViewPosition;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wetherAddHero){
            wetherAddHero = !wetherAddHero;
            mDatas.clear();
            mDatas.addAll(db.getAllPresentHeros());
            mSeekbar.setMax(mDatas.size()-1);
            mSeekbar.setProgress(mRecyclerView.getScrolledPosition());
            DLog.d("MainPage: ", "MainActivity onResume: mDatas-"+mDatas.size());
            adapter.notifyDataSetChanged();
        }
        DLog.d("MainPage: ", "MainActivity onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();

        DLog.d("MainPage: ", "MainActivity onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        DLog.d("MainPage: ", "MainActivity onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        DLog.d("MainPage: ", "MainActivity onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DLog.d("MainPage: ", "MainActivity onDestroy()");

        if (mRecyclerView != null) {
            // 释放资源
            mRecyclerView.release();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override
    public void onItemPhotoChanged() {
        setBlurImage(true);
    }

    private void delayDispaly(int delay){
        mContainer.setVisibility(View.INVISIBLE);
        loadImage.setVisibility(View.VISIBLE);
        //随机展示图片
        Random random=new Random();
        int randomPic = random.nextInt(5);
        int resId = getResources().getIdentifier("loadpic"+ randomPic , "mipmap" , getPackageName());
        loadImage.setBackgroundResource(resId);
        //延时展示
        Timer mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {//创建一个线程来执行run方法中的代码
            @Override
            public void run() {
                mHandler.obtainMessage(0).sendToTarget();
            }
        };
        mTimer.schedule(mTimerTask, delay);//延迟2秒执行
    }

    public static Hero getClickedHero(){
        Hero temp = clickedHero;
        return temp;
    }

    public static void  setClickedHero(Hero hero){
        clickedHero = hero;
    }

    public void playBackgroundMusic() {
        mediaPlayer = MediaPlayer.create(this, R.raw.glory_theme);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
    }



    @Override
    public void click(int i) {
        if(i==1){
            wetherAddHero = true;
            Intent intent = new Intent(MainActivity.this,AddActivity.class);
            startActivity(intent);
        }
        if(i==2){
            if(mDatas.size()>1){
                int mCurViewPosition = mRecyclerView.getScrolledPosition();
                db.deletePresentHero(mDatas.get(mCurViewPosition));
                mDatas.remove(mCurViewPosition);
                mSeekbar.setMax(mDatas.size());
                adapter.notifyDataSetChanged();
                setBlurImage(true);
            }
        }
    }
}
