package com.geoquiz.game;

import com.geoquiz.game.packets.AcceptPacket;
import com.geoquiz.game.packets.ConnectPacket;
import com.geoquiz.game.packets.DisconnectPacket;
import com.geoquiz.game.packets.SendResultPacket;
import com.geoquiz.game.packets.NamePacket;
import com.geoquiz.game.packets.NumsPacket;
import com.geoquiz.game.packets.ReadyPacket;
import com.geoquiz.game.packets.RequestPacket;
import com.geoquiz.game.packets.ScorePacket;
import com.geoquiz.game.packets.ServerStopPacket;
import com.geoquiz.game.packets.UnreadyPacket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class QuizServer {
    public Server server;
    public boolean isTime;
    public boolean isPlayer;
    public boolean sendPlayer;
    public Kryo kryo;
    public boolean opponentConnected;
    public boolean opponentReady;
    public boolean opponentGivedUp;
    public boolean restartTimer;
    public String yourName;
    public String opponentName;
    public int[] nums;
    public int opponentScore;
    public boolean opponentFinish;

    QuizServer(String name) throws IOException {
        server = new Server();
        server.start();

        kryo = server.getKryo();
        kryo.register(RequestPacket.class);
        kryo.register(NamePacket.class);
        kryo.register(AcceptPacket.class);
        kryo.register(ConnectPacket.class);
        kryo.register(DisconnectPacket.class);
        kryo.register(ReadyPacket.class);
        kryo.register(UnreadyPacket.class);
        kryo.register(ServerStopPacket.class);
        kryo.register(NumsPacket.class);
        kryo.register(ScorePacket.class);
        kryo.register(SendResultPacket.class);
        kryo.register(int[].class);


        yourName = name;
        opponentScore = 0;
        nums = new int[5];


        server.bind(54555, 54777);
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ReadyPacket){
                    opponentReady = true;
                }
                if (object instanceof UnreadyPacket){
                    opponentReady = false;
                    restartTimer = true;
                }
                if (object instanceof RequestPacket) {
                    NamePacket namePacket = new NamePacket();
                    namePacket.name = yourName;
                    server.sendToAllTCP(namePacket);
                }
                if (object instanceof NamePacket) {
                    NamePacket oppNamePacket = (NamePacket)object;
                    opponentName = oppNamePacket.name;
                    AcceptPacket accept = new AcceptPacket();
                    server.sendToAllTCP(accept);
                }
                if(object instanceof ConnectPacket) {
                    opponentConnected = true;
                }
                if(object instanceof DisconnectPacket) {
                    opponentConnected = false;
                }
                if(object instanceof ScorePacket) {
                    ScorePacket scorePacket = (ScorePacket)object;
                    opponentScore = scorePacket.score;
                }
                if(object instanceof SendResultPacket) {
                    opponentFinish = true;
                }
            }
        });
    }

    /*public void giveUp(){
        GiveUpPacket giveUp = new GiveUpPacket();
        server.sendToAllTCP(giveUp);
    }*/

    public void sendNums(int[] ms) {
        NumsPacket numsPacket = new NumsPacket();
        numsPacket.ms = ms;
        server.sendToAllTCP(numsPacket);
        this.nums = ms;
    }

    public void ready(){
        ReadyPacket ready = new ReadyPacket();
        server.sendToAllTCP(ready);
    }

    public void unready(){
        UnreadyPacket unready = new UnreadyPacket();
        server.sendToAllTCP(unready);
    }

    public void serverStop(){
        ServerStopPacket stop = new ServerStopPacket();
        server.sendToAllTCP(stop);
        server.close();
    }

    public void sendScore(int score){
        ScorePacket scorePacket = new ScorePacket();
        scorePacket.score = score;
        server.sendToAllTCP(scorePacket);
    }

    public void sendFinish(){
        SendResultPacket finish = new SendResultPacket();
        server.sendToAllTCP(finish);
    }
}

