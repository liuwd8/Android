package com.example.liuyh73.glory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class mainPage_ListViewAdapter extends ArrayAdapter<String> {
    private int resourceId;
    //context需要在使用这个Adapter的Activity中传入。
    public mainPage_ListViewAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        // 当view为空时才加载布局，否则，直接修改内容
        String data = getItem(position); //获取当前项的String
        View convertView;
        ViewHolder viewHolder;
        if(view == null){
            convertView = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.HeroType = convertView.findViewById(R.id.mainPage_listView_Text);
            convertView.setTag(viewHolder);
        }
        else{
            convertView = view;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.HeroType.setText(data);
        return convertView;
    }

    class ViewHolder {
        TextView HeroType;
    }
}
