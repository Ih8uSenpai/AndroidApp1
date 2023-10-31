package com.example.androidapp1.adapters;

import static com.example.androidapp1.activities.HomeActivity.current_user_data;
import static com.example.androidapp1.activities.HomeActivity.getCharacterIconById;
import static com.example.androidapp1.activities.HomeActivity.getCharacterIconByName;
import static com.example.androidapp1.activities.HomeActivity.parseItemsFromDB_cones;
import static com.example.androidapp1.activities.HomeActivity.usersData;
import static com.example.androidapp1.fragments.CharactersFragment.characters_current_character;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.androidapp1.models.Character;
import com.example.androidapp1.models.Cone;
import com.example.androidapp1.R;
import com.example.androidapp1.models.ConeUserdata;
import com.example.androidapp1.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Objects;

public class CharactersConeAdapter extends BaseAdapter {
    private Context context;
    private List<ConeUserdata> items;

    private View rootView;
    DatabaseReference usersData;


    public CharactersConeAdapter(Context context, List<ConeUserdata> items, View rootView, DatabaseReference usersData) {
        this.context = context;
        this.items = items;
        this.rootView = rootView;
        this.usersData = usersData;
    }

    public CharactersConeAdapter() {
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

        System.out.println(items);
        ImageView itemImage = convertView.findViewById(R.id.item_image);
        TextView itemName = convertView.findViewById(R.id.item_name);
        ImageView star4 = convertView.findViewById(R.id.star4);
        ImageView star5 = convertView.findViewById(R.id.star5);
        LinearLayout item_background = convertView.findViewById(R.id.item_background);
        ImageView cone_character_icon = convertView.findViewById(R.id.cone_character_icon);
        TextView item_description = rootView.findViewById(R.id.characters_item_description);
        TextView item_name_details = rootView.findViewById(R.id.characters_cone_name_details);
        LinearLayout item_details = rootView.findViewById(R.id.characters_cone_details);
        ImageView item_image_details = rootView.findViewById(R.id.characters_cone_image_details);
        TextView item_name_details_line = rootView.findViewById(R.id.characters_cone_name_details_line);
        TextView hp_stat_cone_text_details = rootView.findViewById(R.id.characters_hp_stat_cone_text_details);
        TextView atk_stat_cone_text_details = rootView.findViewById(R.id.characters_atk_stat_cone_text_details);
        TextView def_stat_cone_text_details = rootView.findViewById(R.id.characters_def_stat_cone_text_details);
        TextView item_ability_details = rootView.findViewById(R.id.characters_item_ability_details);
        TextView item_lvl = rootView.findViewById(R.id.characters_cone_lvl_details);
        ImageView star4_details = rootView.findViewById(R.id.characters_star4_details), star5_details = rootView.findViewById(R.id.characters_star5_details);
        AppCompatButton enhance_cone = rootView.findViewById(R.id.enhance_cone);
        AppCompatButton equip_cone = rootView.findViewById(R.id.equip_cone);
        ImageButton cones_to_details = rootView.findViewById(R.id.cones_to_details);
        TextView enhance_cone_text_exp = rootView.findViewById(R.id.enhance_cone_text_exp);

        //LinearLayout item_details = convertView.findViewById(R.id.item_details);
        // get current item data
        ConeUserdata item = items.get(position);
        Cone cone = item.coneInfo();


        // set item appearance
        itemImage.setImageResource(item.coneInfo().getImageResource());
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
                int hp = item.HpValue();
                int atk = item.AtkValue();
                int def = item.DefValue();
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


                /*
                ERRORS COULD BE HERE BRO!!!
                 */

                enhance_cone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enhance_cone_text_exp.setVisibility(View.VISIBLE);
                        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                        anim.setDuration(2000);
                        enhance_cone_text_exp.startAnimation(anim);

                        anim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                enhance_cone_text_exp.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        item.changeExp(100, Constants.cone_exp_table, usersData);

                        item_lvl.setText(String.format("%d/80", item.getLvl()));
                        int hp = item.HpValue();
                        int atk = item.AtkValue();
                        int def = item.DefValue();
                        hp_stat_cone_text_details.setText(String.format("%d", hp));
                        atk_stat_cone_text_details.setText(String.format("%d", atk));
                        def_stat_cone_text_details.setText(String.format("%d", def));
                    }
                });


                equip_cone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // надеваем новый конус
                        equipCone(item);
                        cones_to_details.performClick();
                        //showOrHideCharacterIcon(item, cone_character_icon);
                    }
                });

            }
        });

        showOrHideCharacterIcon(item, cone_character_icon);
        return convertView;
    }


    public void equipCone(ConeUserdata selectedConeUserdata) {
        // снимаем конус с перса на котором он был надет
        if (selectedConeUserdata.getCharacter_id() != -1) {
            //unEquipCharacter(current_user_data.getCharacters().get(selectedConeUserdata.getCharacter_id()));
            current_user_data.getCharacters().get(selectedConeUserdata.getCharacter_id()).setCone_id("null");
        }
        // снимаем конус который надет на текущем персе
        if (!Objects.equals(characters_current_character.getCone_id(), "null")) {
            //unEquipCharacter(characters_current_character);
            current_user_data.getCones().get(Integer.parseInt(characters_current_character.getCone_id())).changeCharacter_id(-1);
        }

        characters_current_character.setCone_id(String.valueOf(selectedConeUserdata.getId()));
        selectedConeUserdata.changeCharacter_id(Integer.parseInt(characters_current_character.getId()));
        usersData.child("cones").child(String.valueOf(selectedConeUserdata.getId())).child("character_id").setValue(Integer.parseInt(characters_current_character.getId()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        reloadDataFromFirebase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("DATA_ERROR", "Error updating data", e);
                        // Обработка ошибки обновления данных.
                    }
                });
    }
    public void showOrHideCharacterIcon(ConeUserdata item, ImageView cone_character_icon) {
        if (item.getCharacter_id() != -1) {
            cone_character_icon.setImageResource(getCharacterIconById(item.getCharacter_id()));
            cone_character_icon.setVisibility(View.VISIBLE);
            Log.d("CHAR_ICON", "Setting character icon for cone ID: " + item.getId());
        } else {
            cone_character_icon.setVisibility(View.INVISIBLE);
            Log.d("CHAR_ICON", "Hiding character icon for cone ID: " + item.getId());
        }
    }

    public void reloadDataFromFirebase() {
        items = parseItemsFromDB_cones();
        notifyDataSetChanged();
    }


}
