package com.example.liuyh73.glory;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyReccleViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    private MyReccleViewHolder(View v){
        super(v);
        mConvertView = v;
        mViews = new SparseArray<>();
    }

    public static MyReccleViewHolder get(ViewGroup parent, int layoutId){
        View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new MyReccleViewHolder(convertView);
    }

    public <T extends View> T getView(int id){
        View v = mViews.get(id);
        if(v == null){
            v = mConvertView.findViewById(id);
            mViews.put(id, v);
        }
        return (T)v;
    }
}
