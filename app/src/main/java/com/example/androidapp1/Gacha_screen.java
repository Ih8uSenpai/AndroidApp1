package com.example.androidapp1;

import static com.example.androidapp1.HomeScreen.current_user_data;
import static com.example.androidapp1.HomeScreen.loot_table_3star;
import static com.example.androidapp1.HomeScreen.loot_table_4star;
import static com.example.androidapp1.HomeScreen.loot_table_5star;
import static com.example.androidapp1.HomeScreen.ost1;
import static com.example.androidapp1.HomeScreen.usersData;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.androidapp1.Models.UserData;
import com.example.androidapp1.Models.pair;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class Gacha_screen extends Fragment {

    View view;
    AppCompatButton event_banner, standart_banner;
    AppCompatButton pull, pull_10;
    TextView gems1, gold;

    ImageView banner_background;
    ImageButton char_banner, cones_banner, toHome;
    boolean isEventBanner, isStandartBanner, isCharacterBanner, isConesBanner;
    ConstraintLayout pull_animation_screen;
    VideoView pull_animation_1;
    MediaPlayer pull_ost;
    VideoView loot_animation;

    Dialog dialog;
    View pull_window;
    ConstraintLayout pull_layout;
    TextView item_name_field;
    ShapeableImageView item_picture_field;
    SurfaceHolder sh;
    SurfaceHolder sh1;
    SurfaceHolder sh2;

    VideoView loot_wait_animation;
    VideoView loot_spawn_animation;
    MediaPlayer pull_animation;
    MediaPlayer appear_animation;
    SurfaceView pull_surface;
    SurfaceView appear_surface;
    SurfaceView star_sky;
    VideoView clip_5star;
    boolean clip_is_playing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        init();
        setOnClickListeners();

        return view;
    }

    public void init(){
        event_banner = view.findViewById(R.id.event_banner_button);
        char_banner = view.findViewById(R.id.banner_characters);
        cones_banner = view.findViewById(R.id.banner_cones);
        event_banner = view.findViewById(R.id.event_banner_button);
        standart_banner = view.findViewById(R.id.standart_banner_button);
        banner_background = view.findViewById(R.id.banner_background);
        toHome = view.findViewById(R.id.memoriesToHome);
        isEventBanner = true;
        isStandartBanner = false;
        isCharacterBanner = true;
        isConesBanner = false;
        pull = view.findViewById(R.id.pull);
        pull_10 = view.findViewById(R.id.pull_10);
        gems1 = view.findViewById(R.id.gems1);
        pull_animation_screen = view.findViewById(R.id.pull_animation_screen);
        pull_animation_1 = view.findViewById(R.id.pull_animation1);
        pull_ost = MediaPlayer.create(getContext(), R.raw.pull_ost);
        pull_layout = view.findViewById(R.id.pull_window);
        dialog = new Dialog(getContext(), R.style.myFullscreenAlertDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        pull_window = inflater.inflate(R.layout.pull_window, null);
        dialog.setContentView(pull_window);
        item_name_field = (TextView) dialog.findViewById(R.id.item_name_field);
        item_picture_field = (ShapeableImageView) dialog.findViewById(R.id.item_picture_field);
        item_picture_field.setImageResource(R.drawable.card1);
        star_sky = (SurfaceView) dialog.findViewById(R.id.star_sky);
        pull_animation = new MediaPlayer();
        appear_animation = new MediaPlayer();
        
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

        SurfaceHolder.Callback callback2 = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                appear_animation.setDisplay(sh2);
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }


            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        };
        pull_surface = view.findViewById(R.id.pull_surface);
        pull_surface.getHolder().addCallback(callback1);


        appear_surface = (SurfaceView) dialog.findViewById(R.id.appear_animation);
        appear_surface.getHolder().addCallback(callback2);


        // gacha wait animation video
        loot_wait_animation = (VideoView) dialog.findViewById(R.id.wait_animation);
        loot_wait_animation.setVideoURI(Uri.parse( "android.resource://" + requireContext().getPackageName() + "/" + R.raw.loot_wait_animation));
        clip_5star = (VideoView) dialog.findViewById(R.id.clip_5star);

        gold = view.findViewById(R.id.gold_gacha);
        gold.setText(String.format("%d", current_user_data.getGold()));
        gems1.setText(String.format("%d", current_user_data.getGems()));
    }

    public void setOnClickListeners(){
        standart_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStandartBanner = true;
                isEventBanner = false;
                change_banner();
            }
        });
        event_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEventBanner = true;
                isStandartBanner = false;
                change_banner();
            }
        });
        char_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCharacterBanner = true;
                isConesBanner = false;
                change_banner();
            }
        });
        cones_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConesBanner = true;
                isCharacterBanner = false;
                change_banner();
            }
        });
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof FragmentInteractionListener) {
                    ((FragmentInteractionListener) getActivity()).onFragmentClosed();
                }
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
                //pull_10.setBackgroundResource(R.drawable.card1);
            }
        });
        
    }




    public void change_banner(){
        if (isCharacterBanner){
            if (isEventBanner) {
                banner_background.setImageResource(R.drawable.event_character_banner);
            } else if (isStandartBanner) {
                banner_background.setImageResource(R.drawable.standart_character_banner);
            }
        }
        else if (isConesBanner){
            if (isEventBanner) {
                banner_background.setImageResource(R.drawable.event_weapon_banner);
            } else if (isStandartBanner) {
                banner_background.setImageResource(R.drawable.standart_weapon_banner);
            }
        }

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
        if (current_user_data.getPulls_to_5star() >= 50){
            loot_value = random.nextInt(loot_table_5star.length);
            current_user_data.setLoot_table(current_user_data.generateLootTable(current_user_data.getLoot_table()));
            loot = loot_table_5star[loot_value];
            current_user_data.updatePulls_to_5star(0, usersData);
            ost1.pause();
            //current_user_data.getCones().get(loot_value).setIs_obtained(true, String.valueOf(loot_value), usersData);

            return new pair(5, loot_value);
        }
        else if (current_user_data.getPulls_to_4star() >= 10){
            loot_value = random.nextInt(loot_table_4star.length);
            loot = loot_table_4star[loot_value];
            current_user_data.updatePulls_to_4star(0, usersData);
            ost1.pause();
            current_user_data.getCones().get(loot_value + 8).setIs_obtained(true, String.valueOf(loot_value + 8), usersData);
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


            current_user_data.getCones().get(loot_value).setIs_obtained(true, String.valueOf(loot_value), usersData);
        } else if (current_user_data.getLoot_table().get(rarity_value).first == 4) {
            loot_value = random.nextInt(loot_table_4star.length);
            loot = loot_table_4star[loot_value];
            current_user_data.updatePulls_to_4star(0, usersData);
            current_user_data.update_loot_table(rarity_value, usersData);


            current_user_data.getCones().get(loot_value + 8).setIs_obtained(true, String.valueOf(loot_value + 8), usersData);
        } else {
            loot_value = random.nextInt(loot_table_5star.length);
            current_user_data.updatePulls_to_5star(0, usersData);
            current_user_data.setLoot_table(current_user_data.generateLootTable(current_user_data.getLoot_table()));


            //current_user_data.getCones().get(loot_value).setIs_obtained(true, String.valueOf(loot_value), usersData);
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
                myVideoUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.pull_3star);
                pull_animation_1.setVideoURI(myVideoUri);
                break;
            case 4:
                myVideoUri = Uri.parse("android.resource://" + requireContext().getPackageName()  + "/" + R.raw.pull_4star);
                pull_animation_1.setVideoURI(myVideoUri);
                break;
            case 5:
                myVideoUri = Uri.parse("android.resource://" + requireContext().getPackageName()  + "/" + R.raw.pull_5star);
                pull_animation_1.setVideoURI(myVideoUri);
                break;
            default:
                break;
        }
        pull_animation_1.setVisibility(View.VISIBLE);
        pull_animation_screen.setVisibility(View.VISIBLE);
        //memories_screen.setVisibility(View.GONE);
        pull_animation_1.start();
        pull_animation_1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //memories_screen.setVisibility(View.VISIBLE);
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

    private void showLootAnimation_10(ArrayList<pair> loot_list, int i) throws InterruptedException {

        //set default params

        item_name_field.setVisibility(View.GONE);

        item_picture_field.setAlpha((float) 0);

        item_name_field.setTranslationX(0);
        item_name_field.setAlpha((float) 0);

        Thread thread_for_5star = new Thread(new Runnable() {
            @Override
            public void run() {
                if (loot_list.get(i).first == 5) {
                    switch (loot_table_5star[loot_list.get(i).second]) {
                        case "Kafka":
                            clip_5star.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.kafka_clip));
                            break;
                        case "Kiana":
                            clip_5star.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.kiana_clip));
                            break;
                        case "Blade":
                            clip_5star.setVideoURI(Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.blade_clip));
                            break;
                        default:
                            break;
                    }
                    clip_is_playing = true;
                    clip_5star.setVisibility(View.VISIBLE);
                    clip_5star.start();
                    clip_5star.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            clip_5star.setVisibility(View.GONE);
                            clip_is_playing = false;
                        }
                    });
                }

            }
        });
        thread_for_5star.start();
        thread_for_5star.join();


        // start star sky background
        /*if (engine == null || engine.isStopped())
            engine = new Engine(star_sky);*/


        // animate image
        //item_picture_field.animate().setDuration(1000).translationY(750).rotationYBy(355f).rotation(10).alpha(1);
        ViewPropertyAnimator animation = item_picture_field.animate().setDuration(1000).alpha(1);
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





        // wait animation
        if (!loot_wait_animation.isPlaying())
            // cycle video
            loot_wait_animation.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
            });


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

        appear_animation = new MediaPlayer();
        try {
            switch (loot_list.get(i).first) {
                case 3:
                    appear_animation.setDataSource(getContext(), Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.appear_3star_gacha));
                    break;
                case 4:
                    appear_animation.setDataSource(getContext(), Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.appear_4star_gacha));
                    break;
                case 5:
                    appear_animation.setDataSource(getContext(), Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.appear_5star_gacha));
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            appear_animation.reset();
            try {
                switch (loot_list.get(i).first) {
                    case 3:
                        appear_animation.setDataSource(getContext(), Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.appear_3star_gacha));
                        break;
                    case 4:
                        appear_animation.setDataSource(getContext(), Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.appear_4star_gacha));
                        break;
                    case 5:
                        appear_animation.setDataSource(getContext(), Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.appear_5star_gacha));
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
            appear_animation.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        appear_animation.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // show animation
                dialog.show();

                appear_surface.setVisibility(View.VISIBLE);
                sh2 = appear_surface.getHolder();
                appear_animation.start();
            }
        });
        //pull_animation_1.setVisibility(View.VISIBLE);

        appear_animation.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                appear_surface.setVisibility(View.GONE);
                appear_animation.reset();
            }
        });



        // switch to next item animation
        pull_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                // if there's more pulls left then do again
                if (clip_is_playing) return;
                if (i != loot_list.size() - 1) {
                    animation.cancel();
                    try {
                        showLootAnimation_10(loot_list, i + 1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // if pulls ended then close window and release resources
                else {
                    pull_ost.pause();
                    pull_ost.seekTo(0);
                    dialog.dismiss();
                    loot_wait_animation.stopPlayback();
                    //engine.stop();
                    if(!ost1.isPlaying())
                        ost1.start();
                }
            }
        });

    }

    public void showPullAnimation_10(int max_rarity, ArrayList<pair> loot_list) {
        pull_animation = new MediaPlayer();
        try {
            setDataSourceByRarity(max_rarity);
        } catch (IOException e) {
            pull_animation.reset();
            try {
                setDataSourceByRarity(max_rarity);
            } catch (IOException e1) {
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
        //memories_screen.setVisibility(View.GONE);
        //pull_animation_1.start();
        pull_animation.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //memories_screen.setVisibility(View.VISIBLE);
                pull_animation_screen.setVisibility(View.GONE);
                pull_surface.setVisibility(View.GONE);
                pull_animation.reset();
                //if (!pull_ost.isPlaying())
                //pull_ost.start();
                try {
                    showLootAnimation_10(loot_list, 0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
    private void setDataSourceByRarity(int rarity) throws IOException {
        int resId;
        switch (rarity) {
            case 3:
                resId = R.raw.pull_3star;
                break;
            case 4:
                resId = R.raw.pull_4star;
                break;
            case 5:
                resId = R.raw.pull_5star;
                break;
            default:
                return;
        }
        pull_animation.setDataSource(getContext(), Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + resId));
    }
    
}