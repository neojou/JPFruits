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
    FragmentQuestion frag_question;
    FragmentImage frag_image;
    ConstraintLayout window_layout;
    Button button_start;
    Button button_answer;
    Button button_next;
    Button button_finished;
    Button button_return;
    Button button_quit;

    boolean isStarted;
    boolean isFinished;
    boolean isAnswered;


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

        switch_to_main_page();

        isStarted = false;
        isFinished = false;
        isAnswered = false;

        return binding.getRoot();
    }

    private void setViewItemsBinding() {
        window_layout = binding.windowLayout;
        button_start = binding.buttonStart;
        button_answer = binding.buttonAnswer;
        button_next = binding.buttonNext;
        button_finished = binding.buttonFinished;
        button_return = binding.buttonReturn;
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
        button_answer.setOnClickListener(this);
        button_next.setOnClickListener(this);
        button_finished.setOnClickListener(this);
        button_return.setOnClickListener(this);
        button_quit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_start)
            to_question_start();
        else if (id == R.id.button_answer)
            to_check_answer();
        else if (id == R.id.button_next)
            to_next_question();
        else if (id == R.id.button_finished)
            to_question_finished();
        else if (id == R.id.button_return)
            return_to_main();
        else if (id == R.id.button_quit)
            to_quit();
    }

    private void to_question_start() {
        Log.d(TAG, "to_start");

        switch_to_question();

        isStarted = true;
        isFinished = false;
        isAnswered = false;
    }


    private void to_check_answer() {
        Log.d(TAG, "check_answer");

        frag_question.check_answer();

        isAnswered = true;
        set_buttons_to_question();
    }


    private void to_next_question() {
        Log.d(TAG, "next_question");

        if ( frag_question.next_question() ) {
            isAnswered = false;
            set_buttons_to_question();
        } else {
            to_question_finished();
        }
    }

    private void to_question_finished() {
        Log.d(TAG, "to_question_finished");

        frag_question.finish_answer();

        isFinished = true;
        set_buttons_to_return_only();
    }

    private void return_to_main() {
        Log.d(TAG, "return to main");
        isAnswered = false;
        switch_to_main_page();
    }

    private void to_quit() {
        Log.d(TAG, "to_quit");
        getActivity().finish();
        System.exit(0);
    }

    private void switch_to_main_page() {
        ft_switch_to_main();
        set_buttons_to_main();
    }

    private void switch_to_question() {
        ft_switch_to_question();
        set_buttons_to_question();
    }

    private void ft_clean_all_first() {
        if (frag_question != null) {
            if (frag_question.isAdded())
                ft.remove(frag_question);
            frag_question = null;
        }
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

    private void ft_switch_to_question() {
        if (fragment_manager == null) {
            Log.e(TAG, "fragment_manager is null");
            return;
        }
        ft = fragment_manager.beginTransaction();

        ft_clean_all_first();
        frag_question = new FragmentQuestion();
        ft.add(R.id.fragment_main, frag_question);
        ft.commit();
    }

    private void set_buttons_to_main() {
        button_start.setVisibility(View.VISIBLE);
        button_answer.setVisibility(View.GONE);
        button_next.setVisibility(View.GONE);
        button_finished.setVisibility(View.GONE);
        button_return.setVisibility(View.GONE);
        button_quit.setVisibility(View.VISIBLE);
    }

    private void set_buttons_to_question() {
        button_start.setVisibility(View.GONE);
        if (!isAnswered) {
            button_answer.setVisibility(View.VISIBLE);
            button_next.setVisibility(View.GONE);
            button_finished.setVisibility(View.GONE);
        } else {
            button_answer.setVisibility(View.GONE);
            button_next.setVisibility(View.VISIBLE);
            button_finished.setVisibility(View.VISIBLE);
        }
        button_return.setVisibility(View.GONE);
        button_quit.setVisibility(View.GONE);
    }

    private void set_buttons_to_return_only() {
        button_start.setVisibility(View.GONE);
        button_answer.setVisibility(View.GONE);
        button_next.setVisibility(View.GONE);
        button_finished.setVisibility(View.GONE);
        button_return.setVisibility(View.VISIBLE);
        button_quit.setVisibility(View.GONE);
    }


}
