package com.example.liuyh73.glory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddActivity extends AppCompatActivity {

    private ArrayList<Hero> data;
    private ArrayList<Hero> showData = new ArrayList<Hero>();
    private MyDB myDB = new MyDB(AddActivity.this);
    private int showNum = 10;
    private MyListViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent intent = getIntent();

        data = (ArrayList<Hero>) myDB.getAllNotPresentHeros();

        int i = (showNum > data.size()-1) ? data.size()-1 : showNum;
        for(; i >= 0; --i){
            showData.add(data.get(i));
        }

        final CustomSwipeRefreshLayout mySwipeRefreshLayout = findViewById(R.id.mySwipeRefreshLayout);

        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        mySwipeRefreshLayout.setProgressViewOffset(true, 100, 200);

        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        mySwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);

        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mySwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 通过 setEnabled(false) 禁用下拉刷新
        mySwipeRefreshLayout.setEnabled(true);

        // 设定下拉圆圈的背景
        // mySwipeRefreshLayout.setProgressBackgroundColor(R.color.red);

        /*
         * 设置手势下拉刷新的监听
         */
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // 刷新动画开始后回调到此方法
                        mySwipeRefreshLayout.setRefreshing(false);
                        showData.clear();
                        showNum += 5;
                        int i = (showNum > data.size()-1) ? data.size()-1 : showNum;
                        for(; i >= 0; --i){
                            showData.add(data.get(i));
                        }
                        myAdapter.refresh(showData);
                    }
                }
        );

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "add" item
                SwipeMenuItem addItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                addItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,0xCE)));
                // set item width
                addItem.setWidth(dp2px(getApplicationContext(),80));
                // set item title
                addItem.setTitle("Add");
                // set item title fontsize
                addItem.setTitleSize(18);
                // set item title font color
                addItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(addItem);

                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setTitle("Edit");
                editItem.setTitleSize(18);
                editItem.setTitleColor(Color.WHITE);
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                editItem.setWidth(dp2px(getApplicationContext(),80));
                // add to menu
                menu.addMenuItem(editItem);
            }
        };


        final SwipeMenuListView listView = findViewById(R.id.SwipeMenuListView);

        listView.setDividerHeight(dp2px(AddActivity.this,10));

        // set creator
        listView.setMenuCreator(creator);
        myAdapter = new MyListViewAdapter(this, showData);
        listView.setAdapter(myAdapter);
        listView.setTag(0);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // add
                        myDB.insertPresentHero(showData.get(position));
                        showData.remove(showData.get(position));
                        myAdapter.refresh(showData);
                        // Toast.makeText(AddActivity.this, "add", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        // edit
                        Log.i("show", showData.get(position).getName());
                        final BlurSlideFromBottomPopup pWin = new BlurSlideFromBottomPopup(AddActivity.this, showData.get(position));
                        pWin.onBtnClick(new BlurSlideFromBottomPopup.onBtnClick() {
                            @Override
                            public void saveBtnClick(Hero hero) {
                                myDB.updateHero(hero);
                                pWin.dismiss();
                                // Toast.makeText(AddActivity.this, "save", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void addBtnClick(Hero hero) {
                                showData.remove(hero);
                                myDB.insertPresentHero(hero);
                                myAdapter.refresh(showData);
                                pWin.dismiss();
                                // Toast.makeText(AddActivity.this, "add", Toast.LENGTH_LONG).show();
                            }
                        });
                        pWin.showPopupWindow();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // 搜索按钮
        final ImageView search = findViewById(R.id.add_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = SearchFragment.newInstance();
                searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
                    @Override
                    public void OnSearchClick(String keyword) {
                        // 处理逻辑
                        // Toast.makeText(AddActivity.this, keyword, Toast.LENGTH_SHORT).show();
                        listView.setTag(1);
                        showData = (search(keyword, data));
                        myAdapter.refresh(showData);
                    }
                });
                searchFragment.show(getSupportFragmentManager(),SearchFragment.TAG);
            }
        });

        // 背景图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.background);
        bitmap = BlurBitmapUtil.blurBitmap(AddActivity.this,bitmap,15f);
        listView.setBackground(new BitmapDrawable(getResources(), bitmap));

    }

    // 模糊搜索
    public ArrayList<Hero> search(String str, ArrayList<Hero> list){
        ArrayList<Hero> results = new ArrayList<Hero>();
        Pattern pattern = Pattern.compile(str);
        showNum = 5;
        for(int i=0; i < list.size(); i++){
            Hero hero = ((Hero)list.get(i));
            Matcher matcher = pattern.matcher(hero.getName() + hero.getHero_type() + hero.getTitle());
            if(matcher.find()){
                results.add(list.get(i));
            }
        }
        return results;
    }

    /**
     * 将dp转换成px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dp2px(Context context, float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
