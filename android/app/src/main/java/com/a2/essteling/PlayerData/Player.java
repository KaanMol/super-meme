package com.a2.essteling.PlayerData;

import android.content.Context;

import com.a2.essteling.R;
import com.a2.essteling.PlayerData.History;
import com.a2.essteling.PlayerData.HistoryList;
import com.a2.essteling.PlayerData.HistoryListener;

import java.util.LinkedList;

public class Player implements HistoryListener {
    private String name;
    private LinkedList<History> gameHistorie;
    private String cardId;
    private int colorId;
    private HistoryList historyListRequest;

    public Player(String name, LinkedList<History> gameHistorie) {
        this.name = name;
        this.gameHistorie = gameHistorie;
        this.colorId = R.color.Red;
    }

    public Player(String name, String cardId, Context context){
        this.name = name;
        this.cardId = cardId;

        historyListRequest = new HistoryList(cardId, this, context);

        this.gameHistorie = new LinkedList<>();
        this.colorId = R.color.Red;


    }

    public void updateHistoryRequest(Context context) {
        historyListRequest = new HistoryList(cardId, this, context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<History> getGameHistorie() {
        return gameHistorie;
    }

    public void setGameHistorie(LinkedList<History> gameHistorie) {
        this.gameHistorie = gameHistorie;
    }

    @Override
    public void onHistoryReceived(LinkedList<History> histories) {
        this.gameHistorie = histories;
    }

    public int getColor() {
        return this.colorId;
    }

    public void setColor(int colorId){
        this.colorId = colorId;
    }
}

