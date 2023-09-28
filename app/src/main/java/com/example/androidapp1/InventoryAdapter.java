package com.example.androidapp1;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidapp1.Models.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends BaseAdapter {
    private Context context;
    private List<InventoryItem> items;

    public InventoryAdapter(Context context, List<InventoryItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.inventory_item, parent, false);
        }

        ImageView itemImage = convertView.findViewById(R.id.item_image);
        TextView itemName = convertView.findViewById(R.id.item_name);
        ImageView star4 = convertView.findViewById(R.id.star4);
        ImageView star5 = convertView.findViewById(R.id.star5);
        LinearLayout item_background = convertView.findViewById(R.id.item_background);

        // get current item data
        InventoryItem item = items.get(position);

        // set item appearance
        itemImage.setImageResource(item.getImageResource());
        itemName.setText(item.getName());
        if (item.getRarity() == 4) {
            star4.setVisibility(View.VISIBLE);
            item_background.setBackgroundResource(R.color.light_purple);
        }
        if (item.getRarity() == 5) {
            star4.setVisibility(View.VISIBLE);
            star5.setVisibility(View.VISIBLE);
            item_background.setBackgroundResource(R.color.light_orange);
        }
        return convertView;
    }
}
