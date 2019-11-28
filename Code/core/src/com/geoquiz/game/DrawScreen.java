package com.geoquiz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class DrawScreen implements Screen {
    final private GeoQuiz game;
    Stage stage;
    Label.LabelStyle style;
    Label result;
    TextButton leave;
    TextButton restart;
    boolean isHost;


    public DrawScreen(final GeoQuiz game, int yourScore, final boolean isHost) {
        this.game = game;
        stage = new Stage();
        this.isHost = isHost;

        style = new Label.LabelStyle();
        style.font = game.font;

        if(isHost) {
            result = new Label("Draw!\n   " + yourScore + " : " + game.server.opponentScore, style);
        }else {
            result = new Label("Draw!\n   " + yourScore + " : " + game.client.opponentScore, style);
        }
        result.setPosition(Gdx.graphics.getWidth()/2-result.getPrefWidth()/2,Gdx.graphics.getHeight()/4*3);
        stage.addActor(result);
        leave = new TextButton("leave", game.buttonStyles.shortButtonStyle);
        leave.setWidth(400);
        leave.setHeight(250);
        leave.getLabel().setFontScale(2f, 2f);
        restart = new TextButton("restart", game.buttonStyles.shortButtonStyle);
        restart.setWidth(400);
        restart.setHeight(250);
        restart.getLabel().setFontScale(2f, 2f);
        leave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isHost) {
                    game.server.serverStop();
                } else {
                    game.client.disconnect();
                    game.client.client.stop();
                    game.client.start();
                }
                game.setScreen(new MultMenu(game));
            }
        });
        restart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isHost) {
                    game.server.opponentReady = false;
                } else {
                    game.client.opponentReady = false;
                }
                game.setScreen(new WaitRoom(game, isHost));
            }
        });
        leave.setPosition(Gdx.graphics.getWidth() / 2 - 500, 50);
        restart.setPosition(Gdx.graphics.getWidth() / 2 + 100, 50);

        stage.addActor(leave);
        stage.addActor(restart);
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

    }
}
