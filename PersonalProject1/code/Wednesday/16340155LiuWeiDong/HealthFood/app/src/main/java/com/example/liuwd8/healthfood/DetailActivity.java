package com.example.liuwd8.healthfood;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    int position;
    int flag;
    int isCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datail);
        init();
        flag = 0;
        isCollection = 0;
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        if(isCollection == 1) {
            bundle.putInt("position", position);
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void init() {
        Bundle bundle = this.getIntent().getExtras();
        TextView detailFoodNameTextView = findViewById(R.id.detailFoodName);
        TextView detailFoodTypeTextView = findViewById(R.id.detailFoodType);
        TextView detailNutrientsTextView = findViewById(R.id.detailNutrients);
        RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);

        String foodName = bundle.getString("foodName");
        String foodType = bundle.getString("foodType");
        String nutrients = bundle.getString("nutrients");
        detailFoodNameTextView.setText(foodName);
        detailFoodTypeTextView.setText(foodType);
        detailNutrientsTextView.setText("富含  " + nutrients);
        String backgroundColor = bundle.getString("backgroundColor");
        relativeLayout.setBackgroundColor(Color.parseColor(backgroundColor));
        position = bundle.getInt("position");

        ListView listView = findViewById(R.id.detailList);
        String[] data = {"分享信息", "不感兴趣", "查看更多信息", "出错反馈"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.operation, data);
        listView.setAdapter(arrayAdapter);
    }

    public void clickStarButton(View view) {
        ImageView imageView = findViewById(R.id.star);
        if(flag == 0) {
            imageView.setImageResource(R.mipmap.full_star);
            flag = 1;
        } else {
            imageView.setImageResource(R.mipmap.empty_star);
            flag = 0;
        }
    }

    public void clickCollectionButton(View view) {
        isCollection = 1;
        Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
    }

    public void clickBackButton(View view) {
        Bundle bundle = new Bundle();
        if(isCollection == 1) {
            bundle.putInt("position", position);
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}
