package ru.ccfit.idrisova.task3;

import javax.swing.*;
import java.awt.Image;

enum CardSuit{
    DIAMONDS(0),
    HEARTS(1),
    CLUBS(2),
    SPADES(3);

    final private int value;
    CardSuit(int v){
        this.value = v;
    }
    public int v(){
        return this.value;
    }
}

enum CardValue{
    SIX(0),
    SEVEN(1),
    EIGHT(2),
    NIGHT(3),
    TEN(4),
    JACK(5),
    QUEEN(6),
    KING(7),
    ACE(8);

    final private int value;
    CardValue(int v){
        this.value = v;
    }
    public int v(){
        return this.value;
    }
}

enum CardStatus{
    inDeck,
    inGamerDeck,
    attack,
    fightBack,
    remove
}

public class Card {
    private CardSuit suit;
    private CardValue value;
    private CardStatus cardStatus = CardStatus.inDeck;
    final static Image cardShirt = new ImageIcon("src/main/resources/cards/shirt.png").getImage();
    final static Image[] cardSuits = new Image[CardSuit.values().length];
    final static Image[] cardImages = new Image[CardSuit.values().length * CardValue.values().length];
    final static String[] suits = {
            "DIAMONDS", "HEARTS", "CLUBS", "SPADES"
    };


    public Card(CardSuit suit, CardValue value){
        this.suit = suit;
        this.value = value;
    }

    public CardSuit getCardSuit(){
        return  suit;
    }

    public CardValue getCardValue(){
        return value;
    }

    public void setSuitAndValue(CardSuit suit, CardValue value){
        this.suit = suit;
        this.value = value;
    }

    public CardStatus getCardStatus(){
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus){
        this.cardStatus = cardStatus;
    }
    public boolean isFightBack(Card card, CardSuit trump){
        if(trump == this.getCardSuit())
            return (trump != card.getCardSuit()) || (card.getCardValue().v() < this.getCardValue().v());
        return (this.getCardSuit() == card.getCardSuit()) && (card.getCardValue().v() < this.getCardValue().v());
    }

    static public Image getCardShirt(){
        return cardShirt;
    }
    static public Image getCardSuit(CardSuit suit){
        if(null == cardSuits[suit.v()]){
            cardSuits[suit.v()] = new ImageIcon("src/main/resources/cards/" + suits[suit.v()] + ".png").getImage();
        }
        return cardSuits[suit.v()];
    }
    static public Image getCardImage(Card c) {
        int idx = c.getCardSuit().v() * CardValue.values().length + c.getCardValue().v();
        if(null == cardImages[idx]){
            final String[] values = {
                    "SIX", "SEVEN", "EIGHT", "NIGHT", "TEN",
                    "JACK", "QUEEN", "KING", "ACE"
            };
            cardImages[idx] = new ImageIcon("src/main/resources/cards/" +
                    suits[c.getCardSuit().v()] + "/" + values[c.getCardValue().v()] + ".JPG").getImage();
        }
        return cardImages[idx];
    }
}
