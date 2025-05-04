package com.example.timesync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuItem;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

public class CustomMenuAdapter extends BaseAdapter {
    private Context context;
    private List<MenuItem> menuItems = new ArrayList<>();

    public CustomMenuAdapter(Context context) {
        this.context = context;
    }

    public void addItem(MenuItem item) {
        menuItems.add(item);
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.popup_menu_item, parent, false);
            
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.itemIcon);
            holder.title = convertView.findViewById(R.id.itemTitle);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        MenuItem item = getItem(position);
        
        // Set the title
        holder.title.setText(item.getTitle());
        
        // Set the icon if available
        Drawable icon = item.getIcon();
        if (icon != null) {
            holder.icon.setImageDrawable(icon);
            holder.icon.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setVisibility(View.GONE);
        }
        
        return convertView;
    }
    
    private static class ViewHolder {
        ImageView icon;
        TextView title;
    }
} 