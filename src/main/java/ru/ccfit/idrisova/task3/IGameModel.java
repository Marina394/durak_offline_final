package ru.ccfit.idrisova.task3;

import java.awt.event.KeyEvent;
import java.util.Iterator;

public interface IGameModel {
    public int deckSize();
    public CardSuit trump();
    public int numberOfGamers();
    public Iterator<Gamer> firstGamer();
    public Iterator<Card> firstAttackCard();
    public Iterator<Card> firstFightCard();
    public void startGame();
    public void nextStep();
    public void gameAction(KeyEvent e);
}
