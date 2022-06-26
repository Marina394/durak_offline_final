package ru.ccfit.idrisova.task3;

import ru.ccfit.idrisova.task3.Observer.Observable;
import ru.ccfit.idrisova.task3.Observer.Observer;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Iterator;

public class GameView extends JPanel implements Observer {
    final private IGameModel igameModel;
    public ActionOnKey actionOnKey = new ActionOnKey();
    final private static int width = 57;
    final private static int height = 88;

    public GameView(IGameModel gm, Observable observer) {
        this.igameModel = gm;
        setBackground(Color.gray);
        setFocusable(true);

        observer.reg(this);
        this.addKeyListener(this.actionOnKey);
    }
    private void drawGamer(Graphics2D g, Gamer gamer, Point basePoint){
        Iterator<Card> it = gamer.firstCard();
        final int w = width + 5;
        final int n = gamer.numberOfCards();
        int j1 = 0;

        while(it.hasNext()){
            Point p = new Point(basePoint.x - w*(n - 2*j1)/2, basePoint.y);
            drawCard(g, it.next(), p);
            ++j1;
        }
    }
    private void drawDeck(Graphics2D g){
        Point deckPoint = new Point(40, 248);
        Point trumpPoint = new Point(42, 254);

        if(0 < igameModel.deckSize())
            drawCard(g, null, deckPoint);

        if (null != igameModel.trump())
            drawCardSuit(g, igameModel.trump(), trumpPoint);
    }
    private void drawAttackCards(Graphics2D g){
        Iterator<Card> it = igameModel.firstAttackCard();
        if(null != it) {
            final int n = 1; // number of attack cards from igameModel
            final int w = width + 5;
            int j1 = 0;
            Point basePoint = new Point(370, 236);
            while (it.hasNext()) {
                Point p = new Point(basePoint.x - w*(n - 2*j1)/2, basePoint.y);
                drawCard(g, it.next(), p);
                ++j1;
            }
        }
    }
    private void drawFightCards(Graphics2D g){
        Iterator<Card> it = igameModel.firstFightCard();
        if(null != it) {
            final int n = 1; // number of fight cards from igameModel
            final int w = width + 5;
            int j1 = 0;
            Point basePoint = new Point(380, 256);
            while (it.hasNext()) {
                Point p = new Point(basePoint.x - w*(n - 2*j1)/2, basePoint.y);
                drawCard(g, it.next(), p);
                ++j1;
            }
        }
    }
    private void drawCardSuit(Graphics2D g, CardSuit cs, Point p){
        g.drawImage(Card.getCardSuit(null == cs ? CardSuit.SPADES : cs), p.x, p.y, width, height, this);
    }
    private void drawCard(Graphics2D g, Card c, Point p){
        g.drawImage(null == c ? Card.getCardShirt() : Card.getCardImage(c), p.x, p.y, width, height, this);
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        if(igameModel != null){
            int xx = 350;
            int[] yy = {
                    50, 430
            };
            Graphics2D graphics2D = (Graphics2D) g;
            Iterator<Gamer> it = igameModel.firstGamer();

            int j1 = 0;
            if(null != it) {
                while (it.hasNext()) {
                    Point point = new Point(xx, yy[j1]);
                    drawGamer(graphics2D, it.next(), point);
                    ++j1;
                }
            }

            drawDeck(graphics2D);
            drawAttackCards(graphics2D);
            drawFightCards(graphics2D);
        }
    }
    @Override
    public void update() {
        repaint();
    }
    class ActionOnKey extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            if(null != igameModel)
                igameModel.gameAction(e);
        }
    }
}
