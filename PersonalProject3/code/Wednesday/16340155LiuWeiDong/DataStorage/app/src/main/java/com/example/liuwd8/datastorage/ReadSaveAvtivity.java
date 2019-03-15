package com.example.liuwd8.datastorage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadSaveAvtivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_read_save);
    }

    public void saveButtonClicked(View view) {
        File file = new File(getExternalFilesDir(null), "DemoFile.txt");

        try {
            FileOutputStream os = new FileOutputStream(file);
            EditText editText = findViewById(R.id.message);
            byte[] data = editText.getText().toString().getBytes();
            os.write(data);
            os.close();
            Toast.makeText(getApplicationContext(), "Save successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Failed to save file.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void loadButtonClicked(View view) {
        File file = new File(getExternalFilesDir(null), "DemoFile.txt");

        try {
            FileInputStream is = new FileInputStream(file);
            int length = is.available();
            byte[] data = new byte[length];
            is.read(data);
            EditText editText = findViewById(R.id.message);
            editText.setText(new String(data));
            is.close();
            Toast.makeText(getApplicationContext(), "Load successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Failed to load file.", Toast.LENGTH_SHORT).show();
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    public void clearButtonClicked(View view) {
        EditText editText = findViewById(R.id.message);
        editText.setText("");
    }
}
