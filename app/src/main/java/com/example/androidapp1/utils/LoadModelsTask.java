package com.example.androidapp1.utils;

import android.os.AsyncTask;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LoadModelsTask extends AsyncTask<Void, Integer, Void> {
    private MyRenderer renderer;
    private ConstraintLayout constraintLayout;

    public LoadModelsTask(MyRenderer renderer, ConstraintLayout constraintLayout) {
        this.renderer = renderer;
        this.constraintLayout = constraintLayout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        constraintLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        renderer.loadModels();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        constraintLayout.setVisibility(View.GONE);
    }
}

