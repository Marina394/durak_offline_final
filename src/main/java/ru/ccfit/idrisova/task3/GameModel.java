package ru.ccfit.idrisova.task3;

import ru.ccfit.idrisova.task3.Observer.Observable;
import ru.ccfit.idrisova.task3.Observer.Observer;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.awt.event.KeyEvent.*;

public class GameModel implements Observable, IGameModel {
    private enum GameState {
        IDLE,
        BOT_ATTACK,
        HUMAN_ATTACK,
        BOT_FIGHTBACK,
        HUMAN_FIGHTBACK,
        DEAL_CARDS,
        GAME_OVER
    }
    private GameState gameState;
    private GameState nextGameState;
    private ArrayList<Gamer> gamers;
    private GamerBot bot;
    private GamerHuman human;
    private ArrayList<Card> deck;
    private ArrayList<Card> attackCards;
    private ArrayList<Card> fightCards;
    private CardSuit trump;
    final private ArrayList<Observer> observers = new ArrayList<>();
    final private Timer timer = new Timer();

    public GameModel(){
    }

    public CardSuit getTrump() {
        return trump;
    }
    private void setTrump() {
        int indexSuit = new Random().nextInt(CardSuit.values().length);
        trump = CardSuit.values()[indexSuit];
    }
    @Override
    public void reg(Observer o) {
        observers.add(o);
    }
    @Override
    public void notifySubscribers() {
        Iterator<Observer> it = observers.iterator();
        while (it.hasNext())
            it.next().update();
    }
    public Iterator<Card> firstFightCard(){
        return null == fightCards ? null : fightCards.iterator();
    }
    public void startGame(){
        createGameDeck();
        setTrump();
        createGamers();
        if(null != attackCards)
            attackCards.clear();
        if(null != fightCards)
            fightCards.clear();
        gameState = GameState.HUMAN_ATTACK;
    }
    public int deckSize(){
        return null == deck ? 0 : deck.size();
    }
    public CardSuit trump(){
        return GameState.IDLE == gameState ? null : trump;
    }
    public int numberOfGamers(){
        return GameState.IDLE == gameState ? 0 : 2;
    }
    public Iterator<Gamer> firstGamer(){
        return null == gamers ? null : gamers.iterator();
    }
    public Iterator<Card> firstAttackCard(){
        return null == attackCards ? null : attackCards.iterator();
    }
    public void nextStep(){
        switch(gameState) {
            case BOT_ATTACK:
                botStep();
                break;
            case BOT_FIGHTBACK:
                botStep();
                break;
            case DEAL_CARDS:
                dealCards();
                break;
        }
    }
    public void gameAction(KeyEvent e){
        if(GameState.HUMAN_ATTACK == gameState || GameState.HUMAN_FIGHTBACK == gameState){
            int key = e.getKeyCode();

            if(VK_UNDEFINED != key){
                if(GameState.HUMAN_ATTACK == gameState){
                    Card card = human.attack(key - VK_0, this.getTrump());

                    if(null != card) {
                        if(null == attackCards)
                            attackCards = new ArrayList<>();
                        attackCards.add(card);
                        gameState = GameState.BOT_FIGHTBACK;
                    }
                }
                else if (key == VK_ENTER) {
                    Iterator<Card> it = attackCards.iterator();
                    while(it.hasNext()){
                        human.takeCard(it.next());
                    }
                    attackCards.clear();
                    nextGameState = GameState.BOT_ATTACK;
                    gameState = GameState.DEAL_CARDS;
                }
                else{
                    Card card = human.attack(key - VK_0, this.getTrump());
                    Card attackCard = attackCards.get(0);
                    if(card.isFightBack(attackCard, this.getTrump())){
                        fightCards.add(card);
                        nextGameState = 1 < attackCards.size() ? GameState.BOT_ATTACK : GameState.HUMAN_ATTACK;
                        gameState = GameState.DEAL_CARDS;
                    }
                    else{
                        human.takeCard(card);
                        JOptionPane.showMessageDialog(null,
                                "wrong card, try another one", "Warning", JOptionPane.INFORMATION_MESSAGE);
                        //System.out.println("wrong card, try another");
                        gameState = GameState.HUMAN_FIGHTBACK; // redundant
                    }
                }
            }
            notifySubscribers(); // redraw panel
            timer.schedule(new NextStepTask(), 2000);
        }
    }
    private void createGameDeck(){
        final int numberOfCards = 36;
        final int n = CardValue.values().length;
        Random r = new Random();
        ArrayList<Integer> cards = new ArrayList<>();
        for(int j1 = 0; j1 < numberOfCards; ++j1)
            cards.add(j1);

        deck = new ArrayList<>();
        for(int sz = numberOfCards; 0 < sz; --sz){
            int idx = r.nextInt(sz);
            int c = cards.get(idx);
            deck.add(new Card(CardSuit.values()[c/n], CardValue.values()[c%n]));
            cards.remove(idx);
        }
    }
    private void dealCards(){
        final int NumberOfCards = 6;

        if(0 == deck.size()) {
            if (0 == human.numberOfCards()) {
                if (0 == bot.numberOfCards()) {
                    JOptionPane.showMessageDialog(null,
                            "game over. draw", "Info", JOptionPane.INFORMATION_MESSAGE);
                    //System.out.println("game over. draw");
                    gameState = GameState.GAME_OVER;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "game over. you win", "Info", JOptionPane.INFORMATION_MESSAGE);
                    //System.out.println("game over. you win");
                    gameState = GameState.GAME_OVER;
                }
            } else if (0 == bot.numberOfCards()) {
                JOptionPane.showMessageDialog(null,
                        "game over. bot win", "Info", JOptionPane.INFORMATION_MESSAGE);
                //System.out.println("game over. bot win");
                gameState = GameState.GAME_OVER;
            }
        }
        if(GameState.GAME_OVER != gameState && null != gamers){
            attackCards = new ArrayList<>();
            fightCards = new ArrayList<>();
            for(Iterator<Gamer> it = gamers.iterator(); it.hasNext();){
                Gamer g = it.next();
                while((g.numberOfCards() < NumberOfCards) && (0 < deck.size())){
                    g.takeCard(deck.get(0));
                    deck.remove(0);
                }
            }
            notifySubscribers();
            timer.schedule(new NextStepTask(), 2000);
            gameState = nextGameState;
        }
    }
    private void createGamers(){
        final int numberOfCards = 6;

        gamers = new ArrayList<>();
        bot = new GamerBot(true);
        human = new GamerHuman();
        gamers.add(bot);
        gamers.add(human);

        // deal cards
        for (int j1 = 0; j1 < numberOfCards; ++j1){
            Iterator<Gamer> it = gamers.iterator();
            while(it.hasNext()){
                Gamer g = it.next();
                Card c = deck.get(0);
                g.takeCard(c);
                deck.remove(0);
            }
        }
    }
    private void botStep() {
        if(GameState.BOT_ATTACK == gameState){
            Card card = bot.attack(0, trump);
            if(null != card){
                if(null == attackCards)
                    attackCards = new ArrayList<>();
                attackCards.add(card);
                notifySubscribers();
                gameState = GameState.HUMAN_FIGHTBACK;
            }
        }
        else if(GameState.BOT_FIGHTBACK == gameState) {
            boolean ok = true;
            for(Iterator<Card> it = attackCards.iterator(); it.hasNext(); ) {
                Card card = bot.fightBack(it.next(), trump);
                if(null != card){
                    if(null == fightCards)
                        fightCards = new ArrayList<>();
                    fightCards.add(card);
                }
                else{
                    ok = false;
                    break;
                }
            }
            if(ok){
                notifySubscribers();
                gameState = GameState.DEAL_CARDS;
                nextGameState = GameState.BOT_ATTACK;
                timer.schedule(new NextStepTask(), 2000);
            }
            else{
                Iterator<Card> it = firstAttackCard();
                while(it.hasNext())
                    bot.takeCard(it.next());
                it = firstFightCard();
                while(null != it && it.hasNext())
                    bot.takeCard(it.next());
                if(null != attackCards)
                    attackCards.clear();
                if(null != fightCards)
                    fightCards.clear();
                notifySubscribers();
                gameState = GameState.DEAL_CARDS;
                nextGameState = GameState.HUMAN_ATTACK;
                nextStep();
            }
        }
    }

    private class NextStepTask extends TimerTask {
        @Override
        public void run() {
            nextStep();
        }
    }
}
