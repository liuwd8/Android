package com.example.liuwd8.httpapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class MyRecyclerViewAdapter<T> extends RecyclerView.Adapter<MyViewHolder> {
//    private OnItemClickListener onItemClickListener;
    private Context context;
    private int layoutId;
    private List<T> data;

    public MyRecyclerViewAdapter(Context _context, int _layoutId, List<T> _data) {
        context = _context;
        layoutId = _layoutId;
        data = _data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.get(context, parent, layoutId);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        convert(holder, data.get(position)); // convert函数需要重写，下面会讲
//        if (onItemClickListener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onItemClickListener.onClick(holder.getAdapterPosition());
//                }
//            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    onItemClickListener.onLongClick(holder.getAdapterPosition());
//                    return true;
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        if(data == null) {
            return 0;
        }
        return data.size();
    }

    public abstract void convert(MyViewHolder holder, T t);

//    public interface OnItemClickListener{
//        void onClick(int position);
//        void onLongClick(int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener _onItemClickListener) {
//        this.onItemClickListener = _onItemClickListener;
//    }
}
