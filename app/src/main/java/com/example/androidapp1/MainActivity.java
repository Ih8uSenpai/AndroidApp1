// launch activity
package com.example.androidapp1;

import static com.example.androidapp1.HomeScreen.parseItemsFromFile_artifacts;
import static com.example.androidapp1.HomeScreen.parseItemsFromFile_cones;
import static com.example.androidapp1.HomeScreen.parseItemsFromFile_items;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.androidapp1.Models.Character;
import com.example.androidapp1.Models.InventoryItem;
import com.example.androidapp1.Models.User;
import com.example.androidapp1.Models.UserData;
import com.example.androidapp1.Models.pair;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CONES_SIZE = 3;
    private static final int ARTIFACTS_SIZE = 5;
    private static final int ITEMS_SIZE = 2;
    TextView touch_screen_text;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users, usersData;

    ConstraintLayout root;

    VideoView videoPlayer;

    MediaPlayer ost1;

    ArrayList<Character> characters = new ArrayList<>();
    private List<InventoryItem> items_cone;
    private List<InventoryItem> items_artifact;
    private List<InventoryItem> items_item;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_all();
        set_animations();

        // adding video
        Uri myVideoUri= Uri.parse( "android.resource://" + getPackageName() + "/" + R.raw.launch_bg2);
        videoPlayer.setVideoURI(myVideoUri);
        // start+cycle video
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                touch_screen_text.setVisibility(View.VISIBLE);
                // audio
                ost1 = MediaPlayer.create(MainActivity.this, R.raw.ost1);
                ost1.setLooping(true);
                ost1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        ost1.seekTo(0);
                        ost1.start();
                    }
                });
            }
        });



        // add event to open auth window
        root.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_screen_text.getAnimation().cancel();
                touch_screen_text.getAnimation().reset();
                touch_screen_text.clearAnimation();
                touch_screen_text.setVisibility(View.GONE);
                showSignInWindow();
                return false;
            }
        });
    }



    private void init_all(){
        touch_screen_text = findViewById(R.id.touch_screen_text);
        root = findViewById(R.id.root_element);
        videoPlayer = findViewById(R.id.videoView);

        // database interaction variables
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        usersData = db.getReference("UsersData");

        // add characters
        characters.add(new Character("Kiana", 1, 0, 0, 0, 0, 0, "ability_kiana", "passive_kiana", "talent_kiana", false));
        characters.add(new Character("Kafka", 1, 0, 0, 0, 0, 0, "ability_kafka", "passive_kafka", "talent_kafka", false));
        characters.add(new Character("Blade", 1, 0, 0, 0, 0, 0, "ability_blade", "passive_blade", "talent_blade", false));

        // add inventory items
        items_cone = parseItemsFromFile_cones(this, R.raw.cones);
        items_artifact = parseItemsFromFile_artifacts(this, R.raw.artifacts);
        items_item = parseItemsFromFile_items(this, R.raw.items);

    }

    private void set_animations(){
        Animation connectingAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_scale_animation);
        touch_screen_text.setAnimation(connectingAnimation);
        touch_screen_text.getAnimation().start();
    }

    private void showSignInWindow() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setContentView(sign_in_window);
        //dialog.setMessage("Введите данные");
        AppCompatButton btnSignUp = dialog.findViewById(R.id.signUp);
        final EditText email = dialog.findViewById(R.id.emailField);
        final EditText pass = dialog.findViewById(R.id.passField);
        AppCompatButton sign_in_button = dialog.findViewById(R.id.signIn);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "password length must be longer than 5 symbols", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(MainActivity.this, HomeScreen.class));
                                //startActivity(new Intent(MainActivity.this, ReadActivity.class));
                                releaseResources();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(root, "Ошибка авторизации", Snackbar.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showRegisterWindow();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                touch_screen_text.setVisibility(View.VISIBLE);
                Animation connectingAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_scale_animation);
                touch_screen_text.setAnimation(connectingAnimation);
                touch_screen_text.getAnimation().start();
            }
        });

        dialog.show();
    }

    private void showRegisterWindow() {
        Dialog dialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.register_window, null);
        dialog.setContentView(sign_in_window);

        final EditText email = dialog.findViewById(R.id.emailField);
        final EditText pass = dialog.findViewById(R.id.passField);
        final EditText name = dialog.findViewById(R.id.nameField);
        AppCompatButton sign_up_button = dialog.findViewById(R.id.signUp);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Пароль должен быть больше 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите имя пользователя", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // регистрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // data for sign in
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());

                                //добавление в бд
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user);
                                // game data for user
                                UserData userData = UserData.getInstance();;

                                userData.setExp(0);
                                userData.setGems(0);
                                userData.setGold(0);
                                userData.setLvl(1);
                                userData.setPulls(0);
                                userData.setEvent_pulls(0);
                                userData.setPulls_to_4star(0);
                                userData.setPulls_to_5star(0);
                                userData.setLoot_table(userData.generateLootTable(new ArrayList<pair>()));
                                userData.setCharacters(characters);
                                //if (userData.getCones().size() != 16)
                                userData.setCones(items_cone);
                                userData.setArtifacts(items_artifact);
                                userData.setItems(items_item);
                                //добавление данных пользователя в бд
                                usersData.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Данные успешно загружены!", Snackbar.LENGTH_LONG).show();
                                            }
                                        });

                            }
                        });
                dialog.dismiss();
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                touch_screen_text.getAnimation().cancel();
                touch_screen_text.getAnimation().reset();
                touch_screen_text.clearAnimation();
                touch_screen_text.setVisibility(View.GONE);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                touch_screen_text.setVisibility(View.VISIBLE);
                Animation connectingAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_scale_animation);
                touch_screen_text.setAnimation(connectingAnimation);
                touch_screen_text.getAnimation().start();
            }
        });
        dialog.show();
    }

    private void releaseResources(){
        ost1.stop();
        ost1.setLooping(false);
        videoPlayer.stopPlayback();
    }


}

