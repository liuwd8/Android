package com.example.liuwd8.healthfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {
    List<Collection> data = new ArrayList<>();
    List<Collection> collectionData = new ArrayList<>();
    private static final String STATICACTION = "com.example.liuwd8.MyStaticFilter";
    private static final String WIDGETSTATICACTION = "com.example.liuwd8.MyWidgetStaticFilter";
    Bundle bundle;
    MyListViewAdapter adapter;

    public static class MessageEvent {
        public int position;

        public MessageEvent(int _position) {
            position = _position;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Random random = new Random();
        int n = random.nextInt(data.size());

        bundle = new Bundle();
        bundle.putInt("position", n);
        bundle.putString("foodName", data.get(n).getFoodName());
        bundle.putString("foodType", data.get(n).getFoodType());
        bundle.putString("nutrients", data.get(n).getNutrients());
        bundle.putString("backgroundColor", data.get(n).getBackgroundColor());
        Intent intentBroadcast = new Intent(STATICACTION); //定义Intent
        intentBroadcast.putExtras(bundle);
        sendBroadcast(intentBroadcast);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Random random = new Random();
        int n = random.nextInt(data.size());

        bundle = new Bundle();
        bundle.putInt("position", n);
        bundle.putString("foodName", data.get(n).getFoodName());
        bundle.putString("foodType", data.get(n).getFoodType());
        bundle.putString("nutrients", data.get(n).getNutrients());
        bundle.putString("backgroundColor", data.get(n).getBackgroundColor());
        Intent intentWidget = new Intent(WIDGETSTATICACTION);
        intentWidget.putExtras(bundle);
        sendBroadcast(intentWidget);
        super.onRestart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.position != -1 && !collectionData.contains(data.get(event.position))) {
            collectionData.add(data.get(event.position));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent indentData) {
        if(resultCode == RESULT_OK && indentData != null) {
            Bundle bundle = indentData.getExtras();
            if(bundle != null) {
                int position = bundle.getInt("position", -1);
                if(position != -1 && !collectionData.contains(data.get(position))) {
                    collectionData.add(data.get(position));
                }
            }
        }
    }

    void init() {
        data.add(new Collection("大豆", "粮食", "蛋白质", "#BB4C3B"));
        data.add(new Collection("十字花科蔬菜", "肉食", "维生素C", "#C48D30"));
        data.add(new Collection("牛奶", "饮品", "钙", "#4469B0"));
        data.add(new Collection("海鱼",  "肉食", "蛋白质", "#20A17B"));
        data.add(new Collection("菌菇类", "蔬菜", "微量元素", "#BB4C3B"));
        data.add(new Collection("番茄", "蔬菜", "番茄红素", "#4469B0"));
        data.add(new Collection("胡萝卜", "蔬菜", "胡萝卜素", "#20A17B"));
        data.add(new Collection("芥麦", "粮食", "膳食纤维", "#BB4C3B"));
        data.add(new Collection("鸡蛋", "杂食", "几乎所有营养物质", "#C48D30"));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final MyRecyclerViewAdapter<Collection> myRecyclerViewAdapter = new MyRecyclerViewAdapter<Collection>(MainActivity.this, R.layout.item, data) {
            @Override
            public void convert(MyViewHolder holder, Collection s) {
                TextView foodShortType = holder.getView(R.id.foodShortType);
                TextView foodName = holder.getView(R.id.foodName);
                foodShortType.setText(s.getFoodType().substring(0,1));
                foodName.setText(s.getFoodName());
            }
        };
        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                onClickItem(data, position);
            }

            @Override
            public void onLongClick(int position) {
                Toast.makeText(getApplicationContext(),"删除"+data.get(position).getFoodName(), Toast.LENGTH_SHORT).show();
                data.remove(position);
                myRecyclerViewAdapter.notifyItemRemoved(position);
            }
        });

        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(myRecyclerViewAdapter);
        scaleInAnimationAdapter.setDuration(1000);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(scaleInAnimationAdapter);
        recyclerView.setItemAnimator(new OvershootInLeftAnimator());

        collectionData.add(new Collection("收藏夹","*", "", ""));

        ListView listView = findViewById(R.id.listView);
        final MyListViewAdapter myListViewAdapter = new MyListViewAdapter(this, collectionData);
        listView.setAdapter(myListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    onClickItem(collectionData, position);
                }
            }
        });
        final Context me = this;
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                if(position != 0) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(me);
                    alertDialog.setTitle("删除").setMessage("确定删除"+collectionData.get(position).getFoodName()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            collectionData.remove(position);
                            myListViewAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
                return true;
            }
        });
        adapter = myListViewAdapter;
    }

    public void clickButton(View view) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        if(recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.INVISIBLE);
            ListView listView = findViewById(R.id.listView);
            listView.setVisibility(View.VISIBLE);
            FloatingActionButton floatingActionButton = (FloatingActionButton) view;
            floatingActionButton.setImageResource(R.mipmap.mainpage);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            ListView listView = findViewById(R.id.listView);
            listView.setVisibility(View.INVISIBLE);
            FloatingActionButton floatingActionButton = (FloatingActionButton) view;
            floatingActionButton.setImageResource(R.mipmap.collect);
        }
    }

    public void onClickItem(List<Collection> data , int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("foodName", data.get(position).getFoodName());
        bundle.putString("foodType", data.get(position).getFoodType());
        bundle.putString("nutrients", data.get(position).getNutrients());
        bundle.putString("backgroundColor", data.get(position).getBackgroundColor());
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,RESULT_FIRST_USER);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        final String exceptAction = "Widget collection";
        if(intent.getAction() != null && (intent.getAction().equals(exceptAction) || intent.getAction().equals("Detail collection"))) {
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setVisibility(View.INVISIBLE);
            ListView listView = findViewById(R.id.listView);
            listView.setVisibility(View.VISIBLE);
            FloatingActionButton floatingActionButton = findViewById(R.id.btn);
            floatingActionButton.setImageResource(R.mipmap.mainpage);
        }
    }
}
