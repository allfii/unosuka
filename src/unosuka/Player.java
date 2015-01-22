/*
 * The MIT License
 *
 * Copyright 2014 masphei <masphei@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package unosuka;

import java.util.ArrayList;
import java.util.Random;
import unosuka.AI.Level;
import unosuka.Card.Color;

/**
 *
 * Player class 
 */
public class Player {
    private int id; // player number
    private ArrayList<Card> cards; // cards in hand
    private IUno iUno;
    private boolean isHuman;
    private Level aiLevel;
    private int memoryLevel;

    public IUno getiUno() {
        return iUno;
    }

    public void setiUno(IUno iUno) {
        this.iUno = iUno;
    }

    public int getMemoryLevel() {
        return memoryLevel;
    }

    public void setMemoryLevel(int memoryLevel) {
        this.memoryLevel = memoryLevel;
    }

    public IUno getIUno() {
        return iUno;
    }

    public void setIUno(IUno _iUno) {
        this.iUno = _iUno;
    }

    public Player(int id, ArrayList<Card> cards, Level level) {
        this.id = id;
        this.cards = cards;
        this.aiLevel = level;
        this.memoryLevel = 1;
    }
    public void addCard(Card card){
        this.cards.add(card);
    }
    
    public boolean hasCards(){
        return cards.size()>0;
    }
    
    public boolean unoSituation(){
        return cards.size()==1;
    }
    
    public boolean unoShout(){
        return AI.unoShout(aiLevel);
    }
    
    public Color pickColor(){
        return AI.pickColor(aiLevel, cards, getIUno().unoMind());
    }
    
    public Card play(Card lastCard){
        Card card = null;
        ArrayList<Card> possibilities = Card.possibleCards(cards, lastCard);
        if(possibilities.size()>0){
            //card = AI.randomAction(possibilities, lastCard);
            card = AI.action(aiLevel, cards, lastCard, getIUno().unoMind());
            cards.remove(card);
        }
        return card;
    }
    
    public boolean drawCard(Card lastCard){
        ArrayList<Card> possibilities = Card.possibleCards(cards, lastCard);
        if (possibilities.size()==0){
            return true;
        }else{
            return AI.drawAction(aiLevel, cards, lastCard, getIUno().unoMind());
        }
    }
    
    public boolean playDrawnCard(Card drawnCard, Card lastCard){
        if (Card.possibleToPlay(cards, drawnCard, lastCard)){
            if(Setting.DEBUG) System.out.println(drawnCard.getContent()+"-"+drawnCard.getColor()+" vs "+lastCard.getContent()+"-"+lastCard.getColor() +" possible to play");            
            return AI.drawnCardAction(aiLevel, cards, drawnCard, lastCard, getIUno().unoMind());
        }else{
            if(Setting.DEBUG) System.out.println(drawnCard.getContent()+"-"+drawnCard.getColor()+" vs "+lastCard.getContent()+"-"+lastCard.getColor() +" not possible to play");            
            return false;
        }
    }

    public Level getAiLevel() {
        return aiLevel;
    }

    public void setAiLevel(Level level) {
        if(Setting.DEBUG) System.out.println("Players " + id + " level "+level);
        this.aiLevel = level;
    }
    
    public boolean isIsHuman() {
        return isHuman;
    }

    public void setIsHuman(boolean isHuman) {
        this.isHuman = isHuman;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    
}
