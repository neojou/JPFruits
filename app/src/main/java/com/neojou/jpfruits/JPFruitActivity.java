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
        if (savedInstanceState == null) {
            // initial do once data
            FragmentQuestion.init_fruit_dvm(this.getApplication());
            ft_start();
        }
    }

    private void bindingViewItems() {
        DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    private void ft_start() {
        // fragment settings
        fragment_manager = getSupportFragmentManager();
        FragmentMain frag_main = new FragmentMain();

        FragmentTransaction ft;
        ft = fragment_manager.beginTransaction();
        ft.replace(R.id.fragment_activity_main, frag_main);
        ft.setReorderingAllowed(true);
        ft.commit();
    }
}

