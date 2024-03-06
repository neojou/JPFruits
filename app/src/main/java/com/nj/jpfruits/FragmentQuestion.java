package com.nj.jpfruits;

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

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nj.jpfruits.databinding.FragmentQuestionBinding;


import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
public class FragmentQuestion extends Fragment
{
    private static final String TAG="JPFruits:FragmentQuestion";
    FragmentQuestionBinding binding;
    Question cur_question;
    TextView question_title;
    ImageView question_image;
    TextView question_content;
    TextView choice_title;
    RadioGroup choice_rd;
    RadioButton[] choice_rb = new RadioButton[4];
    String[] choices = new String[4];

    ArrayList<Question> questions;
    int cur_question_id;

    int total_answered;
    int correct_answered;
    int wrong_answered;

    public FragmentQuestion()
    {
        super(R.layout.fragment_question);
        setup_questions();
        stats_set_start();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question, container, false);
        setViewItemsBinding();
        return binding.getRoot();
    }

    private void setViewItemsBinding() {
        question_title = binding.questionTitle;
        question_image = binding.questionImage;
        question_content = binding.questionContent;
        choice_title = binding.choiceTitle;
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
        stats_set_start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        set_next_question();
    }

    private void setup_questions() {
        questions = new ArrayList<Question>();
        Question q;
        //1
        q = new Question("strawberry", "もも", "イチゴ", "リンゴ", "バナナ", 2);
        questions.add(q);
        //2
        q = new Question("orange", "オレンジ", "キウイ", "リンゴ", "スイカ", 1);
        questions.add(q);
        //3
        q = new Question("banana", "リンゴ", "キウイ", "もも", "バナナ", 4);
        questions.add(q);
        //4
        q = new Question("kiwi", "リンゴ", "キウイ", "イチゴ", "バナナ", 2);
        questions.add(q);
        //5
        q = new Question("watermelon", "もも", "キウイ", "スイカ", "イチゴ", 3);
        questions.add(q);
        //6
        q = new Question("grape", "スイカ", "オレンジ", "ブドウ", "イチゴ", 3);
        questions.add(q);
        //7
        q = new Question("apple", "キウイ", "バナナ", "リンゴ", "イチゴ", 3);
        questions.add(q);
        //8
        q = new Question("pineapple", "パイナップル", "イチゴ", "バナナ", "もも", 1);
        questions.add(q);
        //9
        q = new Question("dragonfruit", "スイカ", "ドラゴンフルーツ", "キウイ", "リンゴ", 2);
        questions.add(q);
        //10
        q = new Question("peach", "オレンジ", "スイカ", "リンゴ", "もも", 4);
        questions.add(q);
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
        choice_title.setVisibility(View.VISIBLE);
        if (answer != cur_question.right_choice) {
            choice_title.setText(getString(R.string.choice_title) + " : " +
                    getString(R.string.wrong));
            set_wrong_choice_button(answer);
            stats_add_if_answered_correct(false);
        } else {
            choice_title.setText(getString(R.string.choice_title) + " : " +
                    getString(R.string.correct));
            stats_add_if_answered_correct(true);
        }
    }

    public boolean set_next_question() {
        cur_question = null;
        cur_question_id++;
        if (cur_question_id > questions.size())
            return false;
        cur_question = questions.get(cur_question_id - 1);
        set_question_to_view(cur_question);
        return true;
    }

    public void finish_answer() {
        stats_show();
    }

    private void set_question_to_view(Question q) {
        //Log.d(TAG, "set_question_to_view");

        question_title.setVisibility(View.VISIBLE);
        question_title.setText(q.img_filename);

        // question_image
        Activity activity = getActivity();
        int id = activity.getResources().getIdentifier(q.img_filename, "drawable", activity.getPackageName());
        question_image.setImageResource(id);
        question_image.setScaleType(ImageView.ScaleType.FIT_XY);
        question_image.setVisibility(View.VISIBLE);

        question_content.setVisibility(View.INVISIBLE);
        choice_title.setVisibility(View.INVISIBLE);
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

    private void set_screen_without_question() {
        question_content.setVisibility(View.INVISIBLE);
        choice_title.setVisibility(View.INVISIBLE);
        choice_rd.setVisibility(View.INVISIBLE);
    }

    private void set_screen_with_question() {
        question_content.setVisibility(View.VISIBLE);
        question_content.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        choice_title.setVisibility(View.VISIBLE);
        choice_rd.setVisibility(View.VISIBLE);
    }

    private void stats_set_start() {
        total_answered = 0;
        correct_answered = 0;
        wrong_answered = 0;
        cur_question_id = 0;
    }

    private void stats_add_if_answered_correct(boolean isCorrect) {
        total_answered++;
        if (isCorrect)
            correct_answered++;
        else
            wrong_answered++;
    }

    private void stats_show() {
        question_title.setVisibility(View.INVISIBLE);
        question_image.setVisibility(View.INVISIBLE);
        question_content.setVisibility(View.VISIBLE);
        question_content.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        choice_title.setVisibility(View.VISIBLE);
        choice_rd.setVisibility(View.INVISIBLE);

        //Log.d(TAG, "Total answered =" + Integer.toString(total_answered));
        //Log.d(TAG, "Correct answers =" + Integer.toString(correct_answered));
        //Log.d(TAG, "Wrong answers =" + Integer.toString(wrong_answered));

        int rate = 0;
        if (total_answered != 0)
            rate = correct_answered * 100 / total_answered;
        //Log.d(TAG, "correct rate: " + Integer.toString(rate) +"%");

        String contentStr = getString(R.string.stats_total_str, total_answered) + "\n\n";
        contentStr += getString(R.string.stats_each_str, correct_answered, wrong_answered);
        question_content.setText(contentStr);
        choice_title.setText(getString(R.string.stats_rate_str, rate));
    }
}
