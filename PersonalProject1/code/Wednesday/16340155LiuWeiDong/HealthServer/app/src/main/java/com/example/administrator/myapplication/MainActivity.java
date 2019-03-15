package com.example.administrator.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    EditText searchText;
    String radioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = findViewById(R.id.radiusGroup);
        searchText = findViewById(R.id.searchText);
        radioGroup.check(R.id.rd1);
        radioText="图片";
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioText = ((RadioButton) findViewById(checkedId)).getText().toString();
                Toast.makeText(getApplicationContext(), radioText + "被选中", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void click(View view) {
        if(searchText.getText() == null || searchText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        if(searchText.getText().toString().equals("Health")) {
            alertDialog.setMessage(radioText+"搜索成功");
        } else {
            alertDialog.setMessage("搜索失败");
        }
        alertDialog.setTitle("提示").setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"对话框“确定”按钮被点击",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"对话框“取消”按钮被点击",Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }
}
