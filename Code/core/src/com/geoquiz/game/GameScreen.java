package com.geoquiz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameScreen implements Screen {

    private final GeoQuiz game;
    private Stage stage;
    private int localScore;
    private Quiz round;
    private int delay;
    private int updateScore;
    private int roundCount;
    private boolean fromLan;
    private boolean isHost;
    private boolean sendFin;
    private int[] nums;
    private Label.LabelStyle style;
    private Label stats;

    public GameScreen(GeoQuiz game, boolean fromLan, boolean isHost, int[] ms) {
        localScore = 0;
        this.game = game;
        stage = new Stage();
        roundCount = 0;
        sendFin = true;

        delay = 0;
        updateScore = 0;

        this.fromLan = fromLan;
        this.isHost = isHost;
        if(fromLan) {
            style = new Label.LabelStyle();
            style.font = game.font;
            if(isHost) {
                stats = new Label(game.player.name + " " + localScore + " : " + game.server.opponentScore + " " + game.server.opponentName, style);
            }else {
                stats = new Label(game.player.name + " " + localScore + " : " + game.client.opponentScore + " " +game.client.opponentName, style);
            }
            stats.setPosition(Gdx.graphics.getWidth()/2-stats.getPrefWidth()/2,Gdx.graphics.getHeight()-300);
            stage.addActor(stats);
        }
        nums = ms;
        Question question = game.questionList.get(nums[0]);
        round = new Quiz(question, game, localScore, stage, this.fromLan);
        Gdx.input.setInputProcessor(stage);
    }

    @Override

    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(255, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(fromLan) {
            updateScore++;
            if (updateScore > 100) {
                updateScore = 0;
                stats.setText("");
                if (isHost) {
                    nums = game.server.nums;
                    stats = new Label(game.player.name + " " + localScore + " : " + game.server.opponentScore + " " + game.server.opponentName, style);
                } else {
                    nums = game.client.nums;
                    stats = new Label(game.player.name + " " + localScore + " : " + game.client.opponentScore + " " + game.client.opponentName, style);

                }
                stats.setPosition(Gdx.graphics.getWidth()/2-stats.getPrefWidth()/2,Gdx.graphics.getHeight()-300);
                stage.addActor(stats);
            }
        }
        if(round.close && roundCount < 5) {
            delay++;
            if(round.score) {
            localScore++;
            round.score=false;
            }
        }
        if(delay>100){
            if(fromLan) {
                if(isHost) {
                    game.server.sendScore(localScore);
                } else {
                    game.client.sendScore(localScore);
                }
            }
            stage.clear();
            delay = 0;
            roundCount++;
            if(roundCount<5) {
                Question question = game.questionList.get(nums[roundCount]);
                round = new Quiz(question, game, localScore, stage, this.fromLan);
            }
        }
        if (roundCount==5 && sendFin) {
            if(fromLan) {
                if(isHost) {
                    game.server.sendFinish();
                } else {
                    game.client.sendFinish();
                }
                sendFin = false;
            }else{
                game.player.changeStat(localScore);
                if(localScore==5) {
                    game.setScreen(new WinScreen(game, localScore,fromLan, isHost));
                }else{
                    game.setScreen(new LooseScreen(game, localScore, fromLan, isHost));
                }
            }
        }
        if(fromLan) {
            if(isHost) {
                if(roundCount==5 && game.server.opponentFinish) {
                    choose(game.server.opponentScore);
                }
            } else {
                if(roundCount==5 && game.client.opponentFinish) {
                    choose(game.client.opponentScore);
                }
            }
        }

        round.draw(stage);

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

    public void choose(int opponentScore) {
        if(localScore > opponentScore) {
            game.setScreen(new WinScreen(game, localScore, fromLan, isHost));
        }else if(localScore < opponentScore) {
            game.setScreen(new LooseScreen(game, localScore, fromLan, isHost));
        }else {
            game.setScreen(new DrawScreen(game, localScore, isHost));
        }
    }
}
