package com.geoquiz.game;

public class Question {
    final public String question;
    final public String rightAns;
    final public String[] answers = new String[4];

    public Question(String question, String rightAns, String[] answers) {
        this.question = question;
        this.rightAns = rightAns;
        for (int i = 0; i < this.answers.length; i++) {
            this.answers[i] = answers[i];
        }
    }
}
