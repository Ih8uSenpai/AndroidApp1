// Class for rendering 3d objects
package com.example.androidapp1.utils;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;

import com.example.androidapp1.R;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.StreamingTexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.Renderer;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;


public class MyRenderer extends Renderer {

    private Context mContext;
    private Object3D mObject3D;

    public ArrayList<Object3D> getObjects3D() {
        return objects3D;
    }


    private ArrayList<Object3D> objects3D;
    private float mPreviousX;
    private float mPreviousY;
    private int current_index = 0;
    ArrayList<Integer> resourceIds = new ArrayList<>();


    public String character_name;

    // Initial radius, theta, and phi
    float radius = 6.0f; // Distance from the object. Adjust this as necessary
    float theta = 1.5f;   // Initial rotation around Y-axis
    float phi = (float) Math.toRadians(80);   // Initial rotation around X-axis, setting to a "second person" view

    private StreamingTexture mVideoTexture;
    private MediaPlayer mMediaPlayer;


    public MyRenderer(Context context, String character_name) {
        super(context);
        objects3D = new ArrayList<>();
        mContext = context;
        this.character_name = character_name;
        setFrameRate(2);
    }


    @Override
    protected void initScene() {


    }

    public void loadModels() {
        getCurrentScene().setBackgroundColor(0, 0, 0, 0);
        Camera camera = getCurrentCamera();


        // Convert spherical coordinates to Cartesian coordinates for the initial camera position
        float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
        float y = (float) (radius * Math.cos(phi));
        float z = (float) (radius * Math.sin(phi) * Math.sin(theta));

        camera.setPosition(x, 4, z);//0 4 5

        /*DirectionalLight key = new DirectionalLight(-3,-4,-5);
        key.setColor(1.0f, 1.0f, 1.0f);
        key.setPower(2);
        getCurrentScene().addLight(key);*/
        for (int ind = 1; ind <= 3; ind++) {
            String objectPath = "raw/" + character_name + ind;
            int resourceId = getContext().getResources().getIdentifier(objectPath, null, getContext().getPackageName());
            resourceIds.add(resourceId);
        }

        for (int i = 0; i < resourceIds.size(); i++) {
            int resourceId = resourceIds.get(i);

            try {
                LoaderOBJ loader = new LoaderOBJ(getContext().getResources(), mTextureManager, resourceId);
                loader.parse();
                Object3D object3D = loader.getParsedObject();
                object3D.setScale(0.2f);
                objects3D.add(object3D);
            } catch (ParsingException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            // Create and start the media player
            mMediaPlayer = MediaPlayer.create(getContext(), R.raw.bg5);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
            mMediaPlayer.setVideoScalingMode(2);

            // Create the video texture
            mVideoTexture = new StreamingTexture("videoTexture", mMediaPlayer);

            // Create a material and apply the video texture to it
            Material material = new Material();
            material.setColorInfluence(0);
            material.addTexture(mVideoTexture);

            int sphereSegments = 1024; // Increase this value to increase sphere geometry complexity
            Sphere sphere = new Sphere(15, sphereSegments, sphereSegments);
            sphere.setScaleX(-1);
            // Apply the material to the sphere
            sphere.setMaterial(material);

            // Add the sphere to the scene
            getCurrentScene().addChild(sphere);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Always look at the center of the first object in the list
        if(!objects3D.isEmpty()) {
            camera.setLookAt(objects3D.get(0).getPosition().x, objects3D.get(0).getPosition().y + 2, objects3D.get(0).getPosition().z);
        }

        int surfaceWidth = getViewportWidth();
        int surfaceHeight = getViewportHeight();
        camera.setProjectionMatrix(surfaceWidth, surfaceHeight);
    }

    public void setObjects3D(ArrayList<Object3D> objects3D) {
        this.objects3D = objects3D;
    }
    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        // Not needed for this implementation
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        float dTheta = 0; // Rotation around Y-axis
        //float dPhi = 0;   // Rotation around X-axis

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                dTheta = (x - mPreviousX) * 0.01f; // Reducing the speed of rotation
                //dPhi = (y - mPreviousY) * 0.01f;   // Reducing the speed of rotation

                rotateCamera(dTheta);
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
    }

    private void rotateCamera(float dTheta){

        theta += dTheta;


        // Convert spherical coordinates to Cartesian coordinates
        float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
        float y = (float) (radius * Math.cos(phi));
        float z = (float) (radius * Math.sin(phi) * Math.sin(theta));

        // Update camera position
        getCurrentCamera().setPosition(x, 4, z);

        // Always look at the center of the object
        getCurrentCamera().setLookAt(objects3D.get(0).getPosition().x, objects3D.get(0).getPosition().y + 2, objects3D.get(0).getPosition().z);
    }





    private int counter = 0;  // Initialize a counter variable
    private int frameRate = 7; // Adjust this value to slow down or speed up the animation

    private void rotateAllObjects(float dx){
        for (int i = 0; i < objects3D.size(); i++) {
            objects3D.get(i).rotate(Vector3.Axis.Y, -dx * 0.1f);
        }
    }

    public int i_r = 0;
    public int i_y = 0;
    public void change_y(){
        float x = (float) (radius * Math.sin(phi) * Math.cos(theta));
        float y = (float) (radius * Math.cos(phi));
        float z = (float) (radius * Math.sin(phi) * Math.sin(theta));
        getCurrentCamera().setPosition(x, y + 14 + i_y, z);
    }


    public void change_radius(){
        float x = (float) ((radius + i_r) * Math.sin(phi) * Math.cos(theta));
        float y = (float) ((radius + i_r) * Math.cos(phi));
        float z = (float) ((radius + i_r) * Math.sin(phi) * Math.sin(theta));
        getCurrentCamera().setPosition(x, y + 14, z);
    }
    @Override
    protected void onRender(long elapsedTime, double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        if (mVideoTexture != null) {
            mVideoTexture.update();
        }
        counter++;
        // Only switch objects every frameRate-th render call
        if (counter % frameRate == 0) {
            // Animate the object by switching between frames
            if (objects3D != null && !objects3D.isEmpty()) {
                // make the current object invisible
                objects3D.get(current_index).setVisible(false);
                // remove the current object from scene
                getCurrentScene().removeChild(objects3D.get(current_index));

                // calculate the next index
                current_index = (current_index + 1) % objects3D.size();

                // get the next object
                Object3D nextFrame = objects3D.get(current_index);

                // make the next object visible
                nextFrame.setVisible(true);

                // add the next object to scene
                getCurrentScene().addChild(nextFrame);

                // update mObject3D to point to the next object
                mObject3D = nextFrame;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null) mMediaPlayer.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaPlayer != null) mMediaPlayer.start();
    }

}