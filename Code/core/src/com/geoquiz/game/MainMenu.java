package com.geoquiz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

public class MainMenu implements Screen {
    private final GeoQuiz game;
    private Array<TextButton> textButtonArray;
    private Stage stage;

    public MainMenu(final GeoQuiz game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        textButtonArray = new Array<TextButton>();

        TextButton singleButton = new TextButton("Single Quiz", game.buttonStyles.longButtonStyle);
        singleButton.setWidth(1000);
        singleButton.setHeight(300);
        singleButton.getLabel().setFontScale(2f, 2f);
        singleButton.setPosition(40, 940);
        textButtonArray.add(singleButton);

        TextButton lanButton = new TextButton("Quiz with opponent", game.buttonStyles.longButtonStyle);
        lanButton.setWidth(1000);
        lanButton.setHeight(300);
        lanButton.getLabel().setFontScale(2f, 2f);
        lanButton.setPosition(40, 640);
        textButtonArray.add(lanButton);

        TextButton statButton = new TextButton("Statistics", game.buttonStyles.longButtonStyle);
        statButton.setWidth(1000);
        statButton.setHeight(300);
        statButton.getLabel().setFontScale(2f, 2f);
        statButton.setPosition(40, 340);
        textButtonArray.add(statButton);

        TextButton exitButton = new TextButton("Exit", game.buttonStyles.longButtonStyle);
        exitButton.setWidth(1000);
        exitButton.setHeight(300);
        exitButton.getLabel().setFontScale(2f, 2f);
        exitButton.setPosition(40, 40);
        textButtonArray.add(exitButton);

        textButtonArray.get(0).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                switchScreen(game, new GameScreen(game, false, false, game.generateRandMs()));
            }
        });
        textButtonArray.get(1).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                switchScreen(game, new MultMenu(game));
            }
        });
        textButtonArray.get(2).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switchScreen(game, new StatScreen(game, false, false));
            }
        });
        textButtonArray.get(3).addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        Pixmap labelColor = new Pixmap(200, 100, Pixmap.Format.RGB888);
        labelColor.setColor(Color.LIGHT_GRAY);
        labelColor.fill();

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.fontColor = Color.CHARTREUSE;
        textFieldStyle.font = game.font;
        textFieldStyle.font.getData().setScale(2f,2f);
        textFieldStyle.background = new Image(new Texture(labelColor)).getDrawable();

        final TextField textField = new TextField(game.player.name, textFieldStyle);
        textField.setHeight(115);
        textField.setWidth(400);
        textField.setMaxLength(15);
        textField.setPosition(40, 1465);

        stage.addActor(textField);

        TextButton changeName = new TextButton("Change name", game.buttonStyles.longButtonStyle);
        changeName.setWidth(400);
        changeName.setHeight(150);
        changeName.getLabel().setFontScale(2f, 2f);
        changeName.setPosition(420, 1450);
        changeName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!textField.getText().equals(""))
                    game.player.setName(textField.getText());
                game.player.saveFile();
            }
        });
        stage.addActor(changeName);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = game.font;
        Label name = new Label("Welcome to the GeoQuiz,", style);
        name.setPosition(40, 1600);
        name.setFontScale(2.f, 2.f);
        stage.addActor(name);

        for(int i = 0; i< 4; i++) {
            stage.addActor(textButtonArray.get(i));
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    public void switchScreen(final GeoQuiz game, final Screen newScreen) {
        stage.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(fadeOut(0.3f));
        sequenceAction.addAction(run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(newScreen);
            }
        }));
        stage.getRoot().addAction(sequenceAction);
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
