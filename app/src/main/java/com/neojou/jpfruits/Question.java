package com.neojou.jpfruits;

@startuml
package com.neojou.jpfruits {
    class Question {
        - final String img_filename
        - final String choice1
        - final String choice2
        - final String choice3
        - final int right_choice
        + Question(String filename, String choice1, String choice2, String choice3, int correct_answer)
    }

    Question o-- String
}
@enduml

public class Question {
    final public String img_filename;
    final public String choice1;
    final public String choice2;
    final public String choice3;

    final public int right_choice;

    public Question(String filename, String choice1, String choice2, String choice3, int correct_answer) {
        this.img_filename = filename;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.right_choice = correct_answer;
    }
}
