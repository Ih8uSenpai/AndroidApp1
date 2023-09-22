package com.example.androidapp1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.androidapp1.Models.InventoryItem;
import com.example.androidapp1.Models.User;
import com.example.androidapp1.Models.UserData;
import com.example.androidapp1.Models.pair;
import com.example.androidapp1.engine.Engine;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HomeScreen extends AppCompatActivity implements FragmentInteractionListener {

    private static final int CONES_SIZE = 3;
    private static final int ARTIFACTS_SIZE = 5;
    private static final int ITEMS_SIZE = 2;
    int[] exp_table = new int[]{100, 200, 320, 450, 600, 850, 1200, 1600, 2200, 3000,
            3751, 4212, 4912, 5921, 7123, 8234, 9653, 10923, 12402, 14329,
            16481, 18982, 21424, 24021, 27965, 32420, 36831, 41242, 47634, 55131,
            62521, 70123, 79123, 89123, 103211, 112381, 124181, 137123, 151271, 165247,
            181427, 196148, 216471, 238131, 260812, 284881, 310161, 342518, 381271, 459211,
            561412, 725219, 920001, 1201231, 1601231, 2301231, 3101231, 4101231, 5501231, 10501231, 17501231};
    int[] characters_exp_table = new int[]{100, 200, 320, 450, 600, 850, 1200, 1600, 2200, 3000,
            3751, 4212, 4912, 5921, 7123, 8234, 9653, 10923, 12402, 14329,
            16481, 18982, 21424, 24021, 27965, 32420, 36831, 41242, 47634, 55131,
            62521, 70123, 79123, 89123, 103211, 112381, 124181, 137123, 151271, 165247,
            181427, 196148, 216471, 238131, 260812, 284881, 310161, 342518, 381271, 459211,
            561412, 725219, 920001, 1201231, 1601231, 2301231, 3101231, 4101231, 5501231, 10501231, 17501231};
    static String[] loot_table_3star = new String[]{"Threads of Fate", "Echoes of the Forgotten", "Veil of Serenity", "Glimmer of Hope", "Whispers of Time", "Mists of Solitude", "Eclipsed Moon", "Shattered Illusions"};
    static String[] loot_table_4star = new String[]{"Codex of the Unfathomable", "Resonance of the Void", "Vortex of Forgotten Dreams", "Nexus of Cosmic Synchronicity", "Tempest of Calamity", "Phantasmal Crucible", "Odyssey of the Ancestral", "Aetherial Quasar"};
    static String[] loot_table_5star = new String[]{"Kafka", "Kiana", "Blade"};
    VideoView videoPlayer, play_button;
    static MediaPlayer ost1, ost2, ost3, ost4;


    //database
    FirebaseAuth auth;
    FirebaseDatabase db;
    static DatabaseReference usersData;
    DatabaseReference users;
    ConstraintLayout root;

    // userdata vars declaration
    TextView username;
    TextView level;
    TextView gold;
    TextView gems;
    ProgressBar levelProgressBar;
    AppCompatButton get_exp;
    AppCompatButton get_gold;
    AppCompatButton get_gems;
    ImageButton memories_btn;
    User current_user;
    static UserData current_user_data;
    ArrayAdapter adapter;
    ConstraintLayout home_screen;
    ConstraintLayout memories_screen;
    ImageButton memoriesToHome;

    TextView gems1, pulls, gems_inv, gold_inv;



    MediaPlayer mp;


    Engine engine;

    MaterialCardView profile_bar;
    ImageButton rating_button;
    ImageButton settings_button;
    ImageButton inventory_button;
    ImageButton characters_button;
    ConstraintLayout rating_screen;
    ConstraintLayout inventory_screen;
    ConstraintLayout characters_screen;
    ConstraintLayout settings_screen;
    AppCompatButton rating_to_home;
    AppCompatButton characters_to_home;
    ImageButton inventory_to_home;
    AppCompatButton settings_to_home;
    SeekBar volume_seek_bar;
    AudioManager audioManager;
    ConstraintLayout eventsLayout;
    ImageButton eventsToHome;
    AppCompatButton items_btn, cones_btn, artifacts_btn;




    int max_volume, cur_volume;

    Spinner language_spinner;

    GridView inventory_grid_cones, inventory_grid_artifacts, inventory_grid_items;
    List<InventoryItem> items_cone, items_artifact, items_item;
    ImageButton events_btn;
    private static ArrayList<Integer> coneIds, artifactIds, itemsIds;
    TextView inventory_section_name;

    ImageButton music_switch;

    ImageButton guild_button;

    FrameLayout fragmentContainer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        // initialisation
        init_all();
        // set all listeners
        setOnclickListeners();
        setOnPreparedListeners();
        setOnCompletionListeners();
        setOnChangeListeners();
        setOnItemSelectedListeners();
        // restart or start music and video
        restartMusic();
        // database read
        getUsers();
        getUsersData();

        //onCreateEnd
    }



    private void restartMusic() {
        if (videoPlayer == null) {
            // adding video
            videoPlayer = findViewById(R.id.home_screen_bg);
            Uri myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.home_background5);
            videoPlayer.setVideoURI(myVideoUri);
            // start+cycle video
            videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                }
            });
        }
        if (!videoPlayer.isPlaying())
            videoPlayer.start();


        // adding audio
        if (ost1 == null)
            playMusic();
        else
        if (!ost1.isPlaying()){
            ost1.seekTo(0);
            ost1.start();
        }
    }

    // read Users from db
    private void getUsers()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                current_user = dataSnapshot.getValue(User.class);
                assert current_user != null;
                String s = current_user.getName();
                SpannableString ss=new SpannableString(s);
                ss.setSpan(new UnderlineSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                username.setText(s);
                //username.setText(ss);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        users.addValueEventListener(vListener);
    }
    // read UsersData data from db
    private void getUsersData()
    {
        ValueEventListener vListener = new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //User user = dataSnapshot.getValue(User.class);
                //assert user != null;
                //username.setText(user.getName());
                current_user_data = UserData.getInstance();
                current_user_data = dataSnapshot.getValue(UserData.class);
                assert current_user_data != null;
                level.setText(String.format("%d", current_user_data.getLvl()));
                gems.setText(String.format("%d", current_user_data.getGems()));
                gold.setText(String.format("%d", current_user_data.getGold()));
                gold_inv.setText(String.format("%d", current_user_data.getGold()));
                gems_inv.setText(String.format("%d", current_user_data.getGems()));
                levelProgressBar.setMax(exp_table[current_user_data.getLvl() + 1] - exp_table[current_user_data.getLvl()]);
                levelProgressBar.setProgress(current_user_data.getExp() - exp_table[current_user_data.getLvl()]);
                items_cone = parseItemsFromDB_cones();
                items_artifact = parseItemsFromDB_artifacts();
                items_item = parseItemsFromDB_items();
                inventory_grid_cones.setAdapter(new InventoryAdapter(HomeScreen.this, items_cone));
                inventory_grid_artifacts.setAdapter(new InventoryAdapter(HomeScreen.this, items_artifact));
                inventory_grid_items.setAdapter(new InventoryAdapter(HomeScreen.this, items_item));

                /*for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    User user = ds.getValue(User.class);
                    assert user != null;
                    listData.add(user.getName());
                    listTemp.add(user);
                }*/
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        usersData.addValueEventListener(vListener);
    }

    public void playMusic() {
        ost1 = MediaPlayer.create(this, R.raw.home_screen_ost1);
        ost1.setLooping(true);
        ost1.seekTo(0);
        ost1.start();
    }

    public void openMemories(){
        memories_screen.setVisibility(View.VISIBLE);
        home_screen.setVisibility(View.GONE);
    }

    private void releaseResources() {
        ost1.stop();
        ost1.setLooping(false);
    }
    public void init_all(){

        home_screen = findViewById(R.id.home_screen);
        memories_screen = findViewById(R.id.memories_screen);
        gems_inv = findViewById(R.id.gems_inv);
        gold_inv = findViewById(R.id.gold_inv);
        eventsLayout = findViewById(R.id.events_window);
        eventsToHome = findViewById(R.id.eventsToHome);
        //Uri myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_3star);
        //pull_animation_1.setVideoURI(myVideoUri);
        //pull_animation_1.seekTo(0);

        profile_bar = findViewById(R.id.profile_bar);
        rating_button = findViewById(R.id.rating_button);
        settings_button = findViewById(R.id.settings_button);
        inventory_button = findViewById(R.id.inventory_button);
        characters_button = findViewById(R.id.characters_button);
        rating_screen = findViewById(R.id.rating_screen);
        inventory_screen = findViewById(R.id.inventory_screen);
        settings_screen = findViewById(R.id.settings_screen);
        rating_to_home = findViewById(R.id.rating_to_home);
        inventory_to_home = findViewById(R.id.inventory_to_home);
        settings_to_home = findViewById(R.id.settings_to_home);
        events_btn = findViewById(R.id.events_button);




        // set inventory layout
        coneIds = new ArrayList<>();
        artifactIds = new ArrayList<>();
        itemsIds = new ArrayList<>();
        for (int i = 0; i < CONES_SIZE; i++) {
            String objectPath = "raw/" + "cone" + (i + 1);
            int resourceId = this.getResources().getIdentifier(objectPath, null, this.getPackageName());
            coneIds.add(resourceId);
        }
        for (int i = 0; i < ARTIFACTS_SIZE; i++) {
            String objectPath = "raw/" + "cone" + (i + 1);
            int resourceId = this.getResources().getIdentifier(objectPath, null, this.getPackageName());
            coneIds.add(resourceId);
        }
        for (int i = 0; i < ITEMS_SIZE; i++) {
            String objectPath = "raw/" + "cone" + (i + 1);
            int resourceId = this.getResources().getIdentifier(objectPath, null, this.getPackageName());
            coneIds.add(resourceId);
        }
        inventory_section_name = findViewById(R.id.inventory_section_name);
        inventory_section_name.setText("Cones");
        cones_btn = findViewById(R.id.cones_btn);
        artifacts_btn = findViewById(R.id.artifacts_btn);
        items_btn = findViewById(R.id.items_btn);
        /*
        items_cone = parseItemsFromDB_cones();
        items_artifact = parseItemsFromDB_artifacts();
        items_item = parseItemsFromDB_items();
         */
        inventory_grid_cones = findViewById(R.id.inventory_grid_cones);
        inventory_grid_artifacts = findViewById(R.id.inventory_grid_artifacts);
        inventory_grid_items = findViewById(R.id.inventory_grid_items);
        /*
        inventory_grid_cones.setAdapter(new InventoryAdapter(this, items_cone));
        inventory_grid_artifacts.setAdapter(new InventoryAdapter(this, items_artifact));
        inventory_grid_items.setAdapter(new InventoryAdapter(this, items_item));
         */
        // settings screen vars
        volume_seek_bar = findViewById(R.id.volume_seek_bar);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        max_volume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        cur_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume_seek_bar.setMax(max_volume);
        volume_seek_bar.setProgress(cur_volume);
        language_spinner = findViewById(R.id.language_spinner);

        // database interaction variables
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        usersData = FirebaseDatabase.getInstance().getReference("UsersData").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        users = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        adapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item);


        // set userData fields
        level = findViewById(R.id.lvl);
        username = findViewById(R.id.username);
        levelProgressBar = findViewById(R.id.progress);
        gold = findViewById(R.id.gold);
        gems = findViewById(R.id.gems);
        get_exp = findViewById(R.id.gain_exp);
        get_gold = findViewById(R.id.gain_gold);
        get_gems = findViewById(R.id.gain_gems);
        memories_btn = findViewById(R.id.memories);



        ArrayList<Integer> coneIds;



        music_switch = findViewById(R.id.music_button);
        ost2 = MediaPlayer.create(HomeScreen.this, R.raw.ost2);
        ost2.setLooping(true);
        ost3 = MediaPlayer.create(HomeScreen.this, R.raw.ost3);
        ost3.setLooping(true);
        ost4 = MediaPlayer.create(HomeScreen.this, R.raw.ost4);
        ost4.setLooping(true);

        guild_button = findViewById(R.id.guild_button);
        fragmentContainer = findViewById(R.id.fragment_container);

    }


    public static List<InventoryItem> parseItemsFromFile_cones(Context context, int resourceId) {
        List<InventoryItem> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceId)))) {
            String nameLine;
            String descriptionLine;
            while ((nameLine = br.readLine()) != null && (descriptionLine = br.readLine()) != null) {
                String name = nameLine.substring(nameLine.indexOf(':') + 1).trim();
                String description = descriptionLine.substring(descriptionLine.indexOf(':') + 1).trim();
                items.add(new InventoryItem(R.drawable.cone1, name, description));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }
    public static List<InventoryItem> parseItemsFromFile_artifacts(Context context, int resourceId) {
        List<InventoryItem> items = new ArrayList<>();
        int i = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceId)))) {
            String nameLine;
            String descriptionLine;
            while ((nameLine = br.readLine()) != null && (descriptionLine = br.readLine()) != null) {
                String name = nameLine.substring(nameLine.indexOf(':') + 1).trim();
                String description = descriptionLine.substring(descriptionLine.indexOf(':') + 1).trim();
                items.add(new InventoryItem(R.drawable.cone2, name, description));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }
    public static List<InventoryItem> parseItemsFromFile_items(Context context, int resourceId) {
        List<InventoryItem> items = new ArrayList<>();
        int i = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceId)))) {
            String nameLine;
            String descriptionLine;
            while ((nameLine = br.readLine()) != null && (descriptionLine = br.readLine()) != null) {
                String name = nameLine.substring(nameLine.indexOf(':') + 1).trim();
                String description = descriptionLine.substring(descriptionLine.indexOf(':') + 1).trim();
                items.add(new InventoryItem(R.drawable.cone3, name, description));
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    public List<InventoryItem> parseItemsFromDB_cones() {
        List<InventoryItem> obtained_items = new ArrayList<>();
        List<InventoryItem> all_items = current_user_data.getCones();
        for (int i = 0; i < all_items.size(); i++){
            if (all_items.get(i).getIs_obtained())
                obtained_items.add(new InventoryItem(R.drawable.cone1, all_items.get(i).getName(), all_items.get(i).getDescription(), "function" + i));
                //obtained_items.add(all_items.get(i));
        }
        return obtained_items;
    }
    public List<InventoryItem> parseItemsFromDB_artifacts() {
        List<InventoryItem> obtained_items = new ArrayList<>();
        List<InventoryItem> all_items = current_user_data.getArtifacts();
        for (int i = 0; i < all_items.size(); i++){
            if (all_items.get(i).getIs_obtained())
                obtained_items.add(all_items.get(i));
        }
        return obtained_items;
    }
    public List<InventoryItem> parseItemsFromDB_items() {
        List<InventoryItem> obtained_items = new ArrayList<>();
        List<InventoryItem> all_items = current_user_data.getItems();
        for (int i = 0; i < all_items.size(); i++){
            if (all_items.get(i).getIs_obtained())
                obtained_items.add(all_items.get(i));
        }
        return obtained_items;
    }

    private void setOnclickListeners(){


        events_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsLayout.setVisibility(View.VISIBLE);
            }
        });
        eventsToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsLayout.setVisibility(View.GONE);
            }
        });


        get_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_user_data.gainExp(200, exp_table, usersData);
            }
        });
        get_gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_user_data.gainGold(100, usersData);
            }
        });
        get_gems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_user_data.gainGems(2000, usersData);
            }
        });



        memories_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace_fragment();


                //openMemories();
            }
        });
        /*pull_animation_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pull_animation_1.seekTo(pull_animation_1.getDuration());
            }
        });*/
        profile_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Profile_screen.class));
                releaseResources();
                finish();
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings_screen.setVisibility(View.VISIBLE);
                //home_screen.setVisibility(View.GONE);
            }
        });
        settings_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //home_screen.setVisibility(View.VISIBLE);
                settings_screen.setVisibility(View.GONE);
            }
        });
        rating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating_screen.setVisibility(View.VISIBLE);
                home_screen.setVisibility(View.GONE);
            }
        });
        rating_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_screen.setVisibility(View.VISIBLE);
                rating_screen.setVisibility(View.GONE);
            }
        });

        cones_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventory_section_name.setText("Cones");
                inventory_grid_cones.setVisibility(View.VISIBLE);
                inventory_grid_artifacts.setVisibility(View.GONE);
                inventory_grid_items.setVisibility(View.GONE);
            }
        });
        artifacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventory_section_name.setText("Artifacts");
                inventory_grid_artifacts.setVisibility(View.VISIBLE);
                inventory_grid_cones.setVisibility(View.GONE);
                inventory_grid_items.setVisibility(View.GONE);
            }
        });
        items_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventory_section_name.setText("Items");
                inventory_grid_items.setVisibility(View.VISIBLE);
                inventory_grid_cones.setVisibility(View.GONE);
                inventory_grid_artifacts.setVisibility(View.GONE);
            }
        });
        inventory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventory_screen.setVisibility(View.VISIBLE);
                home_screen.setVisibility(View.GONE);
            }
        });
        inventory_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_screen.setVisibility(View.VISIBLE);
                inventory_screen.setVisibility(View.GONE);
            }
        });


        characters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, Characters.class));
                releaseResources();
                finish();
                //startActivity(new Intent(MainActivity.this, ReadActivity.class));
            }
        });

        music_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ost1.isPlaying())
                {
                    ost1.pause();
                    ost2.seekTo(0);
                    ost2.start();
                    //music_switch.setText("HOYO-MiX — Moon Halo");
                }
                else if (ost2.isPlaying()){
                    ost2.pause();
                    ost3.seekTo(0);
                    ost3.start();
                    //music_switch.setText("Aimer — escalate");
                }
                else if (ost3.isPlaying()){
                    ost3.pause();
                    ost4.seekTo(0);
                    ost4.start();
                    //music_switch.setText("Tokio Hotel - Monsoon");
                }
                else if (ost4.isPlaying()){
                    ost4.pause();
                    ost1.seekTo(0);
                    ost1.start();
                    //music_switch.setText("HOYO-MiX — Hidden Dreams in the Depths");
                }

            }
        });


        guild_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //replace_fragment();

            }
        });


    }

    private void replace_fragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        fragmentTransaction.replace(R.id.fragment_container, new Gacha_screen());
        /*if (currentFragment == null) {
            fragmentTransaction.replace(R.id.fragment_container, new Gacha_screen());
        } else if (currentFragment instanceof Gacha_screen) {
            fragmentTransaction.replace(R.id.fragment_container, new fragment2());
            
        }
        else
            fragmentTransaction.replace(R.id.fragment_container, new Gacha_screen());
         */
        fragmentTransaction.commit();
        home_screen.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
    }

    public void setOnCompletionListeners(){


    }

    public void setOnPreparedListeners(){

    }

    public void setOnChangeListeners(){
        volume_seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setOnItemSelectedListeners(){
        // set parameters for language spinner
        language_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(HomeScreen.this, "Selected item: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("English");
        arrayList.add("Русский");
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(this, R.layout.color_spinner_layout, arrayList);
        adapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        language_spinner.setAdapter(adapter1);


    }






    @Override
    public void onFragmentClosed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
// Затем удалите фрагмент
        transaction.remove(new Gacha_screen());
        transaction.commit();
        fragmentContainer.setVisibility(View.GONE);
        home_screen.setVisibility(View.VISIBLE);
    }

}
