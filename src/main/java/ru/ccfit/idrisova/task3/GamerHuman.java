package ru.ccfit.idrisova.task3;

import java.util.ArrayList;
import java.util.Iterator;

public class GamerHuman implements Gamer {
    final private ArrayList<Card> cards;

    GamerHuman(){
        cards = new ArrayList<>();
    }
    @Override
    public Card fightBack(Card card, CardSuit trump) {
        return null;
    }
    @Override
    public Card attack(int idx, CardSuit trump) {
        Card card = null;
        if((0 <= idx) && (idx < cards.size())){
            card = cards.get(idx);
            cards.remove(idx);
        }
        return card;
    }
    @Override
    public void takeCard(Card card) {
        cards.add(card);
    }
    @Override
    public int numberOfCards() {
        return null == cards ? 0 : cards.size();
    }
    @Override
    public Iterator<Card> firstCard() {
        return null == cards ? null : cards.iterator();
    }
}
