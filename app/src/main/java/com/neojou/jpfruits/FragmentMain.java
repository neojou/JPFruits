package com.neojou.jpfruits;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.neojou.jpfruits.databinding.FragmentMainBinding;

@startuml
package com.neojou.jpfruits {
    class FragmentMain {
        - static final String TAG = "JPFruits:FragmentMain"
                - FragmentMainBinding binding
        - FragmentQuestion frag_question
        - FragmentImage frag_image
        - ConstraintLayout layout_buttons
        - Button button_start
        - Button button_answer
        - Button button_next
        - Button button_finished
        - Button button_return
        - boolean isAnswered
        + View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        + void onViewCreated(View view, Bundle savedInstanceState)
        + void onDestroyView()
        + void onClick(View view)
        - void setViewItemsBinding()
        - void setup_button_padding(DisplayMetrics dm)
        - void setup_button_textsize(DisplayMetrics dm)
        - void setup_button_display(DisplayMetrics dm)
        - void init_screen()
        - void setButtons()
        - void set_button_click_listener()
        - void to_question_start()
        - void to_check_answer()
        - void to_next_question()
        - void to_question_finished()
        - void return_to_main()
        - void switch_to_main_page()
        - void switch_to_question()
        - void ft_switch_to_main()
        - void ft_switch_to_question()
        - void set_buttons_to_main()
        - void set_buttons_to_question()
        - void set_buttons_to_return_only()
    }

    FragmentMain ..|> NJFragment
    FragmentMain o-- FragmentMainBinding
    FragmentMain o-- FragmentQuestion
    FragmentMain o-- FragmentImage
    FragmentMain o-- ConstraintLayout
    FragmentMain o-- Button
    FragmentMain o-- DisplayMetrics
    FragmentMain o-- LayoutInflater
    FragmentMain o-- ViewGroup
    FragmentMain o-- Bundle
    FragmentMain o-- View
    FragmentMain o-- DataBindingUtil
}
@enduml


public class FragmentMain extends NJFragment
        implements View.OnClickListener {
    private static final String TAG="JPFruits:FragmentMain";

    FragmentMainBinding binding;
    FragmentQuestion frag_question;
    FragmentImage frag_image;
    ConstraintLayout layout_buttons;
    Button button_start;
    Button button_answer;
    Button button_next;
    Button button_finished;
    Button button_return;

    boolean isAnswered;

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
        layout_buttons = binding.layoutButtons;
        button_start = binding.buttonStart;
        button_answer = binding.buttonAnswer;
        button_next = binding.buttonNext;
        button_finished = binding.buttonFinished;
        button_return = binding.buttonReturn;
    }

    private void setup_button_padding(DisplayMetrics dm) {
        int screen_height_px = dm.heightPixels;
        final float layout_height_ratio = 0.15f * 0.1f; // 10% for the space
        int choice_px = (int)((float)(screen_height_px) * layout_height_ratio);
        //Log.d(TAG, "setup_button_padding() : padding px = " + choice_px);
        if (choice_px < 20) {
            Log.e(TAG, "setup_button_padding() : choice_px(" + choice_px + ") < 20");
            return;
        }
        layout_buttons.setPadding(choice_px, choice_px, choice_px, choice_px);
    }

    private void setup_button_textsize(DisplayMetrics dm) {
        int screen_width_px = dm.widthPixels;
        final float layout_width_ratio = 0.6f;
        final float max_words_num = 12.0f;
        int choice_px = (int)((float)(screen_width_px) * layout_width_ratio / max_words_num);
        //Log.d(TAG, "setup_text_size() : padding px = " + choice_px);
        if (choice_px < 48) {
            Log.e(TAG, "setup_button_textsize() : choice_px(" + choice_px + ") < 48");
            return;
        }
        button_start.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);
        button_answer.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);
        button_next.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);
        button_finished.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);
        button_return.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);
    }

    private void setup_button_display(DisplayMetrics dm) {
        setup_button_padding(dm);
        setup_button_textsize(dm);
    }

    private void init_screen()
    {
        DisplayMetrics dm = get_screen_display_metrics();
        if (dm == null) {
            Log.e(TAG, "init_screen() : DisplayMetrics is null");
            return;
        }
        setup_button_display(dm);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init_screen();
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
