package com.nj.jpfruits;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.nj.jpfruits.databinding.FragmentMainBinding;

public class FragmentMain extends Fragment
        implements View.OnClickListener {
    private static final String TAG="JPFruits:FragmentMain";
    final private FragmentManager fragment_manager;

    FragmentMainBinding binding;
    FragmentTransaction ft;
    FragmentImage frag_image;
    ConstraintLayout window_layout;
    Button button_start;
    Button button_quit;

    public FragmentMain(FragmentManager fm)
    {
        super(R.layout.fragment_main);
        fragment_manager = fm;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        setViewItemsBinding();
        setButtons();

        switch_to_main_image();

        return binding.getRoot();
    }

    private void setViewItemsBinding() {
        window_layout = binding.windowLayout;
        button_start = binding.buttonStart;
        button_quit = binding.buttonQuit;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setButtons() {
        set_button_click_listener();
    }

    private void set_button_click_listener() {
        button_start.setOnClickListener(this);
        button_quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_start)
            to_start();
        else if (id == R.id.button_quit)
            to_quit();
    }

    private void to_start() {
        Log.d(TAG, "to_start");
    }

    private void to_quit() {
        Log.d(TAG, "to_quit");
        getActivity().finish();
        System.exit(0);
    }

    private void switch_to_main_image() {
        ft_switch_to_main();
        set_buttons_to_main();
    }

    private void ft_clean_all_first() {
        if (frag_image != null) {
            if (frag_image.isAdded())
                ft.remove(frag_image);
            frag_image = null;
        }
    }
    private void ft_switch_to_main() {
        if (fragment_manager == null) {
            Log.e(TAG, "fragment_manager is null");
            return;
        }
        ft = fragment_manager.beginTransaction();

        ft_clean_all_first();
        frag_image = new FragmentImage();
        ft.add(R.id.fragment_main, frag_image);
        ft.commit();
    }

    private void set_buttons_to_main() {
        button_start.setVisibility(View.VISIBLE);
        button_quit.setVisibility(View.VISIBLE);
        //button_next.setVisibility(View.GONE);
    }
}
