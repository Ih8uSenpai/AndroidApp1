package com.example.androidapp1.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.androidapp1.db_manage.UsersDataFirebaseManager;
import com.example.androidapp1.models.Character;
import com.example.androidapp1.utils.LoadModelsTask;
import com.example.androidapp1.models.UserData;
import com.example.androidapp1.utils.MyRenderer;
import com.example.androidapp1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.rajawali3d.view.SurfaceView;

import java.util.ArrayList;

public class CharactersFragment extends Fragment {

    //database
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference usersData;
    DatabaseReference users;
    ArrayAdapter adapter;

    UserData current_user_data;

    private MyRenderer renderer_kiana;
    ImageButton kiana_icon;
    ImageButton kafka_icon;
    ImageButton blade_icon;
    SurfaceView surfaceViewKiana;
    VideoView bg_char;
    AppCompatButton minus_y;
    AppCompatButton plus_y;
    AppCompatButton minus_radius;
    AppCompatButton plus_radius;
    ImageButton to_home;
    private ImageView character_image;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_characters, container, false);
        init_all(view);
        SetOnClickListeners();
        // pull data from db
        getUsersData();
        character_image.setImageResource(R.drawable.kiana_bg);
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
        hp = view.findViewById(R.id.details_stats_hp);
        attack = view.findViewById(R.id.details_stats_atk);
        def = view.findViewById(R.id.details_stats_def);
        crit_rate = view.findViewById(R.id.details_stats_critRate);
        crit_dmg = view.findViewById(R.id.details_stats_critDMG);

    }

    public void SetOnClickListeners() {
        kiana_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setImageResource(R.drawable.kiana_bg);
                character_image.setVisibility(View.VISIBLE);
                show_3d_model.setVisibility(View.VISIBLE);
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
                character_image.setImageResource(R.drawable.kafka_bg);
                character_image.setVisibility(View.VISIBLE);
                show_3d_model.setVisibility(View.GONE);
            }
        });

        blade_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setImageResource(R.drawable.blade_bg1);
                character_image.setVisibility(View.VISIBLE);
                show_3d_model.setVisibility(View.GONE);
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


    }

    private void getUsersData() {
        ValueEventListener vListener = new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_user_data = dataSnapshot.getValue(UserData.class);
                assert current_user_data != null;
                setVisibleForObtainedCharacters();
                lvl.setText("Lv." + String.format("%d", current_user_data.getCharacters().get(0).getCharacter_lvl()));
                hp.setText(String.format("%d", current_user_data.getCharacters().get(0).getHp()));
                def.setText(String.format("%d", current_user_data.getCharacters().get(0).getDefense()));
                attack.setText(String.format("%d", current_user_data.getCharacters().get(0).getAttack()));
                crit_rate.setText(String.format("%d", current_user_data.getCharacters().get(0).getCrit_chance()));
                crit_dmg.setText(String.format("%d", current_user_data.getCharacters().get(0).getCrit_dmg()));
                character_name.setText(current_user_data.getCharacters().get(0).getName());
                exp.setMax(characters_exp_table[current_user_data.getCharacters().get(0).getCharacter_lvl() + 1] - characters_exp_table[current_user_data.getCharacters().get(0).getCharacter_lvl()]);
                exp.setProgress(current_user_data.getCharacters().get(0).getExp() - characters_exp_table[current_user_data.getCharacters().get(0).getCharacter_lvl()]);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        usersData.addValueEventListener(vListener);
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
            default:
                break;
        }
    }
}
