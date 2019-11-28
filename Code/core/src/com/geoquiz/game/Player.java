package com.geoquiz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;

public class Player {

    public String name;
    public int totalScore;
    public int lanWins;
    public int lanLoose;

    Player(){
        loadFile();
    }

    public void setName(String name){
        this.name = name;
    }

    public void loadFile(){
        boolean exists = Gdx.files.local("stats.txt").exists();
        if(!exists){
            createFile();
        }
        FileHandle file = Gdx.files.local("stats.txt");
        String text = file.readString();
        splitString(text);
    }

    public void splitString(String text){
        String[] parts = text.split("~");
        name = parts[0];
        totalScore = Integer.valueOf(parts[1]);
        lanWins = Integer.valueOf(parts[2]);
        lanLoose = Integer.valueOf(parts[3]);
    }

    public String createString(){
        StringBuilder str= new StringBuilder();
        str.append(name);
        str.append("~");
        str.append(totalScore);
        str.append("~");
        str.append(lanWins);
        str.append("~");
        str.append(lanLoose);
        return str.toString();
    }

    public void saveFile(){
        FileHandle file = Gdx.files.local("stats.txt");
        file.writeString(createString(), false);
    }

    public void changeStat(int addScore){
        if(totalScore<-1 && addScore >0) {
            totalScore++;
        }
        totalScore+=addScore;
        saveFile();
    }

    public void createFile(){
        FileHandle file = Gdx.files.local("stats.txt");
        file.writeString("sashaSHS~-1~0~0", false);
    }
}
