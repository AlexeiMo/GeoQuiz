package com.geoquiz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
public class Quiz {
    private Question question;
    private Array<TextButton> answers;
    private TextButton.TextButtonStyle right;
    private TextButton.TextButtonStyle wrong;
    public boolean close;
    public boolean score;


    Quiz(final Question question, GeoQuiz game, int curScore, Stage stage, boolean fromLan){
        right = game.buttonStyles.rightButtonStyle;
        wrong = game.buttonStyles.wrongButtonStyle;
        answers = new Array<TextButton>();
        this.question = question;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font;
        Label ques = new Label(question.question, labelStyle);
        ques.setPosition(Gdx.graphics.getWidth()/2-ques.getPrefWidth()/2, Gdx.graphics.getHeight()/2+300);
        stage.addActor(ques);
        if(!fromLan) {
            Label scoreLabel = new Label("Score: " + curScore, labelStyle);
            scoreLabel.setPosition(Gdx.graphics.getWidth() / 2 - scoreLabel.getPrefWidth() / 2, Gdx.graphics.getHeight() - 300);
            stage.addActor(scoreLabel);
        }
        for (int i = 0; i < 4; i++) {
            final TextButton answer = new TextButton(question.answers[i], game.buttonStyles.longButtonStyle);
            answer.setPosition(100,Gdx.graphics.getHeight()/2-100-i*300);
            answer.setWidth(Gdx.graphics.getWidth()-200);
            answer.setHeight(200);
            answers.add(answer);
            answer.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(answer.getLabel().toString().split("Label: ")[1].equals(question.rightAns)) {
                        answer.setStyle(right);
                        score = true;
                    }
                    else{
                        answer.setStyle(wrong);
                        for (int j = 0; j < 4; j++) {
                            if(answers.get(j).getLabel().toString().split("Label: ")[1].equals(question.rightAns)) {
                                answers.get(j).setStyle(right);
                            }
                        }
                    }
                    close = true;
                    disable();
                }
            });
            stage.addActor(answer);
        }
        Gdx.input.setInputProcessor(stage);
    }

    public void disable(){
        for (int i = 0; i < 4; i++)
            answers.get(i).setTouchable(Touchable.disabled);
    }

    public void draw(Stage stage){
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

}