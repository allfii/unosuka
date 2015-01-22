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

/**
 *
 * Card global implementation where color and content are defined
 */
public class Card {

    public static enum Color {

        RED, BLUE, YELLOW, GREEN, NONE
    }

    public static enum Content {

        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, DRAWTWO, REVERSE, SKIP, WILD, DRAWFOUR
    }
    
    public static enum Effect{
        ACT, NORMAl
    }

    private Color color;
    private Content content;
    private Effect effect;

    public Card(Content content, Color color, Effect effect) {
        this.color = color;
        this.content = content;
        this.effect = effect;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
    
    public static ArrayList<Card> possibleCards(ArrayList<Card> cards, Card lastCard){
        ArrayList<Card> possibilities = new ArrayList<Card>();
        if (lastCard.color == Card.Color.NONE)
            possibilities = cards;
        else{
            for(int i=0;i<cards.size();i++){
                if(cards.get(i).content == Card.Content.DRAWFOUR){
                    if(noColorMatch(cards, lastCard))
                        possibilities.add(cards.get(i));
                }
                else if(cards.get(i).color == Card.Color.NONE || cards.get(i).content == lastCard.content || cards.get(i).color == lastCard.color)
                    possibilities.add(cards.get(i));
            }
        }
        if(Setting.DEBUG) System.out.println("possible cards: " +possibilities.size());
        return possibilities;
    }
    public static boolean possibleToPlay(ArrayList<Card> cards, Card drawnCard, Card lastCard){    
        if(drawnCard.content == Card.Content.DRAWFOUR){
            if(noColorMatch(cards, lastCard))
                return true;
            else return false;
        }
        else if(drawnCard.color == Card.Color.NONE || drawnCard.content == lastCard.content || drawnCard.color == lastCard.color)
            return true;
        else return false;
    }
    public static boolean noColorMatch(ArrayList<Card> cards, Card lastCard){
        for(int i=0;i<cards.size();i++){
            if(cards.get(i).color == lastCard.getColor())
                return false;
        }      
        if(lastCard.getColor() == Card.Color.NONE && cards.size()>1)
            return false;
        return true;
    }
    public static void showCard(Card card){
        if(card!=null)
            if(Setting.DEBUG) System.out.println("("+card.content+"-"+card.color+")");
        else
            if(Setting.DEBUG) System.out.println("null");
    }

    public static ArrayList<Card> InitializeDeck(){
        ArrayList<Card> deck = new ArrayList<Card>();
        for (Content d : Content.values()) {
            if (d == Content.ZERO){ // one '0' card for each color
                for (Color c : Color.values()) {
                    if (c!=Color.NONE){
                        deck.add(new Card(d, c, Effect.NORMAl));
                    }
                }
            }
            else if (d == Content.ONE || d == Content.TWO || d == Content.THREE || d == Content.FOUR || d == Content.FIVE || d == Content.SIX || d == Content.SEVEN || d == Content.EIGHT || d == Content.NINE){ 
                // two '1'-'9' cards for each color
                for (Color c : Color.values()) {
                    if (c!=Color.NONE){
                        deck.add(new Card(d, c, Effect.NORMAl));
                        deck.add(new Card(d, c, Effect.NORMAl));
                    }
                }
            }
            else if (d == Content.DRAWTWO || d == Content.SKIP || d == Content.REVERSE){ 
                // two drawtwo, skip, and reverse cards for each color
                for (Color c : Color.values()) {
                    if (c!=Color.NONE){
                        deck.add(new Card(d, c, Effect.ACT));
                        deck.add(new Card(d, c, Effect.ACT));
                    }
                }
            }
            else if (d == Content.DRAWFOUR || d == Content.WILD){ 
                // four wild and drawfour cards
                deck.add(new Card(d, Color.NONE, Effect.ACT));
                deck.add(new Card(d, Color.NONE, Effect.ACT));
                deck.add(new Card(d, Color.NONE, Effect.ACT));
                deck.add(new Card(d, Color.NONE, Effect.ACT));
            }
        }
        if(Setting.DEBUG) System.out.println(deck.size());
        return deck;
    }
}
