package com.adnan.lgstudies.Adapters;

public class QuestionsAndAnswer {
    String question, optionA, optionB, optionC, optionD, correctAnswer,question_image;

    public QuestionsAndAnswer() {
    }

    public QuestionsAndAnswer(String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer, String question_image) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.question_image = question_image;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestion_image() {
        return question_image;
    }
}