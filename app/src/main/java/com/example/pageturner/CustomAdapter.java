package com.example.pageturner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    String[] result;
    Context context;
    Drawable[] imageList;
    private static LayoutInflater inflater=null;
    public CustomAdapter(pictures pictures, String[] imageNames, Drawable[] images){
        result = imageNames;
        context = pictures;
        imageList = images;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount(){
        return result.length;
    }
    @Override
    public Object getItem(int position){
        return position;
    }
    @Override
    public long getItemId(int position){
        return position;
    }
    public class Holder{
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.imagelist,null);
        holder.tv = (TextView)rowView.findViewById(R.id.textView1);
        holder.img=(ImageView)rowView.findViewById(R.id.imageView1);
        holder.tv.setText(result[position]);
        holder.img.setImageDrawable(imageList[position]);
        return rowView;
    }



}