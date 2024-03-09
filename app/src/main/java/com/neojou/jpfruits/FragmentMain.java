package com.neojou.jpfruits;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.neojou.jpfruits.databinding.FragmentMainBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FragmentMain extends Fragment
        implements View.OnClickListener {
    private static final String TAG="JPFruits:FragmentMain";

    FragmentMainBinding binding;
    FragmentQuestion frag_question;
    FragmentImage frag_image;
    Button button_start;
    Button button_answer;
    Button button_next;
    Button button_finished;
    Button button_return;

    boolean isAnswered;

    public FragmentMain()
    {
        super(R.layout.fragment_main);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        setViewItemsBinding();
        setButtons();

        isAnswered = false;

        return binding.getRoot();
    }

    private void setViewItemsBinding() {
        button_start = binding.buttonStart;
        button_answer = binding.buttonAnswer;
        button_next = binding.buttonNext;
        button_finished = binding.buttonFinished;
        button_return = binding.buttonReturn;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch_to_main_page();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
    }

    private void to_question_start() {
        switch_to_question();
        isAnswered = false;
    }


    private void to_check_answer() {
        frag_question.check_answer();

        isAnswered = true;
        set_buttons_to_question();
    }


    private void to_next_question() {
        //Log.d(TAG, "next_question");

        if ( frag_question.set_next_question() ) {
            isAnswered = false;
            set_buttons_to_question();
        } else {
            to_question_finished();
        }
    }

    private void to_question_finished() {
        //Log.d(TAG, "to_question_finished");

        frag_question.finish_answer();

        set_buttons_to_return_only();
    }

    private void return_to_main() {
        //Log.d(TAG, "return to main");
        isAnswered = false;
        switch_to_main_page();
    }

    private void switch_to_main_page() {
        ft_switch_to_main();
        set_buttons_to_main();
    }

    private void switch_to_question() {
        ft_switch_to_question();
        set_buttons_to_question();
    }

    private void ft_switch_to_main() {
        FragmentManager fragment_manager;
        FragmentActivity ac = getActivity();
        if (ac != null) {
             fragment_manager = ac.getSupportFragmentManager();
        } else {
            Log.e(TAG, "ft_switch_to_main(): getActivity() is null");
            return;
        }
        FragmentTransaction ft;
        ft = fragment_manager.beginTransaction();

        frag_image = new FragmentImage();
        ft.replace(R.id.fragment_main, frag_image);
        ft.setReorderingAllowed(true);
        ft.commit();
    }

    private void ft_switch_to_question() {
        FragmentManager fragment_manager;
        FragmentActivity ac = getActivity();
        if (ac != null) {
            fragment_manager = ac.getSupportFragmentManager();
        } else {
            Log.e(TAG, "ft_switch_to_question(): getActivity() is null");
            return;
        }

        FragmentTransaction ft;
        ft = fragment_manager.beginTransaction();

        frag_question = new FragmentQuestion();
        frag_question.init_data();
        ft.replace(R.id.fragment_main, frag_question);
        ft.setReorderingAllowed(true);
        ft.commit();
    }

    private void set_buttons_to_main() {
        button_start.setVisibility(View.VISIBLE);
        button_answer.setVisibility(View.GONE);
        button_next.setVisibility(View.GONE);
        button_finished.setVisibility(View.GONE);
        button_return.setVisibility(View.GONE);
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
    }

    private void set_buttons_to_return_only() {
        button_start.setVisibility(View.GONE);
        button_answer.setVisibility(View.GONE);
        button_next.setVisibility(View.GONE);
        button_finished.setVisibility(View.GONE);
        button_return.setVisibility(View.VISIBLE);
    }


}
