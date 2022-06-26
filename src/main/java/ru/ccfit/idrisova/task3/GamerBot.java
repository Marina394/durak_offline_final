package ru.ccfit.idrisova.task3;

import java.util.ArrayList;
import java.util.Iterator;

public class GamerBot implements Gamer{
    final private boolean showCards;
    final private ArrayList<Card> cards;
     private ArrayList<Card> shirts;

    GamerBot(boolean showCards){
        this.showCards = showCards;
        cards = new ArrayList<>();
    }
    public void takeCard(Card card){
        card.setCardStatus(CardStatus.inGamerDeck);
        cards.add(card);
    }
    private Card getSmallestCard(Card card1, Card card2, CardSuit trump){
        if(card1.getCardSuit() != trump){
            if(card2.getCardSuit() == trump)
                return card1;
            else
                return card2.getCardValue().ordinal() < card1.getCardValue().ordinal() ? card2 : card1;
        }
        else{
            if(card2.getCardSuit() == trump && card2.getCardValue().ordinal() > card1.getCardValue().ordinal())
                return card1;
            return card2;
        }
    }
    public Card fightBack(Card card, CardSuit trump){
        Card cardFightBack = cards.get(0);
        boolean isFight = false;
        for(int i = 0; i < cards.size(); ++i){
            if(cards.get(i).getCardSuit() == card.getCardSuit()){
                if(cards.get(i).getCardValue().ordinal() > card.getCardValue().ordinal()){
                    cardFightBack = isFight ? getSmallestCard(cardFightBack, cards.get(i), trump) : cards.get(i);
                    isFight = true;
                }
            }
            else{
                if(cards.get(i).getCardSuit() == trump){
                    cardFightBack = isFight ? getSmallestCard(cardFightBack, cards.get(i), trump) : cards.get(i);
                    isFight = true;
                }
            }
        }
        if(isFight) {
            cards.remove(cardFightBack);
            cardFightBack.setCardStatus(CardStatus.fightBack);
        }
        return isFight ? cardFightBack : null;
    }

    public Card attack(int indexCard, CardSuit trump){
        if(0 == cards.size())
            return null;

        Card card = cards.get(0);
        Iterator<Card> it = cards.iterator();

        while(it.hasNext()){
            card = getSmallestCard(card, it.next(), trump);
        }
        card.setCardStatus(CardStatus.attack);
        cards.remove(card);
        return card;
    }
    public int numberOfCards() {
        return cards.size();
    }
    public Iterator<Card> firstCard() {
        if(!showCards) {
            shirts = new ArrayList<>();
            for(int j1 = 0; j1 < cards.size(); ++j1)
                shirts.add(null);
            return shirts.iterator();
        }
        return cards.iterator();
    }
}
