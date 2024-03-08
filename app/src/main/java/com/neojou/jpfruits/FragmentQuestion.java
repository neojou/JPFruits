package com.neojou.jpfruits;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
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
import androidx.fragment.app.Fragment;

import com.neojou.jpfruits.databinding.FragmentQuestionBinding;


import java.util.ArrayList;
import java.util.Collections;

public class FragmentQuestion extends Fragment
{
    private static final String TAG="JPFruits:FragmentQuestion";

    final private FruitDataViewModel fruit_dvm;

    FragmentQuestionBinding binding;
    ImageView question_image;
    TextView answer_result;
    RadioGroup choice_rd;
    RadioButton[] choice_rb = new RadioButton[4];
    String[] choices = new String[4];

    Question cur_question;
    int cur_question_id;

    int total_answered;
    int correct_answered;
    int wrong_answered;

    public FragmentQuestion(FruitDataViewModel dvm)
    {
        super(R.layout.fragment_question);

        fruit_dvm = dvm;
        cur_question_id = 0;
        fruit_dvm.shuffle();
        stats_set_start();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false);
        setViewItemsBinding();
        binding.setFruitDvm(fruit_dvm);
        return binding.getRoot();
    }

    private void setViewItemsBinding() {
        question_image = binding.questionImage;
        answer_result = binding.answerResult;
        choice_rd = binding.choices;
        choice_rb[0] = binding.choice1;
        choice_rb[1] = binding.choice2;
        choice_rb[2] = binding.choice3;
        choice_rb[3] = binding.choice4;
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
            Log.e(TAG, "cur_question is null");
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
        else if (id == R.id.choice4)
            answer = 4;

        set_right_choice_button(cur_question.right_choice);
        answer_result.setVisibility(View.VISIBLE);
        if (answer != cur_question.right_choice) {
            String str = getString(R.string.choice_title) + " : " +
                    getString(R.string.wrong);
            answer_result.setText(str);
            set_wrong_choice_button(answer);
            stats_add_if_answered_correct(false);
        } else {
            String str = getString(R.string.choice_title) + " : " +
                    getString(R.string.correct);
            answer_result.setText(str);
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
        if (fruit_array_temp.size() < 5) {
            Log.e(TAG,"generate_question(): fruit array size is "
                    + fruit_array_temp.size() + "less then 5");
            return null;
        }
        Collections.shuffle(fruit_array_temp);
        fruit_array_temp = new ArrayList<>(fruit_array_temp.subList(0, 5));
        int correct_answer = (int)(Math.random()*(4-1+1) + 1);
        String[] choice_str = new String[4];
        int other_answer_id = 0;
        for (int i = 0; i < 4; i++) {
            if (i == correct_answer - 1) {
                choice_str[i] = jp_name;
                continue;
            }
            if (jp_name.equals(fruit_array_temp.get(other_answer_id))) {
                //Log.i(TAG, "found the same in choices");
                other_answer_id++;
            }
            if (other_answer_id >= 5) {
                Log.e(TAG,"generate_question(): other_answer_id = "
                        + other_answer_id + ">= 5");
                return null;
            }
            choice_str[i] = fruit_array_temp.get(other_answer_id);
            other_answer_id++;
        }

        return new Question(fruit_name, choice_str[0], choice_str[1], choice_str[2], choice_str[3], correct_answer);
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

    private void set_question_to_view(Question q) {
        if (q == null) {
            Log.e(TAG, "set_question_to_view() : input Question is null");
            return;
        }
        //Log.d(TAG, "set_question_to_view");

        // question_image
        Activity activity = getActivity();
        if (activity == null) {
            Log.e(TAG, "set_question_to_view() : getActivity() is null");
            return;
        }
        int id = activity.getResources().getIdentifier(q.img_filename, "drawable", activity.getPackageName());
        question_image.setImageResource(id);
        question_image.setScaleType(ImageView.ScaleType.FIT_XY);
        question_image.setVisibility(View.VISIBLE);

        answer_result.setVisibility(View.INVISIBLE);
        choice_rd.setVisibility(View.VISIBLE);
        choices[0] = q.choice1;
        choices[1] = q.choice2;
        choices[2] = q.choice3;
        choices[3] = q.choice4;
        choice_rb[0].setText(choices[0]);
        choice_rb[1].setText(choices[1]);
        choice_rb[2].setText(choices[2]);
        choice_rb[3].setText(choices[3]);
        set_radio_button_color(choice_rb[0], R.color.gray);
        set_radio_button_color(choice_rb[1], R.color.gray);
        set_radio_button_color(choice_rb[2], R.color.gray);
        set_radio_button_color(choice_rb[3], R.color.gray);
        choice_rb[0].setChecked(false);
        choice_rb[1].setChecked(false);
        choice_rb[2].setChecked(false);
        choice_rb[3].setChecked(false);
        choice_rd.clearCheck();
    }

    private void set_radio_button_color(RadioButton rb, int color) {
        Context mContext = getContext();
        if (mContext != null)
            rb.setTextColor(ContextCompat.getColorStateList(mContext, color));
    }

    private void set_right_choice_button(int which) {
        int id = which - 1;
        if (id < 0 || id > 3) return;
        set_radio_button_color(choice_rb[id], R.color.blue);
        choice_rb[id].setChecked(true);
    }

    private void set_wrong_choice_button(int which) {
        int id = which - 1;
        if (id < 0 || id > 3) return;
        set_radio_button_color(choice_rb[id], R.color.fuchsia);
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
        answer_result.setVisibility(View.VISIBLE);
        answer_result.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        choice_rd.setVisibility(View.INVISIBLE);

        int rate = 0;
        if (total_answered != 0)
            rate = correct_answered * 100 / total_answered;

        String contentStr = getString(R.string.stats_total_str, total_answered) + "\n\n";
        contentStr += getString(R.string.stats_each_str, correct_answered, wrong_answered) + "\n\n";
        contentStr += getString(R.string.stats_rate_str, rate);
        answer_result.setText(contentStr);
    }
}
