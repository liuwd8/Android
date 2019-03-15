package com.example.liuyh73.glory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import java.util.ArrayList;

public class MyListViewAdapter extends BaseAdapter {
    private ArrayList<Hero> list;
    private Context context;

    public MyListViewAdapter(Context _context, ArrayList<Hero> list) {
        this.context = _context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            view = LayoutInflater.from(context).inflate(R.layout.add_item, null);
            viewHolder = new ViewHolder();
            // todo: 绑定信息
            viewHolder.name = (TextView) view.findViewById(R.id.add_name);
            viewHolder.type = (TextView) view.findViewById(R.id.add_type);
            viewHolder.avater = view.findViewById(R.id.add_avater);
            viewHolder.title = view.findViewById(R.id.add_title);

            convertView = view;
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else { // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        // todo: 去除对应的对象绑定信息
        viewHolder.name.setText(list.get(i).getName());
        viewHolder.type.setText(list.get(i).getHero_type());
        viewHolder.title.setText(list.get(i).getTitle());
        Glide.with(context).load(list.get(i).getAvaterUrl()).into(viewHolder.avater);
        // 将这个处理好的view返回
        return convertView;
    }

    // 更新页面
    public void refresh(ArrayList<Hero> _list) {
        list = _list;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        // todo: Hero显示
        public TextView name;
        public TextView type;
        public ImageView avater;
        public TextView title;
    }
}