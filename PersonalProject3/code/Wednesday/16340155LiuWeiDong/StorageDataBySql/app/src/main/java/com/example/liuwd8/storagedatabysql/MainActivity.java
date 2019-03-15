package com.example.liuwd8.storagedatabysql;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    boolean status = true;
    Bitmap avatar;
    public static final String LOGINACTION = "LoginAction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rd1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                status = checkedId == R.id.rd1;
                EditText editText = findViewById(R.id.confirmPassword);
                int k = status ? View.GONE : View.VISIBLE;
                editText.setVisibility(k);
                ImageView imageView = findViewById(R.id.image);
                imageView.setVisibility(k);
                EditText editText1 = findViewById(R.id.password);
                editText.setText("");
                editText1.setText("");
            }
        });

        avatar = BitmapFactory.decodeResource(getResources(), R.mipmap.me);
    }

    public void clickOkButton(View view) {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        if (username.getText() == null || username.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Username cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.getText() == null || password.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Password cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        myUserDB userDB = new myUserDB(getApplicationContext());
        if (status) {
            // 登录
            UserData userData = userDB.queryByUsername(username.getText().toString());
            if (userData == null) {
                Toast.makeText(getApplicationContext(), "Username not existed.", Toast.LENGTH_SHORT).show();
            } else if(userData.getPassword().equals(password.getText().toString())) {
                // 登陆成功
                Log.i("sadfasdf", "clickOkButton: " + userData.getArrayList().size());
                Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                UserData.current = userData;
                intent.setAction(LOGINACTION);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Password.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // 注册
            EditText confirmPassword = findViewById(R.id.confirmPassword);
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                UserData userData = new UserData(username.getText().toString(), password.getText().toString(), avatar, null);
                long rid = userDB.save(userData);
                if (rid == -1) {
                    Toast.makeText(getApplicationContext(), "Username already existed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Register successfully.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                    UserData.current = userData;
                    intent.setAction(LOGINACTION);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Password mismatch.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickClearButton(View view) {
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        username.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    public void clickImageView(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0 && data != null) {
            Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            ImageView imageView = findViewById(R.id.image);
            imageView.setImageURI(uri);
            try {
                avatar = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
