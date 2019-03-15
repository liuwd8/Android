package com.example.liuwd8.storagedatabysql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    Context context;
    private List<CommentData> list;
    public MyListViewAdapter(Context c, List<CommentData> _list) {
        context = c;
        list = _list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View convertView;
        ViewHolder viewHolder;
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = convertView.findViewById(R.id.avatar);
            viewHolder.username = convertView.findViewById(R.id.commentUsername);
            viewHolder.date = convertView.findViewById(R.id.commentDate);
            viewHolder.comment =convertView.findViewById(R.id.commentItem);
            viewHolder.star = convertView.findViewById(R.id.starNum);
            viewHolder.starImage = convertView.findViewById(R.id.starImage);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CommentData commentData = list.get(position);
        viewHolder.avatar.setImageBitmap(commentData.getAvatar());
        viewHolder.username.setText(commentData.getUsername());
        viewHolder.star.setText(String.valueOf(commentData.getStar()));
        boolean status = false;
        for (Integer i : UserData.current.getArrayList()) {
            if (i == commentData.get_id()) {
                status = true;
                break;
            }
        }
        if (status) {
            viewHolder.starImage.setImageResource(R.mipmap.red);
        } else {
            viewHolder.starImage.setImageResource(R.mipmap.white);
        }
        final ImageView temp = viewHolder.starImage;
        viewHolder.starImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;
                for (Integer i : UserData.current.getArrayList()) {
                    if (i == commentData.get_id()) {
                        commentData.setStar(commentData.getStar() - 1);
                        UserData.current.getArrayList().remove(i);
                        status = false;
                        temp.setImageResource(R.mipmap.white);
                        break;
                    }
                }
                if (status) {
                    commentData.setStar(commentData.getStar() + 1);
                    UserData.current.getArrayList().add(commentData.get_id());
                    temp.setImageResource(R.mipmap.red);
                }
                myDB db = new myDB(context);
                db.updateStar(commentData);
                myUserDB db1 = new myUserDB(context);
                db1.updateLikeList(UserData.current.getUsername(), UserData.current.getArrayList());
                notifyDataSetChanged();
            }
        });
        viewHolder.date.setText(commentData.getDate());
        viewHolder.comment.setText(commentData.getComment());

        return convertView;
    }

    void refresh(List<CommentData> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    private class ViewHolder {
        public ImageView avatar;
        public ImageView starImage;
        public TextView username;
        public TextView date;
        public TextView star;
        public TextView comment;
    }
}
