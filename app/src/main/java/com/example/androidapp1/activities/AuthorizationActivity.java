// launch activity
package com.example.androidapp1.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.androidapp1.service.ConeFirebaseManager;
import com.example.androidapp1.service.SiteUsersManager;
import com.example.androidapp1.service.UsersDataFirebaseManager;
import com.example.androidapp1.service.UsersFirebaseManager;
import com.example.androidapp1.models.Character;
import com.example.androidapp1.models.ConeUserdata;
import com.example.androidapp1.models.InventoryItem;
import com.example.androidapp1.models.Role;
import com.example.androidapp1.models.User;
import com.example.androidapp1.models.UserData;
import com.example.androidapp1.utils.pair;
import com.example.androidapp1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorizationActivity extends AppCompatActivity {

    TextView touch_screen_text;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users, usersData;
    public static ConeFirebaseManager coneFirebaseManager;
    UsersDataFirebaseManager usersDataFirebaseManager;
    UsersFirebaseManager usersFirebaseManager;

    ConstraintLayout root;

    VideoView videoPlayer;

    MediaPlayer ost1;

    ArrayList<Character> characters = new ArrayList<>();
    private List<ConeUserdata> items_cone;
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
                ost1 = MediaPlayer.create(AuthorizationActivity.this, R.raw.ost1);
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


        //usersFirebaseManager.clearUsersData();
        //usersDataFirebaseManager.clearUsersData();
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


        characters.add(new Character("0", "Kiana", 0, 1, 0, 120,
                215, 102, 5, 50, 21, 11, 7,
                "ability_Kiana", "passive_Kiana", "talent_Kiana", false, "null"));
        characters.add(new Character("1", "Kafka", 0, 1, 0, 110,
                225, 105, 5, 50, 17, 13, 6,
                "ability_Kafka", "passive_Kafka", "talent_Kafka", false, "null"));
        characters.add(new Character("2", "Blade", 0, 1, 0, 137,
                192, 112, 5, 50, 25, 9, 7,
                "ability_Blade", "passive_Blade", "talent_Blade", false, "null"));
        characters.add(new Character("3", "Jing Liu", 0, 1, 0, 117,
                202, 112, 5, 50, 25, 9, 7,
                "ability_Jing Liu", "passive_Jing Liu", "talent_Jing Liu", false, "null"));
        // add inventory items
        coneFirebaseManager = new ConeFirebaseManager();
        items_cone = coneFirebaseManager.fetchConesAndInitializeUserData();
        //items_artifact = parseItemsFromFile_artifacts(this, R.raw.artifacts);
        //items_item = parseItemsFromFile_items(this, R.raw.items);

        usersDataFirebaseManager = new UsersDataFirebaseManager();
        usersFirebaseManager = new UsersFirebaseManager();
        //usersFirebaseManager.clearUsersData();
        //usersDataFirebaseManager.clearUsersData();
    }

    private void set_animations(){
        Animation connectingAnimation = AnimationUtils.loadAnimation(AuthorizationActivity.this, R.anim.alpha_scale_animation);
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
                                // Проверяем, существует ли информация о пользователе в Realtime Database
                                String uid = Objects.requireNonNull(authResult.getUser()).getUid();
                                users.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (!snapshot.exists()) {
                                            User user = new User();
                                            SiteUsersManager siteUsersManager = new SiteUsersManager();
                                            siteUsersManager.getUserByID(uid).thenAccept(siteUser -> {
                                                if (siteUser != null) {
                                                    user.setId(siteUser.getUid());
                                                    user.setEmail(siteUser.getEmail());
                                                    user.setUsername(siteUser.getUsername());
                                                    user.setNickname("");
                                                    user.setPass(pass.getText().toString());
                                                    user.getRoles().add(Role.USER);
                                                    users.child(uid).setValue(user);

                                                    UserData userData = UserData.getInstance();
                                                    // Заполняем userData вашими значениями, как вы делаете в регистрации
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
                                                    userData.setId(uid);
                                                    //userData.setArtifacts(items_artifact);
                                                    //userData.setItems(items_item);
                                                    //добавление данных пользователя в бд

                                                    usersData.child(uid)
                                                            .setValue(userData)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Snackbar.make(root, "Данные успешно загружены!", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            });

                                                    startActivity(new Intent(AuthorizationActivity.this, HomeActivity.class));
                                                    releaseResources();
                                                    finish();
                                                }

                                            });

                                        }
                                        else {
                                            startActivity(new Intent(AuthorizationActivity.this, HomeActivity.class));
                                            releaseResources();
                                            finish();
                                        }
                                        // Переходим на следующий экран
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Обработка ошибки, если потребуется
                                    }
                                });
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
                Animation connectingAnimation = AnimationUtils.loadAnimation(AuthorizationActivity.this, R.anim.alpha_scale_animation);
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
        final EditText username = dialog.findViewById(R.id.nameField);
        AppCompatButton sign_up_button = dialog.findViewById(R.id.signUp);
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (pass.getText().toString().length() < 5) {
                    Snackbar.make(root, "Password require length more than 5 symbols ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username.getText().toString())) {
                    Snackbar.make(root, "Enter your username", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (ifUsedUsername(username.getText().toString())){
                    Snackbar.make(root, "This username is already in use!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                // регистрация пользователя
                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // data for sign in
                                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setUsername(username.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setId(uid);
                                user.setNickname("");

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
                                userData.setId(uid);
                                //userData.setArtifacts(items_artifact);
                                //userData.setItems(items_item);
                                //добавление данных пользователя в бд

                                usersData.child(uid)
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
                Animation connectingAnimation = AnimationUtils.loadAnimation(AuthorizationActivity.this, R.anim.alpha_scale_animation);
                touch_screen_text.setAnimation(connectingAnimation);
                touch_screen_text.getAnimation().start();
            }
        });
        dialog.show();
    }


    private boolean ifUsedUsername(String username){
        for (User el: usersFirebaseManager.fetchAllUsers()) {
            if (el.getNickname() != null && Objects.equals(username, el.getUsername()))
                return true;
        }
        return false;
    }


    private void releaseResources(){
        ost1.stop();
        ost1.setLooping(false);
        videoPlayer.stopPlayback();
    }


}

