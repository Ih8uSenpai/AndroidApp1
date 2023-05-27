package com.example.androidapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

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

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnRegister;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users, usersData;

    ConstraintLayout root;

    VideoView videoPlayer;

    MediaPlayer ost1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // buttons
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        // launcher layout
        root = findViewById(R.id.root_element);

        // audio
        ost1 = MediaPlayer.create(this, R.raw.ost1);
        ost1.setLooping(true);
        ost1.seekTo(0);
        ost1.start();

        // adding video
        videoPlayer = findViewById(R.id.videoView);
        Uri myVideoUri= Uri.parse( "android.resource://" + getPackageName() + "/" + R.raw.bgvid);
        videoPlayer.setVideoURI(myVideoUri);
        // start+cycle video
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        videoPlayer.start();

        // database interaction variables
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        usersData = db.getReference("UsersData");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }
        });



    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign in");
        //dialog.setMessage("Введите данные");
        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);

        final MaterialEditText email = sign_in_window.findViewById(R.id.emailField);
        final MaterialEditText pass = sign_in_window.findViewById(R.id.passField);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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

        dialog.show();
    }

    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Registration");
        //dialog.setMessage("Введите данные");
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        final MaterialEditText email = register_window.findViewById(R.id.emailField);
        final MaterialEditText pass = register_window.findViewById(R.id.passField);
        final MaterialEditText name = register_window.findViewById(R.id.nameField);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Пользователь добавлен!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                                // game data for user
                                UserData userData = new UserData();

                                userData.setExp(0);
                                userData.setGems(0);
                                userData.setGold(0);
                                userData.setLvl(0);
                                userData.setPulls(0);
                                userData.setEvent_pulls(0);
                                userData.setPulls_to_4star(0);
                                userData.setPulls_to_5star(0);
                                userData.setLoot_table(userData.generateLootTable(new ArrayList<pair>()));
                                //добавление данных пользователя в бд
                                usersData.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root, "Данные успешно загружены!", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });
            }
        });

        dialog.show();
    }

    private void releaseResources(){
        ost1.stop();
        ost1.setLooping(false);
        videoPlayer.stopPlayback();
    }

    /*private void showLoginWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Вход");
        //dialog.setMessage("Введите данные");
        LayoutInflater inflater = LayoutInflater.from(this);
        View loginWindow = inflater.inflate(R.layout.activity_main, null);
        dialog.setView(loginWindow);
        dialog.show();
    }*/
}

