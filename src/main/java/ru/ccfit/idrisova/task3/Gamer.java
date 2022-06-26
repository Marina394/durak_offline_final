package ru.ccfit.idrisova.task3;

import java.util.Iterator;

public interface Gamer {
    public Card fightBack(Card card, CardSuit trump);
    public Card attack(int indexCard, CardSuit trump);
    public void takeCard(Card card);
    public int numberOfCards();
    public Iterator<Card> firstCard();
}
