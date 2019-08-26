package com.example.pageturner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    String[] result;
    Context context;
    ArrayList<Drawable> imageList;
    SparseBooleanArray checked;
    private static LayoutInflater inflater=null;

    public CustomAdapter(startPage startPage, String[] imageNames, ArrayList<Drawable> images){
        result = imageNames;
        context = startPage;
        imageList = images;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checked = new SparseBooleanArray();

    }
    public ArrayList<Drawable> getCheckedItems(){
        ArrayList<Drawable> tempArray = new ArrayList<>();
        for(int i=0;i<imageList.size();i++){
            if(checked.get(i)){
                tempArray.add(imageList.get(i));
            }
        }
        return tempArray;
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
        CheckBox checkBox;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.imagelist,null);
        holder.checkBox = (CheckBox)rowView.findViewById(R.id.checkbox);
        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(checked.get(position));
        holder.checkBox.setOnCheckedChangeListener(checkedChangeListener);
        holder.tv = (TextView)rowView.findViewById(R.id.textView1);
        holder.img=(ImageView)rowView.findViewById(R.id.imageView1);
        holder.tv.setText(result[position]);
        holder.img.setImageDrawable(imageList.get(position));
        return rowView;
    }
    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            checked.put((Integer) buttonView.getTag(),isChecked);
        }
    };



}