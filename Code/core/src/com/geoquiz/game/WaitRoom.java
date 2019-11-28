package com.geoquiz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class WaitRoom implements Screen {
    private boolean isHost;
    final private GeoQuiz game;
    private Stage stage;
    private TextButton back;
    private TextButton ready;
    private boolean readyState;
    private Timer timer;
    private Label.LabelStyle style;
    private int updateRate;
    private int ms[];

    public WaitRoom(final GeoQuiz game, final boolean isHost) {
        this.game = game;
        this.isHost = isHost;
        stage = new Stage();
        ms = game.generateRandMs();

        style = new Label.LabelStyle();
        style.font = game.font;

        back = new TextButton("Back", game.buttonStyles.shortButtonStyle);
        back.setWidth(450);
        back.setHeight(250);
        back.getLabel().setFontScale(2f, 2f);
        back.setPosition(100, 100);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isHost) {
                    game.server.serverStop();
                } else {
                    game.client.unready();
                    game.client.disconnect();
                    game.client.client.stop();
                    game.client.start();
                }
                game.setScreen(new MultMenu(game));
            }
        });

        ready = new TextButton("Ready", game.buttonStyles.shortButtonStyle);
        ready.setWidth(450);
        ready.setHeight(250);
        ready.getLabel().setFontScale(2f, 2f);
        ready.setPosition(Gdx.graphics.getWidth() - 600, 100);
        ready.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (isHost) {
                    game.server.ready();
                    readyState = true;
                    if (game.server.opponentReady)
                        game.server.sendNums(game.generateRandMs());
                        timer = new Timer(game, 5);
                } else {
                    game.client.ready();
                    readyState = true;
                    if (game.client.opponentReady)
                        timer = new Timer(game, 5);
                }
                ready.setDisabled(true);
            }
        });

        loadStage();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (isHost) {
            if (readyState && game.server.opponentReady) {
                if (timer == null || game.server.restartTimer) {
                    //game.server.sendNums(game.generateRandMs());
                    game.server.restartTimer = false;
                    timer = new Timer(game, 5);
                }
                timer.drawTime();
                if (timer.start)
                    game.setScreen(new GameScreen(game, true, true, ms));
            }
        } else {
            if (readyState && game.client.opponentReady) {
                if (timer == null || game.client.restartTimer) {
                    game.client.restartTimer = false;
                    timer = new Timer(game, 5);
                }
                timer.drawTime();
                if (timer.start)
                    game.setScreen(new GameScreen(game, true, false, ms));
            }
        }
        if (updateRate > 100) {
            loadStage();
        }
        updateRate++;

        if (!isHost && game.client.serverStop) {
            game.client.serverStop = false;
            game.client.client.stop();
            game.client.start();
            game.setScreen(new MultMenu(game));
        }

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

    private void loadStage() {
        updateRate = 0;
        stage.clear();
        Label name = new Label(game.player.name, style);
        name.setPosition(200, 600);

        stage.addActor(name);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = game.skin.getDrawable("unpressed");
        textButtonStyle.checked = game.skin.getDrawable("pressed");
        textButtonStyle.font = game.font;

        TextButton isReady = new TextButton("ready", textButtonStyle);
        isReady.setWidth(100);
        isReady.setHeight(100);
        if (readyState) {
            isReady.setChecked(true);
            if (isHost) {
                game.server.ready();
            } else {
                game.client.ready();
            }
        }
        isReady.setPosition(600, 600);
        isReady.setTouchable(Touchable.disabled);
        stage.addActor(isReady);

        if (isHost) {
            if (game.server.opponentConnected) {
                Label name1 = new Label(game.server.opponentName, style);
                name1.setPosition(200, 500);

                TextButton isReady1 = new TextButton("ready", textButtonStyle);
                isReady1.setPosition(600, 500);
                if (game.server.opponentReady) {
                    isReady1.setChecked(true);
                }
                isReady1.setTouchable(Touchable.disabled);
                isReady1.setWidth(100);
                isReady1.setHeight(100);
                stage.addActor(isReady1);
                stage.addActor(name1);
            }
        } else {
            Label name1 = new Label(game.client.opponentName, style);
            name1.setPosition(200, 500);

            TextButton isReady1 = new TextButton("ready", textButtonStyle);
            isReady1.setPosition(600, 500);
            if (game.client.opponentReady) {
                isReady1.setChecked(true);
            }
            isReady1.setTouchable(Touchable.disabled);
            isReady1.setWidth(100);
            isReady1.setHeight(100);
            stage.addActor(isReady1);
            stage.addActor(name1);

        }
        stage.addActor(back);
        stage.addActor(ready);
    }

}
