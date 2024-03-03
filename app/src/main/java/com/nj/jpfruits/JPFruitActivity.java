package com.nj.jpfruits;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.concurrent.ExecutorService;


public class JPFruitActivity extends AppCompatActivity {
    private static final String TAG="JPFruits::JPFruitActivity";
    FragmentManager fragment_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);
            ft_start();
        }
    }

    private void ft_start() {
        fragment_manager = getSupportFragmentManager();
        if (fragment_manager == null) {
            Log.e(TAG, "fragment_manager is null");
            return;
        }

        FragmentImage frag_image = new FragmentImage();

        FragmentTransaction ft;
        ft = fragment_manager.beginTransaction();
        ft.setReorderingAllowed(true);
        ft.replace(R.id.fragment_activity_main, frag_image);
        ft.commit();
    }
}

