package com.adnan.lgstudies.Adapters;
public class UserResponseAndResult {
    private int position;
    private String character;
    private int obtained_mark;

    public UserResponseAndResult(int position, String character, int obtained_mark) {
        this.position = position;
        this.character = character;
        this.obtained_mark = obtained_mark;
    }

    public int getPosition() {
        return position;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getObtained_mark() {
        return obtained_mark;
    }

    public void setObtained_mark(int obtained_mark) {
        this.obtained_mark = obtained_mark;
    }
}
