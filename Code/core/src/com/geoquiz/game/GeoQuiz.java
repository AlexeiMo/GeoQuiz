package com.geoquiz.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeoQuiz extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	public Skin skin;
	public TextureAtlas atlas;
	public ButtonStyles buttonStyles;
	public Player player;
	public QuizClient client;
	public QuizServer server;
	public List<Question> questionList = new ArrayList<Question>();


	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("font.fnt"));
		atlas = new TextureAtlas(Gdx.files.internal("texture.atlas"));
		skin = new Skin(atlas);
		buttonStyles = new ButtonStyles(this);
		player = new Player();
		try {
			initList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			client = new QuizClient(this.player.name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setScreen(new MainMenu(this));
	}

	public void createServer() throws IOException {
		server = new QuizServer(this.player.name);
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
		skin.dispose();
	}

	public int[] generateRandMs() {
		Random random = new Random(System.currentTimeMillis());
		int[] ms = new int[5];
		boolean flag = true;
		for (int i = 0; i < 5; i++) {
			int num = random.nextInt(20);
			for (int j = 0; j < i+1; j++) {
				if(ms[j] == num) {
					flag = false;
				}
			}
			if(flag) {
				ms[i] = num;
			}else {
				i--;
				flag = true;
			}
		}
		return ms;
	}

	public void initList() throws IOException {
		FileHandle fileHandle = Gdx.files.internal("data/questions.txt");
		BufferedReader reader = fileHandle.reader(1024);
		String text;
		String[] parts;
		Question question;
		String ques;
		String[] answers = new String[4];
		int rightAnsNum;
		while ((text = reader.readLine()) != null) {
			parts = text.split("!");
			ques = parts[0];
			for (int j = 0; j < 4; j++) {
				answers[j] = parts[j+1];
			}
			rightAnsNum = Integer.parseInt(parts[5]);
			question = new Question(ques, answers[rightAnsNum-1], answers);
			questionList.add(question);
		}
	}
}