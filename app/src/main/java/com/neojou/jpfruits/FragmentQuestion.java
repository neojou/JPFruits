package com.neojou.jpfruits;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.neojou.jpfruits.databinding.FragmentQuestionBinding;


import java.util.ArrayList;
import java.util.Collections;

public class FragmentQuestion extends NJFragment
{
    private static final String TAG="JPFruits:FragmentQuestion";

    private static FruitDataViewModel fruit_dvm = null;

    FragmentQuestionBinding binding;
    ImageView question_image;
    TextView answer_result_line1;
    TextView answer_result_line2;
    TextView answer_result_line3;
    RadioGroup choice_rd;
    private static final int total_choice_num = 3;
    final RadioButton[] choice_rb = new RadioButton[total_choice_num];
    final String[] choices = new String[total_choice_num];

    Question cur_question;
    int cur_question_id;

    int total_answered;
    int correct_answered;
    int wrong_answered;


    public void init_data() {
        if (fruit_dvm == null) {
            Log.e(TAG, "init_data(): fruit_dvm is null");
            return;
        }
        fruit_dvm.shuffle();
        cur_question_id = 0;
        stats_set_start();
    }

    public static void init_fruit_dvm(Application app) {
        if (fruit_dvm == null) {
            fruit_dvm = new FruitDataViewModel(app);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false);
        setViewItemsBinding();
        return binding.getRoot();
    }

    private void setViewItemsBinding() {
        question_image = binding.questionImage;
        answer_result_line1 = binding.answerResultLine1;
        answer_result_line2 = binding.answerResultLine2;
        answer_result_line3 = binding.answerResultLine3;
        choice_rd = binding.choices;
        choice_rb[0] = binding.choice1;
        choice_rb[1] = binding.choice2;
        choice_rb[2] = binding.choice3;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cur_question = null;
        cur_question_id = 0;
        fruit_dvm.shuffle();
        stats_set_start();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        set_next_question();
    }

    public void check_answer() {
        if (cur_question == null) {
            Log.e(TAG, "check_answer(): cur_question is null");
            return;
        }

        int answer = 0;
        int id = choice_rd.getCheckedRadioButtonId();
        if (id == R.id.choice1)
            answer = 1;
        else if (id == R.id.choice2)
            answer = 2;
        else if (id == R.id.choice3)
            answer = 3;

        if (answer == 0) {
            Log.e(TAG, "check_answer(): strange no selected answer");
            return;
        }

        set_right_choice_button(cur_question.right_choice);
        answer_result_line2.setVisibility(View.VISIBLE);
        if (answer != cur_question.right_choice) {
            String str = getString(R.string.wrong);
            answer_result_line2.setText(str);
            set_wrong_choice_button(answer);
            stats_add_if_answered_correct(false);
        } else {
            String str = getString(R.string.correct);
            answer_result_line2.setText(str);
            stats_add_if_answered_correct(true);
        }

    }

    private Question generate_question(int pos) {
        String fruit_name = fruit_dvm.get_fruit_engname_by_array_pos(pos);
        if (fruit_name == null) {
            Log.e(TAG,"generate_question(): pos=" + pos + " is wrong");
            return null;
        }
        String jp_name = fruit_dvm.get_fruit_jpname_by_engname(fruit_name);
        if (jp_name == null) {
            Log.e(TAG,"generate_question(): cannot find jpname by : " + fruit_name);
            return null;
        }

        ArrayList<String> fruit_array_temp = fruit_dvm.copy_jpfruits_array();
        if (fruit_array_temp == null) {
            Log.e(TAG,"generate_question(): fruit array is null");
            return null;
        }
        if (fruit_array_temp.size() < ( total_choice_num + 1) ) {
            Log.e(TAG,"generate_question(): fruit array size is "
                    + fruit_array_temp.size() + "less then " + (total_choice_num + 1));
            return null;
        }
        Collections.shuffle(fruit_array_temp);
        fruit_array_temp = new ArrayList<>(fruit_array_temp.subList(0, total_choice_num + 1));
        int correct_answer = (int)(Math.random()*(total_choice_num-1+1) + 1);
        String[] choice_str = new String[total_choice_num];
        int other_answer_id = 0;
        for (int i = 0; i < total_choice_num; i++) {
            if (i == correct_answer - 1) {
                choice_str[i] = jp_name;
                continue;
            }
            if (jp_name.equals(fruit_array_temp.get(other_answer_id))) {
                //Log.i(TAG, "found the same in choices");
                other_answer_id++;
            }
            if (other_answer_id >= (total_choice_num + 1)) {
                Log.e(TAG,"generate_question(): other_answer_id = "
                        + other_answer_id + ">= " + (total_choice_num + 1));
                return null;
            }
            choice_str[i] = fruit_array_temp.get(other_answer_id);
            other_answer_id++;
        }

        return new Question(fruit_name, choice_str[0], choice_str[1], choice_str[2], correct_answer);
    }

    public boolean set_next_question() {
        cur_question = null;
        cur_question_id++;
        if (cur_question_id > fruit_dvm.get_fruit_array_size())
            return false;
        cur_question = generate_question(cur_question_id - 1);
        set_question_to_view(cur_question);
        return true;
    }

    public void finish_answer() {
        stats_show();
    }




    private void set_question_image(final DisplayMetrics dm, final Question q) {

        Activity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "set_question_to_view() : getActivity() is null");
            return;
        }

        int id = activity.getResources().getIdentifier(q.img_filename, "drawable", activity.getPackageName());

        question_image.setImageResource(id);
        question_image.setVisibility(View.VISIBLE);
    }


    private void set_answer_result_lines(final DisplayMetrics dm) {
        int screen_width_px = dm.widthPixels;
        int screen_height_px = dm.heightPixels;
        final float layout_width_ratio = 0.7f;
        final float layout_height_ratio = 0.1f;
        final int max_alphabet_num = 10;
        int choice_width_px = (int)((float)(screen_width_px) * layout_width_ratio / (float)(max_alphabet_num));
        int choice_height_px = (int)((float)(screen_height_px) * layout_height_ratio);
        int choice_px = choice_width_px;
        if (choice_height_px < choice_px) choice_px = choice_height_px;
        if (choice_px < 48) {
            Log.e(TAG, "set_answer_result_lines() : choice_height_px(" + choice_height_px + ") || choice_width_px(" + choice_width_px + ") < 48");
            return;
        }
        Log.d(TAG, "set_answer_result_lines() : choice_px=" + choice_px);

        answer_result_line1.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);
        answer_result_line2.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);
        answer_result_line3.setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);

        answer_result_line1.setVisibility(View.GONE);
        answer_result_line2.setVisibility(View.INVISIBLE);
        answer_result_line3.setVisibility(View.GONE);
    }

    private void set_radio_button_selections(final DisplayMetrics dm, final Question q) {
        int screen_width_px = dm.widthPixels;
        int screen_height_px = dm.heightPixels;
        final float layout_width_ratio = 0.7f;
        final float layout_height_ratio = 0.4f;
        final int max_alphabet_num = 12;
        int choice_width_px = (int)((float)(screen_width_px) * layout_width_ratio / (float)(max_alphabet_num));
        int choice_height_px = (int)((float)(screen_height_px) * layout_height_ratio / (float)(total_choice_num));
        int choice_px = choice_width_px;
        if (choice_height_px < choice_px) choice_px = choice_height_px;
        if (choice_px < 48) {
            Log.e(TAG, "set_radio_button_selections() : choice_height_px(" + choice_height_px + ") || choice_width_px(" + choice_width_px + ") < 48");
            return;
        }
        Log.d(TAG, "set_radio_button_selections() : choice_px=" + choice_px);
        for (int i = 0; i < total_choice_num; i++)
            choice_rb[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, choice_px);

        choice_rd.setVisibility(View.VISIBLE);
        choices[0] = q.choice1;
        choices[1] = q.choice2;
        choices[2] = q.choice3;
        String contentstr = "1. " + choices[0];
        choice_rb[0].setText(contentstr);
        contentstr = "2. " + choices[1];
        choice_rb[1].setText(contentstr);
        contentstr = "3. " + choices[2];
        choice_rb[2].setText(contentstr);
        set_radio_button_color(choice_rb[0], R.color.gray);
        set_radio_button_color(choice_rb[1], R.color.gray);
        set_radio_button_color(choice_rb[2], R.color.gray);
        choice_rb[0].setChecked(false);
        choice_rb[1].setChecked(false);
        choice_rb[2].setChecked(false);
        choice_rd.clearCheck();
    }

    private void set_question_to_view(Question q) {
        if (q == null) {
            Log.e(TAG, "set_question_to_view() : input Question is null");
            return;
        }
        //Log.d(TAG, "set_question_to_view");

        DisplayMetrics dm = get_screen_display_metrics();
        if (dm == null) {
            Log.e(TAG, "set_radio_button_selections() : dm is null");
            return;
        }


        set_question_image(dm, q);

        set_answer_result_lines(dm);
        set_radio_button_selections(dm, q);
    }

    private void set_radio_button_color(RadioButton rb, int color) {
        Context mContext = getContext();
        if (mContext != null)
            rb.setTextColor(ContextCompat.getColorStateList(mContext, color));
    }

    private void set_right_choice_button(int which) {
        int id = which - 1;
        if (id < 0 || id >= total_choice_num ) return;
        set_radio_button_color(choice_rb[id], R.color.radio_correct_color);
        choice_rb[id].setChecked(true);
    }

    private void set_wrong_choice_button(int which) {
        int id = which - 1;
        if (id < 0 || id >= total_choice_num) return;
        set_radio_button_color(choice_rb[id], R.color.radio_wrong_color);
    }

    private void stats_set_start() {
        total_answered = 0;
        correct_answered = 0;
        wrong_answered = 0;
    }

    private void stats_add_if_answered_correct(boolean isCorrect) {
        total_answered++;
        if (isCorrect)
            correct_answered++;
        else
            wrong_answered++;
    }

    private void stats_show() {

        question_image.setVisibility(View.INVISIBLE);

        answer_result_line1.setVisibility(View.VISIBLE);
        answer_result_line2.setVisibility(View.VISIBLE);
        answer_result_line3.setVisibility(View.VISIBLE);
        answer_result_line1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        answer_result_line2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        answer_result_line3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        choice_rd.setVisibility(View.INVISIBLE);

        int rate = 0;
        if (total_answered != 0)
            rate = correct_answered * 100 / total_answered;

        String contentStr = getString(R.string.stats_total_str, total_answered) + "\n\n";
        answer_result_line1.setText(contentStr);
        contentStr = getString(R.string.stats_each_str, correct_answered, wrong_answered) + "\n\n";
        answer_result_line2.setText(contentStr);
        contentStr = getString(R.string.stats_rate_str, rate);
        answer_result_line3.setText(contentStr);
    }
}
