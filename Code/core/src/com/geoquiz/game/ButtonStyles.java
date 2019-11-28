package com.geoquiz.game;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ButtonStyles {
    public TextButton.TextButtonStyle shortButtonStyle;
    public TextButton.TextButtonStyle longButtonStyle;
    public ImageButton.ImageButtonStyle pressedCircleButton;
    public ImageButton.ImageButtonStyle unpressedCircleButton;
    public TextButton.TextButtonStyle wrongButtonStyle;
    public TextButton.TextButtonStyle rightButtonStyle;

    ButtonStyles(GeoQuiz game){
        shortButtonStyle = new TextButton.TextButtonStyle();
        longButtonStyle = new TextButton.TextButtonStyle();
        pressedCircleButton = new ImageButton.ImageButtonStyle();
        unpressedCircleButton = new ImageButton.ImageButtonStyle();
        wrongButtonStyle = new TextButton.TextButtonStyle();
        rightButtonStyle = new TextButton.TextButtonStyle();

        pressedCircleButton.up = game.skin.getDrawable("pressed");

        unpressedCircleButton.up = game.skin.getDrawable("unpressed");

        longButtonStyle.down = game.skin.getDrawable("longpressed");
        longButtonStyle.over = game.skin.getDrawable("longpressed");
        longButtonStyle.up = game.skin.getDrawable("longunpressed");
        longButtonStyle.font = game.font;

        wrongButtonStyle.up = game.skin.getDrawable("wrongAnswer");
        rightButtonStyle.up = game.skin.getDrawable("rightAnswer");

        wrongButtonStyle.font = game.font;
        rightButtonStyle.font = game.font;

        shortButtonStyle.down = game.skin.getDrawable("shortpressed");
        shortButtonStyle.over = game.skin.getDrawable("shortpressed");
        shortButtonStyle.up = game.skin.getDrawable("shortunpressed");
        shortButtonStyle.font = game.font;
    }
}
