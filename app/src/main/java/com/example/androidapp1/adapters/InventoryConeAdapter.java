package com.example.androidapp1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.androidapp1.activities.HomeActivity;
import com.example.androidapp1.models.Cone;
import com.example.androidapp1.R;
import com.example.androidapp1.models.ConeUserdata;
import com.example.androidapp1.utils.Constants;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class InventoryConeAdapter extends BaseAdapter {
    private Context context;
    private List<ConeUserdata> items;

    private View rootView;
    DatabaseReference usersData;




    public InventoryConeAdapter(Context context, List<ConeUserdata> items, View rootView, DatabaseReference usersData) {
        this.context = context;
        this.items = items;
        this.rootView = rootView;
        this.usersData = usersData;
    }

    public InventoryConeAdapter() {
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
        TextView item_description = rootView.findViewById(R.id.item_description);
        TextView item_name_details = rootView.findViewById(R.id.item_name_details);
        LinearLayout item_details = rootView.findViewById(R.id.item_details);
        ImageView item_image_details = rootView.findViewById(R.id.item_image_details);
        TextView item_name_details_line = rootView.findViewById(R.id.item_name_details_line);
        TextView hp_stat_cone_text_details = rootView.findViewById(R.id.hp_stat_cone_text_details);
        TextView atk_stat_cone_text_details = rootView.findViewById(R.id.atk_stat_cone_text_details);
        TextView def_stat_cone_text_details = rootView.findViewById(R.id.def_stat_cone_text_details);
        TextView item_ability_details = rootView.findViewById(R.id.item_ability_details);
        TextView item_lvl = rootView.findViewById(R.id.cone_lvl_details);
        ImageView star4_details = rootView.findViewById(R.id.star4_details), star5_details = rootView.findViewById(R.id.star5_details);
        AppCompatButton level_up_details = rootView.findViewById(R.id.level_up_details);

        //LinearLayout item_details = convertView.findViewById(R.id.item_details);
        // get current item data
        ConeUserdata item = items.get(position);
        Cone cone = item.getConeInfo();
        // set item appearance
        itemImage.setImageResource(item.getConeInfo().getImageResource());
        itemName.setText(item.getName());
        if (cone.getRarity() == 4) {
            star4.setVisibility(View.VISIBLE);
            item_background.setBackgroundResource(R.color.light_purple);
        }
        if (cone.getRarity() == 5) {
            star4.setVisibility(View.VISIBLE);
            star5.setVisibility(View.VISIBLE);
            item_background.setBackgroundResource(R.color.light_orange);
        }

        // Handle item click
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update item_name_in_details and item_description
                item_name_details.setText(item.getName());
                item_description.setText(cone.getDescription());
                item_image_details.setImageResource(cone.getImageResource());
                item_lvl.setText(String.format("%d/80", item.getLvl()));
                int hp = cone.getBase_hp() + item.getConeInfo().getHp_growth() * item.getLvl();
                int atk = cone.getBase_atk() + item.getConeInfo().getAtk_growth() * item.getLvl();
                int def = cone.getBase_def() + item.getConeInfo().getDef_growth() * item.getLvl();
                hp_stat_cone_text_details.setText(String.format("%d", hp));
                atk_stat_cone_text_details.setText(String.format("%d", atk));
                def_stat_cone_text_details.setText(String.format("%d", def));
                item_ability_details.setText(cone.getAbility());
                if (cone.getRarity() == 3) {
                    int color = ContextCompat.getColor(context, R.color.light_blue);
                    item_name_details.setTextColor(color);
                    star4_details.setVisibility(View.GONE);
                    star5_details.setVisibility(View.GONE);
                    item_name_details_line.setBackgroundResource(R.color.light_blue);
                }
                if (cone.getRarity() == 4) {
                    int color = ContextCompat.getColor(context, R.color.light_purple);
                    item_name_details.setTextColor(color);
                    star4_details.setVisibility(View.VISIBLE);
                    star5_details.setVisibility(View.GONE);
                    item_name_details_line.setBackgroundResource(R.color.light_purple);
                }
                if (cone.getRarity() == 5) {
                    int color = ContextCompat.getColor(context, R.color.light_orange);
                    item_name_details.setTextColor(color);
                    star4_details.setVisibility(View.VISIBLE);
                    star5_details.setVisibility(View.VISIBLE);
                    item_name_details_line.setBackgroundResource(R.color.light_orange);
                }
                item_details.setVisibility(View.VISIBLE);
                level_up_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.changeExp(100, Constants.cone_exp_table, usersData);

                        item_lvl.setText(String.format("%d/80", item.getLvl()));
                        int hp = cone.getBase_hp() + item.getConeInfo().getHp_growth() * item.getLvl();
                        int atk = cone.getBase_atk() + item.getConeInfo().getAtk_growth() * item.getLvl();
                        int def = cone.getBase_def() + item.getConeInfo().getDef_growth() * item.getLvl();
                        hp_stat_cone_text_details.setText(String.format("%d", hp));
                        atk_stat_cone_text_details.setText(String.format("%d", atk));
                        def_stat_cone_text_details.setText(String.format("%d", def));
                    }
                });
            }
        });

        return convertView;
    }
}
