package com.example.androidapp1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.androidapp1.Models.User;
import com.example.androidapp1.Models.UserData;
import com.example.androidapp1.Models.pair;
import com.example.androidapp1.engine.Engine;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

public class HomeScreen extends AppCompatActivity {

    int[] exp_table = new int[]{100, 200, 320, 450, 600, 850, 1200, 1600, 2200, 3000,
            3751, 4212, 4912, 5921, 7123, 8234, 9653, 10923, 12402, 14329,
            16481, 18982, 21424, 24021, 27965, 32420, 36831, 41242, 47634, 55131,
            62521, 70123, 79123, 89123, 103211, 112381, 124181, 137123, 151271, 165247,
            181427, 196148, 216471, 238131, 260812, 284881, 310161, 342518, 381271, 459211,
            561412, 725219, 920001, 1201231, 1601231, 2301231, 3101231, 4101231, 5501231, 10501231, 17501231};
    String[] loot_table_3star = new String[]{"3 star weapon1", "3 star weapon2", "3 star weapon3", "3 star weapon4", "3 star weapon5", "3 star weapon6", "3 star weapon7", "3 star weapon8"};
    String[] loot_table_4star = new String[]{"4 star weapon1", "4 star weapon2", "4 star weapon3", "4 star weapon4", "4 star weapon5", "4 star weapon6", "4 star weapon7", "4 star weapon8"};
    String[] loot_table_5star = new String[]{"5 star1", "5 star2", "5 star3"};
    VideoView videoPlayer;
    MediaPlayer ost1;
    MediaPlayer pull_ost;


    //database
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference usersData;
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
    UserData current_user_data;
    ArrayAdapter adapter;
    ConstraintLayout home_screen;
    ConstraintLayout memories_screen;
    AppCompatButton backHome;
    AppCompatButton pull;
    AppCompatButton pull_10;
    TextView gems1, pulls;
    VideoView pull_animation_1;
    VideoView loot_animation;
    ConstraintLayout pull_animation_screen;
    Dialog dialog;
    View pull_window;
    ConstraintLayout pull_layout;
    TextView item_name_field;
    ShapeableImageView item_picture_field;
    SurfaceHolder sh;
    SurfaceHolder sh1;

    MediaPlayer mp;

    VideoView loot_wait_animation;
    VideoView loot_spawn_animation;
    MediaPlayer pull_animation;
    SurfaceView pull_surface;
    SurfaceView star_sky;
    Engine engine;

    MaterialCardView profile_bar;
    AppCompatButton profile_to_home;
    ConstraintLayout profile_screen;
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
    AppCompatButton inventory_to_home;
    AppCompatButton settings_to_home;
    SeekBar volume_seek_bar;
    AudioManager audioManager;

    int max_volume, cur_volume;

    Spinner language_spinner;





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
            Uri myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.home_page_bg1);
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
                current_user_data = dataSnapshot.getValue(UserData.class);
                assert current_user_data != null;
                level.setText(String.format("%d", current_user_data.getLvl()));
                gems.setText(String.format("%d", current_user_data.getGems()));
                gold.setText(String.format("%d", current_user_data.getGold()));
                pulls.setText(String.format("%d", current_user_data.getPulls()));
                gems1.setText(String.format("%d", current_user_data.getGems()));
                levelProgressBar.setMax(exp_table[current_user_data.getLvl() + 1] - exp_table[current_user_data.getLvl()]);
                levelProgressBar.setProgress(current_user_data.getExp() - exp_table[current_user_data.getLvl()]);
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
        /*startActivity(new Intent(HomeScreen.this, MemoriesScreen.class).putExtra("gems", current_user_data.getGems()));
        //startActivity(new Intent(MainActivity.this, ReadActivity.class));
        releaseResources();
        finish();*/
        memories_screen.setVisibility(View.VISIBLE);
        home_screen.setVisibility(View.GONE);
    }

    private void releaseResources() {
        ost1.stop();
    }
    public void init_all(){

        home_screen = findViewById(R.id.home_screen);
        memories_screen = findViewById(R.id.memories_screen);
        backHome = findViewById(R.id.memoriesToHome);
        pull = findViewById(R.id.pull);
        pull_10 = findViewById(R.id.pull_10);
        gems1 = findViewById(R.id.gems1);
        pulls = findViewById(R.id.pulls);
        pull_animation_screen = findViewById(R.id.pull_animation_screen);
        pull_animation_1 = findViewById(R.id.pull_animation1);
        //Uri myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_3star);
        //pull_animation_1.setVideoURI(myVideoUri);
        //pull_animation_1.seekTo(0);
        pull_ost = MediaPlayer.create(this, R.raw.pull_ost);
        profile_bar = findViewById(R.id.profile_bar);
        profile_to_home = findViewById(R.id.profile_to_home);
        profile_screen = findViewById(R.id.profile_screen);
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

        pull_layout = findViewById(R.id.pull_window);
        dialog = new Dialog(HomeScreen.this, R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(this);
        pull_window = inflater.inflate(R.layout.pull_window, null);
        dialog.setContentView(pull_window);
        item_name_field = (TextView) dialog.findViewById(R.id.item_name_field);
        item_picture_field = (ShapeableImageView) dialog.findViewById(R.id.item_picture_field);
        item_picture_field.setImageResource(R.drawable.card1);
        star_sky = (SurfaceView) dialog.findViewById(R.id.star_sky);
        pull_animation = new MediaPlayer();

        SurfaceHolder.Callback callback1 = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                pull_animation.setDisplay(sh1);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }


            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        };
        pull_surface = findViewById(R.id.pull_surface);
        pull_surface.getHolder().addCallback(callback1);

    }


    private void setOnclickListeners(){
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_screen.setVisibility(View.VISIBLE);
                memories_screen.setVisibility(View.GONE);
            }
        });
        pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_user_data.getPulls() > 0){
                    current_user_data.gainPulls(-1, usersData);
                    ArrayList<pair> loot_list = new ArrayList<>();
                    loot_list.add(makeSinglePull());
                    showPullAnimation_10(loot_list.get(0).first, loot_list);
                } else if (current_user_data.getGems() >= 150) {
                    current_user_data.gainGems(-150, usersData);
                    ArrayList<pair> loot_list = new ArrayList<>();
                    loot_list.add(makeSinglePull());
                    showPullAnimation_10(loot_list.get(0).first, loot_list);
                }
            }
        });

        pull_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_user_data.getPulls() >= 10){
                    current_user_data.gainPulls(-10, usersData);
                    make10Pull();
                } else if (current_user_data.getGems() >= 1500) {
                    current_user_data.gainGems(-1500, usersData);
                    make10Pull();
                }
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
                openMemories();
            }
        });
        pull_animation_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pull_animation_1.seekTo(pull_animation_1.getDuration());
            }
        });
        profile_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_screen.setVisibility(View.VISIBLE);
                home_screen.setVisibility(View.GONE);
            }
        });
        profile_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_screen.setVisibility(View.VISIBLE);
                profile_screen.setVisibility(View.GONE);
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
                finish();
                //startActivity(new Intent(MainActivity.this, ReadActivity.class));
            }
        });



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

    private void make10Pull() {
        ArrayList<pair> loot_list = new ArrayList<>();
        // fill 10 slots
        for (int i = 0; i < 10; i++) {
            loot_list.add(makeSinglePull());
        }
        // determine the rarity of the best loot
        Optional<pair> rarity = loot_list.stream().max(Comparator.comparingInt(pair -> pair.first));
        showPullAnimation_10(rarity.get().first, loot_list);
    }

    private pair makeSinglePull() {
        Random random = new Random();
        int rarity_value = random.nextInt(120);
        int loot_value;
        String loot;

        current_user_data.setPulls_to_4star(current_user_data.getPulls_to_4star() + 1);
        current_user_data.setPulls_to_5star(current_user_data.getPulls_to_5star() + 1);

        // guarantee pulls
        if (current_user_data.getPulls_to_5star() == 50){
            loot_value = random.nextInt(loot_table_5star.length);
            current_user_data.setLoot_table(current_user_data.generateLootTable(current_user_data.getLoot_table()));
            loot = loot_table_5star[loot_value];
            current_user_data.updatePulls_to_5star(0, usersData);
            ost1.pause();
            return new pair(5, loot_value);
        }
        if (current_user_data.getPulls_to_4star() == 10){
            loot_value = random.nextInt(loot_table_4star.length);
            loot = loot_table_4star[loot_value];
            current_user_data.updatePulls_to_4star(0, usersData);
            ost1.pause();
            return new pair(4, loot_value);
        }


        if (current_user_data.getLoot_table() == null)
            current_user_data.setLoot_table(current_user_data.generateLootTable(current_user_data.getLoot_table()));

        // normal pulls
        while (current_user_data.getLoot_table().get(rarity_value).second != 0)
            rarity_value = random.nextInt(120);

        if (current_user_data.getLoot_table().get(rarity_value).first == 3) {
            loot_value = random.nextInt(loot_table_3star.length);
            loot = loot_table_3star[loot_value];
            current_user_data.updatePulls_to_4star(current_user_data.getPulls_to_4star(), usersData);
            current_user_data.updatePulls_to_5star(current_user_data.getPulls_to_5star(), usersData);
            current_user_data.update_loot_table(rarity_value, usersData);
        } else if (current_user_data.getLoot_table().get(rarity_value).first == 4) {
            loot_value = random.nextInt(loot_table_4star.length);
            loot = loot_table_4star[loot_value];
            current_user_data.updatePulls_to_4star(0, usersData);
            current_user_data.update_loot_table(rarity_value, usersData);
        } else {
            loot_value = random.nextInt(loot_table_5star.length);
            current_user_data.updatePulls_to_5star(0, usersData);
            current_user_data.setLoot_table(current_user_data.generateLootTable(current_user_data.getLoot_table()));
            loot = loot_table_5star[loot_value];
        }

        if (ost1.isPlaying())
            ost1.pause();
        return new pair(current_user_data.getLoot_table().get(rarity_value).first, loot_value);
    }
    public void showPullAnimation1(int rarity, int value) {
        Uri myVideoUri;
        switch (rarity){
            case 3:
                myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_3star);
                pull_animation_1.setVideoURI(myVideoUri);
                break;
            case 4:
                myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_4star);
                pull_animation_1.setVideoURI(myVideoUri);
                break;
            case 5:
                myVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_5star);
                pull_animation_1.setVideoURI(myVideoUri);
                break;
            default:
                break;
        }
        pull_animation_1.setVisibility(View.VISIBLE);
        pull_animation_screen.setVisibility(View.VISIBLE);
        memories_screen.setVisibility(View.GONE);
        pull_animation_1.start();
        pull_animation_1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                memories_screen.setVisibility(View.VISIBLE);
                pull_animation_screen.setVisibility(View.GONE);
                pull_animation_1.setVisibility(View.GONE);
                if (!pull_ost.isPlaying())
                    pull_ost.start();
                showLootAnimation(rarity, value);
            }
        });

    }

    private void showLootAnimation(int rarity, int value) {
        switch (rarity){
            case 3:
                item_name_field.setText(loot_table_3star[value]);
                break;
            case 4:
                item_name_field.setText(loot_table_4star[value]);
                break;
            case 5:
                item_name_field.setText(loot_table_5star[value]);
                break;
            default:
                break;
        }
        dialog.show();
        pull_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pull_ost.pause();
                pull_ost.seekTo(0);
                dialog.dismiss();
                if(!ost1.isPlaying())
                    ost1.start();
            }
        });
    }

    private void showLootAnimation_10(ArrayList<pair> loot_list, int i) {
        //set default params

        item_name_field.setVisibility(View.GONE);

        item_picture_field.setRotation(0);
        item_picture_field.setAlpha((float) 0);
        item_picture_field.setRotationY(0);
        item_picture_field.setTranslationY(0);
        item_picture_field.setTranslationX(0);

        item_name_field.setTranslationX(0);
        item_name_field.setAlpha((float) 0);

        if (engine == null || engine.isStopped())
            engine = new Engine(star_sky);
        // animate image
        //item_picture_field.animate().setDuration(1000).translationY(750).rotationYBy(355f).rotation(10).alpha(1);
        ViewPropertyAnimator animation = item_picture_field.animate().setDuration(1000).translationY(750).rotationYBy(355f).rotation(10).alpha(1);
        animation.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                item_name_field.animate().cancel();
                item_name_field.setTranslationX(0);
                item_name_field.setAlpha((float) 0);
                item_name_field.setVisibility(View.VISIBLE);
                item_name_field.animate().setDuration(2000).translationX(-450).alpha(1);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
                item_name_field.animate().cancel();
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });




/*
        // wait animation
        if (!loot_wait_animation.isPlaying())
            loot_wait_animation.start();*/

        switch (loot_list.get(i).first) {
            case 3:
                item_name_field.setText(loot_table_3star[loot_list.get(i).second]);
                break;
            case 4:
                item_name_field.setText(loot_table_4star[loot_list.get(i).second]);
                break;
            case 5:
                item_name_field.setText(loot_table_5star[loot_list.get(i).second]);
                break;
            default:
                break;
        }


        // show animation
        dialog.show();



        // switch to next item animation
        pull_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                if (i != loot_list.size() - 1) {
                    animation.cancel();
                    showLootAnimation_10(loot_list, i + 1);
                }
                else {
                    pull_ost.pause();
                    pull_ost.seekTo(0);
                    dialog.dismiss();
                    engine.stop();
                    if(!ost1.isPlaying())
                        ost1.start();
                }
            }
        });

    }

    public void showPullAnimation_10(int max_rarity, ArrayList<pair> loot_list) {
        pull_animation = new MediaPlayer();
        try {
            switch (max_rarity) {
                case 3:
                    pull_animation.setDataSource(HomeScreen.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_3star));
                    break;
                case 4:
                    pull_animation.setDataSource(HomeScreen.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_4star));
                    break;
                case 5:
                    pull_animation.setDataSource(HomeScreen.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_5star));
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            pull_animation.reset();
            try {
                switch (max_rarity) {
                    case 3:
                        pull_animation.setDataSource(HomeScreen.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_3star));
                        break;
                    case 4:
                        pull_animation.setDataSource(HomeScreen.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_4star));
                        break;
                    case 5:
                        pull_animation.setDataSource(HomeScreen.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pull_5star));
                        break;
                    default:
                        break;
                }
            }
                catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
        }
        try {
            pull_animation.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pull_animation.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                pull_surface.setVisibility(View.VISIBLE);
                sh1 = pull_surface.getHolder();
                pull_animation.start();
            }
        });
        //pull_animation_1.setVisibility(View.VISIBLE);
        pull_animation_screen.setVisibility(View.VISIBLE);
        memories_screen.setVisibility(View.GONE);
        //pull_animation_1.start();
        pull_animation.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                memories_screen.setVisibility(View.VISIBLE);
                pull_animation_screen.setVisibility(View.GONE);
                pull_surface.setVisibility(View.GONE);
                pull_animation.reset();
                //if (!pull_ost.isPlaying())
                    //pull_ost.start();
                showLootAnimation_10(loot_list, 0);
            }
        });

    }


    }
