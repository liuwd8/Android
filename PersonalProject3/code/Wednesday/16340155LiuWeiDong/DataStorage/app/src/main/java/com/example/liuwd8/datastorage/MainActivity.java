package com.example.liuwd8.datastorage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFERENCE", MODE_PRIVATE);
        str = sharedPreferences.getString("Password", "");

        if (str.equals("")) {
            findViewById(R.id.register).setVisibility(View.VISIBLE);
            findViewById(R.id.login).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.register).setVisibility(View.INVISIBLE);
            findViewById(R.id.login).setVisibility(View.VISIBLE);
        }
    }

    public void registerOkButton(View view) {
        EditText editText1 = findViewById(R.id.newPassword);
        EditText editText2 = findViewById(R.id.confirmPassword);
        if (editText1.getText().toString().equals("")) {
            Toast.makeText(this, "Password cannot bt empty.", Toast.LENGTH_SHORT).show();
        }else if (!editText1.getText().toString().equals(editText2.getText().toString())) {
            Toast.makeText(this, "Password mismatch.", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferences = getSharedPreferences("MY_PREFERENCE", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Password",editText1.getText().toString());
            editor.apply();

            Intent intent = new Intent(this, ReadSaveAvtivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void registerClearButton(View view) {
        EditText editText1 = findViewById(R.id.newPassword);
        EditText editText2 = findViewById(R.id.confirmPassword);
        editText1.setText("");
        editText2.setText("");
    }

    public void loginOkButton(View view) {
        if (str.equals(((EditText) findViewById(R.id.password)).getText().toString())) {
            Intent intent = new Intent(this, ReadSaveAvtivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid Password.", Toast.LENGTH_SHORT).show();
        }
    }

    public void loginClearButton(View view) {
        EditText editText = findViewById(R.id.password);
        editText.setText("");
    }
}
