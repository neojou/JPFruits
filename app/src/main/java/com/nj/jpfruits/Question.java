package com.nj.jpfruits;

public class Question {
    public String img_filename;
    public String choice1;
    public String choice2;
    public String choice3;
    public String choice4;

    public int right_choice;

    public Question(String filename, String choice1, String choice2, String choice3, String choice4, int correct_answer) {
        this.img_filename = filename;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.right_choice = correct_answer;
    }
}
