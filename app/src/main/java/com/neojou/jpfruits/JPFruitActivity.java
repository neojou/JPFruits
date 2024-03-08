package com.neojou.jpfruits;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class JPFruitActivity extends AppCompatActivity {
    FragmentManager fragment_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingViewItems();
        ft_start();
    }

    private void bindingViewItems() {
        DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    private void ft_start() {
        fragment_manager = getSupportFragmentManager();
        FragmentMain frag_main = new FragmentMain(fragment_manager);

        FragmentTransaction ft;
        ft = fragment_manager.beginTransaction();
        ft.replace(R.id.fragment_activity_main, frag_main);
        ft.setReorderingAllowed(true);
        ft.commit();
    }
}

