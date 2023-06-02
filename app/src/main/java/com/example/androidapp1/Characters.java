package com.example.androidapp1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.widget.AppCompatButton;

import org.rajawali3d.Object3D;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.scene.Scene;
import org.rajawali3d.view.SurfaceView;

import java.util.ArrayList;

public class Characters extends Activity {

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
    AppCompatButton to_home;
    private ImageView character_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);
        init_all();
        SetOnClickListeners();

        surfaceViewKiana.setZOrderOnTop(true);
        surfaceViewKiana.getHolder().setFormat(PixelFormat.TRANSLUCENT);


        // Making surfaceView transparent


        renderer_kiana = new MyRenderer(Characters.this, "kiana");
        // render surface
        surfaceViewKiana.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        surfaceViewKiana.setSurfaceRenderer(renderer_kiana);
        surfaceViewKiana.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //mRenderer.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mRenderer.onPause();
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        if (surfaceViewKiana.getVisibility() == View.VISIBLE)
            renderer_kiana.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void init_all (){
        surfaceViewKiana = findViewById(R.id.surface_view_kiana);
        kiana_icon = findViewById(R.id.kiana_icon);
        kafka_icon = findViewById(R.id.kafka_icon);
        blade_icon = findViewById(R.id.blade_icon);
        character_image = findViewById(R.id.character_image);
        to_home = findViewById(R.id.charactersToHome);

    }

    public void SetOnClickListeners(){
        kiana_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setVisibility(View.GONE);
                renderer_kiana.onResume();
                //surfaceViewKiana.setVisibility(View.VISIBLE);
            }
        });

        kafka_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setImageResource(R.drawable.kafka_bg);
                character_image.setVisibility(View.VISIBLE);
                //surfaceViewKiana.setVisibility(View.GONE);
            }
        });
        blade_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                character_image.setImageResource(R.drawable.blade_bg1);
                character_image.setVisibility(View.VISIBLE);
                //surfaceViewKiana.setVisibility(View.GONE);
            }
        });

        to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Characters.this, HomeScreen.class));
                finish();
            }
        });



    }
}