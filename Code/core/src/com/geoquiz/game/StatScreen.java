package com.geoquiz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StatScreen implements Screen {
    private final GeoQuiz game;
    private Array<Label> labelArray;
    private Stage stage;


    public StatScreen(final GeoQuiz game, final boolean fromLan, final boolean isHost) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        labelArray = new Array<Label>();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.font;

        Label statistics = new Label("Statistics", style);
        statistics.setFontScale(3f, 3f);
        statistics.setPosition(Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() - 100);
        stage.addActor(statistics);

        if (fromLan) {
            /*if (isHost) {
                for (int i = 0; i < 6; i++) {
                    if (game.server.getPlayer().bestTimes[i] < 0) {
                        Label label1 = new Label("Best time:  N/A", style);
                        label1.setFontScale(1.5f, 1.5f);
                        Label label2 = new Label("Average time:  N/A", style);
                        label2.setFontScale(1.5f, 1.5f);
                        labelArray.add(label1);
                        labelArray.add(label2);
                    } else {
                        Label label1 = new Label("Best time:  " + Float.toString(game.server.getPlayer().bestTimes[i]).replace('.', ':'), style);
                        label1.setFontScale(1.5f, 1.5f);
                        Label label2 = new Label("Average time:  " + Float.toString(game.server.getPlayer().avgTimes[i]).replace('.', ':'), style);
                        label2.setFontScale(1.5f, 1.5f);
                        labelArray.add(label1);
                        labelArray.add(label2);
                    }
                }
                Label label1 = new Label("Winrate:  " + game.server.getPlayer().calcWinrate() + "%", style);
                label1.setFontScale(1.5f, 1.5f);
                Label label2 = new Label("Wins  :  " + game.server.getPlayer().lanWins + "    Losts  :  " + game.server.getPlayer().lanLosts, style);
                label2.setFontScale(1.5f, 1.5f);
                labelArray.add(label1);
                labelArray.add(label2);
            } else {
                for (int i = 0; i < 6; i++) {
                    if (game.client.getPlayer().bestTimes[i] < 0) {
                        Label label1 = new Label("Best time:  N/A", style);
                        label1.setFontScale(1.5f, 1.5f);
                        Label label2 = new Label("Average time:  N/A", style);
                        label2.setFontScale(1.5f, 1.5f);
                        labelArray.add(label1);
                        labelArray.add(label2);
                    } else {
                        Label label1 = new Label("Best time:  " + Float.toString(game.client.getPlayer().bestTimes[i]).replace('.', ':'), style);
                        label1.setFontScale(1.5f, 1.5f);
                        Label label2 = new Label("Average time:  " + Float.toString(game.client.getPlayer().avgTimes[i]).replace('.', ':'), style);
                        label2.setFontScale(1.5f, 1.5f);
                        labelArray.add(label1);
                        labelArray.add(label2);
                    }
                }
                Label label1 = new Label("Winrate:  " + game.client.getPlayer().calcWinrate() + "%", style);
                label1.setFontScale(1.5f, 1.5f);
                Label label2 = new Label("Wins  :  " + game.client.getPlayer().lanWins + "    Losts  :  " + game.client.getPlayer().lanLosts, style);
                label2.setFontScale(1.5f, 1.5f);
                labelArray.add(label1);
                labelArray.add(label2);
            }*/
        } else {
            if (game.player.totalScore < 0) {
                    Label label1 = new Label("Total score:  N/A", style);
                    label1.setFontScale(1.5f, 1.5f);
                    labelArray.add(label1);
            } else {
                    Label label1 = new Label("Total score:  " + Integer.toString(game.player.totalScore), style);
                    label1.setFontScale(1.5f, 1.5f);
                    labelArray.add(label1);
            }
            Label label2 = new Label("Wins  :  " + game.player.lanWins, style);
            label2.setFontScale(1.5f, 1.5f);
            Label label3 = new Label("Looses  :  " + game.player.lanLoose, style);
            label3.setFontScale(1.5f, 1.5f);
            labelArray.add(label2);
            labelArray.add(label3);
        }
        for (int i = 0; i < 3; i++) {
                labelArray.get(i).setPosition(400, Gdx.graphics.getHeight() + 130 - ((i + 2) * Gdx.graphics.getHeight() / 10));
        }

        for (int i = 0; i < 3; i++) {
            stage.addActor(labelArray.get(i));
        }

        TextButton textButton = new TextButton("back", game.buttonStyles.shortButtonStyle);
        textButton.setWidth(400);
        textButton.setHeight(250);
        textButton.getLabel().setFontScale(2f, 2f);
        textButton.setPosition(Gdx.graphics.getWidth() / 2 - 200, 50);

        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (fromLan) {
                    if (isHost) {
                        /*game.setScreen(new DifficultyMenu(game, game.server.diff, fromLan, isHost));*/
                    } else {
                        /*game.setScreen(new DifficultyMenu(game, 3, fromLan, isHost));*/
                    }
                } else
                    game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(textButton);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
