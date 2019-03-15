package com.example.liuwd8.storagedatabysql;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {
    private UserData userData;
    private List<CommentData> list = new ArrayList<>();
    private MyListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        userData = UserData.current;
        myDB db = new myDB(getApplicationContext());
        list = db.query();

        ListView listView = findViewById(R.id.listView);
        final MyListViewAdapter myListViewAdapter = new MyListViewAdapter(getApplicationContext(), list);
        listView.setAdapter(myListViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickItem(parent, view, position, id);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickItem(parent, view, position, id);
                return true;
            }
        });
        adapter = myListViewAdapter;
    }

    public void clickItem(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = \"" + list.get(position).getUsername() + "\"", null, null);
        StringBuilder stringBuilder = new StringBuilder();
        if (cursor != null && cursor.moveToNext()) {
            stringBuilder.append("\nPhone: ");
            cursor.moveToFirst();
            do {
                stringBuilder.append(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))).append("         ");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            stringBuilder.append("\nPhone number not exist.");
        }
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommentActivity.this);
        alertDialog.setTitle("Info").setMessage("Username: "+ list.get(position).getUsername() + stringBuilder.toString()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        }).create().show();
    }

    public void longClickItem(AdapterView<?> parent, View view, final int position, long id) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommentActivity.this);
        if (list.get(position).getUsername().equals(userData.getUsername())) {
            alertDialog.setTitle("Delete or not?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDB db = new myDB(getApplicationContext());
                    db.deleteComment(list.get(position));
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            alertDialog.setTitle("Report or not?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Report successful.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    public void clickSendButton(View view) {
        EditText editText = findViewById(R.id.commentArea);
        if (editText.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Comment connot be empty.", Toast.LENGTH_SHORT).show();
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault(Locale.Category.FORMAT));
            CommentData commentData = new CommentData(userData.getUsername(), editText.getText().toString(), format.format(new Date()), userData.getAvatar(), 0, 0);
            myDB db = new myDB(getApplicationContext());
            if (db.save(commentData) == -1) {
                Toast.makeText(getApplicationContext(), "An Error has occur.", Toast.LENGTH_SHORT).show();
            } else {
                list = db.query();
                adapter.refresh(list);
                editText.setText("");
            }
        }
    }
}
