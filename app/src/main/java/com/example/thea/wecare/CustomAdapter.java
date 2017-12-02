package com.example.thea.wecare;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Item> {

    private Context context;

    public CustomAdapter(Context context, int resourceid, List<Item> Items){
        super (context, resourceid, Items);
        this.context = context;
    }
    private class ViewHolder{
        TextView title;
    }
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        ViewHolder holder = null;
        Item rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (context != null){
            convertView = mInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (rowItem != null){
            holder.title.setText(rowItem.getTitle());
        }
        return convertView;
    }

}
