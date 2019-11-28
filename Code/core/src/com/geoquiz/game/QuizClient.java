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
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;

public class QuizClient {
    public Client client;
    public Kryo kryo;
    public boolean connected;
    public boolean serverStop;
    public boolean opponentReady;
    public boolean opponentGivedUp;
    public boolean restartTimer;
    public String yourName;
    public String opponentName;
    public int[] nums;
    public int opponentScore;
    public boolean opponentFinish;

    QuizClient(String name)throws IOException{
        client = new Client();
        yourName = name;
        start();
    }

    public void start(){
        client.start();
    }

    public void connect(String adress)throws IOException{
        kryo = client.getKryo();
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

        opponentScore = 0;
        nums = new int[5];

        InetAddress address = client.discoverHost(54777, 1000);
        client.connect(1000, address.toString().replace("/", ""), 54555, 54777);
        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof NamePacket) {
                    NamePacket oppNamePacket = (NamePacket)object;
                    opponentName = oppNamePacket.name;
                    NamePacket name = new NamePacket();
                    name.name = yourName;
                    client.sendTCP(name);
                }
                if (object instanceof AcceptPacket) {
                    connected = true;
                }
                if (object instanceof ServerStopPacket) {
                    serverStop = true;
                }
                if (object instanceof ReadyPacket){
                    opponentReady = true;
                }
                if (object instanceof UnreadyPacket){
                    opponentReady = false;
                    restartTimer = true;
                }
                if (object instanceof NumsPacket) {
                    NumsPacket numsPacket = (NumsPacket)object;
                    nums = numsPacket.ms;
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
        client.sendTCP(giveUp);
    }*/

    public void ready(){
        ReadyPacket ready = new ReadyPacket();
        client.sendTCP(ready);
    }

    public void unready(){
        UnreadyPacket unready = new UnreadyPacket();
        client.sendTCP(unready);
    }

    public void sendRequest(){
        RequestPacket req = new RequestPacket();
        client.sendTCP(req);
    }

    public void connect(){
        ConnectPacket connect = new ConnectPacket();
        client.sendTCP(connect);
    }

    public void disconnect(){
        DisconnectPacket disconnect = new DisconnectPacket();
        client.sendTCP(disconnect);
    }

    public void sendScore(int score){
        ScorePacket scorePacket = new ScorePacket();
        scorePacket.score = score;
        client.sendTCP(scorePacket);
    }

    public void sendFinish(){
        SendResultPacket finish = new SendResultPacket();
        client.sendTCP(finish);
    }
}

