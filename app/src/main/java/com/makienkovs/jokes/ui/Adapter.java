package com.makienkovs.jokes.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makienkovs.jokes.R;
import com.makienkovs.jokes.network.Value;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    private ArrayList<Value> posts;
    public Adapter(ArrayList<Value> posts) {
        this.posts = posts;
    }

    @Override
    public int getCount() {
        if (posts == null) return 0;
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        if (posts == null) return null;
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, null);
        fillView(convertView, position);
        return convertView;
    }

    private void fillView(View convertView, int position) {
        Value post = (Value) getItem(position);
        TextView textViewPost = convertView.findViewById(R.id.textViewPost);
        textViewPost.setText(post.getJoke());
    }
}