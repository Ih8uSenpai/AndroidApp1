package com.example.androidapp1.fragments;

import static com.example.androidapp1.activities.HomeActivity.current_user_data;
import static com.example.androidapp1.activities.HomeActivity.getCurrent_user_data;
import static com.example.androidapp1.activities.HomeActivity.parseItemsFromDB_cones;

import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.androidapp1.activities.AuthorizationActivity;
import com.example.androidapp1.adapters.CharactersConeAdapter;
import com.example.androidapp1.models.Character;
import com.example.androidapp1.models.ConeUserdata;
import com.example.androidapp1.utils.LoadModelsTask;
import com.example.androidapp1.models.UserData;
import com.example.androidapp1.utils.MyRenderer;
import com.example.androidapp1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.rajawali3d.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CharactersFragment extends Fragment {

    //database
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference usersData;
    DatabaseReference users;
    ArrayAdapter adapter;


    private MyRenderer renderer_kiana;
    ImageButton kiana_icon;
    ImageButton kafka_icon;
    ImageButton blade_icon;
    ImageButton jingliu_icon;
    SurfaceView surfaceViewKiana;
    VideoView bg_char;
    AppCompatButton minus_y;
    AppCompatButton plus_y;
    AppCompatButton minus_radius;
    AppCompatButton plus_radius;
    LinearLayout sections_menu;
    LinearLayout characters_panel;
    ImageButton to_home;
    private VideoView character_image;
    private AppCompatButton show_3d_model;
    ConstraintLayout constraintLayout;
    // char stats views
    TextView lvl, hp, def, attack, crit_rate, crit_dmg, character_name;
    ProgressBar exp;
    int[] characters_exp_table = new int[]{100, 200, 320, 450, 600, 850, 1200, 1600, 2200, 3000,
            3751, 4212, 4912, 5921, 7123, 8234, 9653, 10923, 12402, 14329,
            16481, 18982, 21424, 24021, 27965, 32420, 36831, 41242, 47634, 55131,
            62521, 70123, 79123, 89123, 103211, 112381, 124181, 137123, 151271, 165247,
            181427, 196148, 216471, 238131, 260812, 284881, 310161, 342518, 381271, 459211,
            561412, 725219, 920001, 1201231, 1601231, 2301231, 3101231, 4101231, 5501231, 10501231, 17501231};
    public static Character characters_current_character;
    AppCompatButton level_up_button;
    TextView expTextView;

    private ConstraintLayout characters_cones_screen;
    private GridView characters_inventory_grid_cones;
    private LinearLayout characters_cone_details;
    private TextView characters_cone_name_details;
    private View characters_cone_name_details_line;
    private ImageView characters_cone_image_details;
    private View characters_details_image_bottom_line;
    private ImageView characters_star4_details;
    private ImageView characters_star5_details;
    private ImageView characters_hp_stat_cone_image_details;
    private TextView characters_hp_stat_cone_text_details;
    private ImageView characters_atk_stat_cone_image_details;
    private TextView characters_atk_stat_cone_text_details;
    private ImageView characters_def_stat_cone_image_details;
    private TextView characters_def_stat_cone_text_details;
    private ConstraintLayout characters_cone_lvl_layout_details;
    private TextView characters_cone_lvl_details;
    private TextView characters_item_ability_details;
    private TextView characters_item_description;
    private AppCompatButton equip_cone;
    private AppCompatButton enhance_cone;
    private ImageButton details_cone;
    private ConstraintLayout characters_details_screen;
    private ConstraintLayout cone_section_screen;
    private ImageButton cones_to_details;
    List<ConeUserdata> items_cone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characters, container, false);

        init_all(view);
        // pull data from db
        setStartCurrentCharacter();
        characters_inventory_grid_cones.setAdapter(new CharactersConeAdapter(CharactersFragment.this.getContext(), items_cone, view, usersData));
        setCurrentCharacterDataToView(characters_current_character);
        setVisibleForObtainedCharacters();
        SetOnClickListeners();
        character_image.setVisibility(View.VISIBLE);
        // setting 3d model
        surfaceViewKiana.setZOrderOnTop(true);
        surfaceViewKiana.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        renderer_kiana = new MyRenderer(requireContext(), "kiana");
        // render surface
        surfaceViewKiana.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        surfaceViewKiana.setSurfaceRenderer(renderer_kiana);
        show_3d_model.setVisibility(View.VISIBLE);

        return view;
    }

    private void init_all(View view) {
        surfaceViewKiana = view.findViewById(R.id.surface_view_kiana);
        kiana_icon = view.findViewById(R.id.kiana_icon);
        kafka_icon = view.findViewById(R.id.kafka_icon);
        blade_icon = view.findViewById(R.id.blade_icon);
        jingliu_icon = view.findViewById(R.id.jingliu_icon);

        character_image = view.findViewById(R.id.character_image);
        to_home = view.findViewById(R.id.charactersToHome);
        show_3d_model = view.findViewById(R.id.show_3d_model);
        constraintLayout = view.findViewById(R.id.characters_loading_screen);

        // database interaction variables
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usersData = FirebaseDatabase.getInstance().getReference("UsersData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        users = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.activity_list_item);

        // set userData fields
        lvl = view.findViewById(R.id.details_lvl);
        exp = view.findViewById(R.id.details_lvl_progress);
        character_name = view.findViewById(R.id.details_character_name);
        character_name.setPaintFlags(character_name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        hp = view.findViewById(R.id.details_stats_hp);
        attack = view.findViewById(R.id.details_stats_atk);
        def = view.findViewById(R.id.details_stats_def);
        crit_rate = view.findViewById(R.id.details_stats_critRate);
        crit_dmg = view.findViewById(R.id.details_stats_critDMG);

        level_up_button = view.findViewById(R.id.details_level_up_button);
        characters_panel = view.findViewById(R.id.characters_panel);
        sections_menu = view.findViewById(R.id.sections_menu);
        expTextView = view.findViewById(R.id.expTextView);


        characters_cones_screen = view.findViewById(R.id.characters_cones_screen);
        characters_inventory_grid_cones = view.findViewById(R.id.characters_inventory_grid_cones);
        characters_cone_details = view.findViewById(R.id.characters_cone_details);
        characters_cone_name_details = view.findViewById(R.id.characters_cone_name_details);
        characters_cone_name_details_line = view.findViewById(R.id.characters_cone_name_details_line);
        characters_cone_image_details = view.findViewById(R.id.characters_cone_image_details);
        characters_details_image_bottom_line = view.findViewById(R.id.characters_details_image_bottom_line);
        characters_star4_details = view.findViewById(R.id.characters_star4_details);
        characters_star5_details = view.findViewById(R.id.characters_star5_details);
        characters_hp_stat_cone_image_details = view.findViewById(R.id.characters_hp_stat_cone_image_details);
        characters_hp_stat_cone_text_details = view.findViewById(R.id.characters_hp_stat_cone_text_details);
        characters_atk_stat_cone_image_details = view.findViewById(R.id.characters_atk_stat_cone_image_details);
        characters_atk_stat_cone_text_details = view.findViewById(R.id.characters_atk_stat_cone_text_details);
        characters_def_stat_cone_image_details = view.findViewById(R.id.characters_def_stat_cone_image_details);
        characters_def_stat_cone_text_details = view.findViewById(R.id.characters_def_stat_cone_text_details);
        characters_cone_lvl_layout_details = view.findViewById(R.id.characters_cone_lvl_layout_details);
        characters_cone_lvl_details = view.findViewById(R.id.characters_cone_lvl_details);
        characters_item_ability_details = view.findViewById(R.id.characters_item_ability_details);
        characters_item_description = view.findViewById(R.id.characters_item_description);
        equip_cone = view.findViewById(R.id.equip_cone);
        enhance_cone = view.findViewById(R.id.enhance_cone);
        details_cone = view.findViewById(R.id.details_cone);
        characters_details_screen = view.findViewById(R.id.characters_details_screen);
        cone_section_screen = view.findViewById(R.id.cone_section_screen);
        cones_to_details = view.findViewById(R.id.cones_to_details);


        items_cone = parseItemsFromDB_cones();
    }

    public void SetOnClickListeners() {
        kiana_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.kiana_background_vid));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                character_image.setVisibility(View.VISIBLE);
                show_3d_model.setVisibility(View.VISIBLE);
                characters_current_character = current_user_data.getCharacterByName("Kiana");
                setCurrentCharacterDataToView(characters_current_character);
            }
        });

        show_3d_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (character_image.getVisibility() == View.GONE) {
                    character_image.setVisibility(View.VISIBLE);
                    show_3d_model.setText("3d model");
                } else {
                    character_image.setVisibility(View.GONE);
                    show_3d_model.setText("2d model");
                    if (renderer_kiana != null) {
                        new LoadModelsTask(renderer_kiana, constraintLayout).execute(); // async model loading
                        surfaceViewKiana.setVisibility(View.VISIBLE);
                    } else {
                        renderer_kiana.onResume();
                    }
                }
            }
        });

        kafka_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.kafka_background_vid));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                character_image.setVisibility(View.VISIBLE);
                show_3d_model.setVisibility(View.GONE);
                characters_current_character = current_user_data.getCharacterByName("Kafka");
                setCurrentCharacterDataToView(characters_current_character);
            }
        });

        blade_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.blade_background_vid));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                character_image.setVisibility(View.VISIBLE);
                show_3d_model.setVisibility(View.GONE);
                characters_current_character = current_user_data.getCharacterByName("Blade");
                setCurrentCharacterDataToView(characters_current_character);
            }
        });
        jingliu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.jingliu_background));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                character_image.setVisibility(View.VISIBLE);
                show_3d_model.setVisibility(View.GONE);
                characters_current_character = current_user_data.getCharacterByName("Jing Liu");
                setCurrentCharacterDataToView(characters_current_character);
            }
        });

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof FragmentInteractionListener) {
                    ((FragmentInteractionListener) getActivity()).onFragmentClosed();
                }
            }
        });

        level_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                characters_current_character.changeExp(100, usersData);
                setCurrentCharacterDataToView(characters_current_character);
                expTextView.setVisibility(View.VISIBLE);

                // Создаем анимацию для исчезновения TextView через 2 секунды
                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(2000);
                expTextView.startAnimation(anim);

                // По завершении анимации скрываем TextView
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Пустая реализация
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        expTextView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Пустая реализация
                    }
                });
            }
        });
        details_cone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (characters_current_character.equippedCone() == null){
                    System.out.println("GET CONE = NULL");
                    openConesScreen();
                }
                else{
                    /*
                    System.out.println("GET CONE != NULL");
                    openConeSection();*/
                    openConesScreen();
                }
            }
        });
        cones_to_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                characters_cones_screen.setVisibility(View.GONE);
                characters_details_screen.setVisibility(View.VISIBLE);
                setCurrentCharacterDataToView(characters_current_character);
            }
        });





    }

    private void openConeSection() {
        System.out.println("CONE SECTION OPENED");
        characters_details_screen.setVisibility(View.INVISIBLE);
        cone_section_screen.setVisibility(View.VISIBLE);
    }

    private void  openConesScreen() {
        System.out.println("CONE SCREEN OPENED");
        characters_details_screen.setVisibility(View.INVISIBLE);
        characters_cones_screen.setVisibility(View.VISIBLE);
    }

    private void setCurrentCharacterDataToView(Character characters_current_character) {
        Locale locale = Locale.US; // You can choose the appropriate locale

        lvl.setText(getString(R.string.character_level,characters_current_character.getCharacter_lvl()));
        hp.setText(String.format(locale, "%d", characters_current_character.HpValue()));
        def.setText(String.format(locale, "%d", characters_current_character.DefValue()));
        attack.setText(String.format(locale, "%d", characters_current_character.AtkValue()));
        crit_rate.setText(String.format(locale, "%d%%", characters_current_character.getCrit_chance()));
        crit_dmg.setText(String.format(locale, "%d", characters_current_character.getCrit_dmg()) + "%");
        character_name.setText(characters_current_character.getName());
        exp.setMax(characters_exp_table[characters_current_character.getCharacter_lvl() + 1] - characters_exp_table[characters_current_character.getCharacter_lvl()]);
        exp.setProgress(characters_current_character.getExp() - characters_exp_table[characters_current_character.getCharacter_lvl()]);
        if (characters_current_character.equippedCone() != null) {
            details_cone.setImageResource(characters_current_character.equippedCone().coneInfo().getImageResource());
        }
        else {
            details_cone.setImageResource(0);
        }
        setStartBackground();
    }

    private void setStartBackground() {
        switch (characters_current_character.getName()){
            case "Kiana":
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.kiana_background_vid));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                break;
            case "Kafka":
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.kafka_background_vid));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                break;
            case "Blade":
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.blade_background_vid));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                break;
            case "Jing Liu":
                character_image.setVideoURI(Uri.parse( "android.resource://com.example.androidapp1/" + R.raw.jingliu_background));
                character_image.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo(0);
                        mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                    }
                });
                break;
            default:
                break;
        }
    }


    public void setVisibleForObtainedCharacters(){
        ArrayList<Character> characters = current_user_data.getCharacters();
        for (Character character: characters) {
            if (character.isObtained()){
                System.out.println("character" + character.getName() + " obtained");
                setVisibleForCharacter(character.getName());
            }
            else {
                System.out.println("character" + character.getName() + " is NOT obtained");
                System.out.println(character);
            }
        }
    }
    public void setVisibleForCharacter(String name){
        switch (name){
            case "Kiana":
                kiana_icon.setVisibility(View.VISIBLE);
                break;
            case "Kafka":
                kafka_icon.setVisibility(View.VISIBLE);
                break;
            case "Blade":
                blade_icon.setVisibility(View.VISIBLE);
                break;
            case "Jing Liu":
                jingliu_icon.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public void setStartCurrentCharacter(){
        ArrayList<Character> characters = current_user_data.getCharacters();
        for (Character character: characters) {
            if (character.isObtained()){
                characters_current_character = character;
                switch (character.getName()){
                    case "Kiana":
                        kiana_icon.performClick();
                        break;
                    case "Kafka":
                        kafka_icon.performClick();
                        break;
                    case "Blade":
                        blade_icon.performClick();
                        break;
                    case "Jing Liu":
                        jingliu_icon.performClick();
                        break;
                    default:
                        break;
                }
                return;
            }
        }
    }



}
