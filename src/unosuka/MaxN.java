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
import static unosuka.Card.noColorMatch;

/**
 *
 * @author masphei <masphei@gmail.com>
 */
public class MaxN {
    
    public static Card search(int depth, ArrayList<ArrayList<Card>> players, ArrayList<Card> deck, Card lastCard){
        return prunning(depth, 0, true, players, lastCard, deck, lastCard).getMovement();
    }
    public static Tuple prunning(int depth, int turn, boolean turnDirectionInc, ArrayList<ArrayList<Card>> players, Card lastCard, ArrayList<Card> deck, Card movement){
        if (depth == 0){ // stop prunning
            return generateTuple(players, movement);
        }else{ // select the best tuple from possibilities
//            System.out.println("depth:"+depth);
//            System.out.println("player size:"+players.size());
            // get possibilities
            ArrayList<Card> possibilities = getAllPossibilities(players.get(turn), lastCard);
            // one more addition for drawing a card
            
            // best tuple
            Tuple bestTuple = null;
            
            for (int i = 0; i <= possibilities.size(); i++) {
                int newTurn = turn;
                boolean newTurnDirectionInc = turnDirectionInc;
                ArrayList<ArrayList<Card>> newPlayers = (ArrayList<ArrayList<Card>>) players.clone();
                ArrayList<Card> newDeck = (ArrayList<Card>) deck.clone();
                Card newLastCard = lastCard;
                Card newMovement;
                if(i == possibilities.size()){ // draw a card
                    newPlayers.get(newTurn).add(getTopCardFromDeck(newDeck));
                    newMovement = null;
                }else{ // play card
                    newLastCard = possibilities.get(i);
                    newMovement = possibilities.get(i);
//                    System.out.println("size:"+possibilities.size());
                    newPlayers.get(newTurn).remove(possibilities.get(i));
//                    System.out.println("size:"+possibilities.size());
                }
                
                if (newLastCard.getContent() == Card.Content.REVERSE)
                    newTurnDirectionInc = !newTurnDirectionInc;
                else if (newLastCard.getContent() == Card.Content.SKIP)
                    newTurn = nextPlayerTurn(newTurn, newTurnDirectionInc, newPlayers.size(), 1);
                else if (newLastCard.getContent() == Card.Content.DRAWTWO){
                    int aturn = nextPlayerTurn(newTurn, newTurnDirectionInc, newPlayers.size(), 1);
                    for (int j = 0; j < 2; j++) {
                        newPlayers.get(aturn).add(getTopCardFromDeck(newDeck));
                    }
                }
                else if (newLastCard.getContent() == Card.Content.DRAWFOUR){
                    int aturn = nextPlayerTurn(newTurn, newTurnDirectionInc, newPlayers.size(), 1);
                    for (int j = 0; j < 4; j++) {
                        newPlayers.get(aturn).add(getTopCardFromDeck(newDeck));
                    }
                }
                newTurn = nextPlayerTurn(newTurn, newTurnDirectionInc, newPlayers.size(), 1);
                Tuple tuple;
                
                if (players.get(turn).size()==0){ // no cards left
                    // finish
                    return generateTuple(players, movement);
                }else{
                    tuple = prunning(depth-1, newTurn, newTurnDirectionInc, newPlayers, newLastCard, newDeck, newMovement);
                }
                
                if(bestTuple == null){
                    bestTuple = generateTuple(newPlayers, newMovement);
                }else{
                    if(isTupleBetter(tuple, bestTuple, turn))
                        bestTuple = new Tuple(tuple.getElements(), tuple.getMovement());
                }
            }
//            System.out.print("best tuple:");
//            Card.showCard(bestTuple.getMovement());
            return bestTuple;
        }
    }
    public static boolean isTupleBetter(Tuple tuple, Tuple comparedTuple, int turn){
        if (tuple.getElements().get(turn).getNumberOfCards() < comparedTuple.getElements().get(turn).getNumberOfCards())
            return true;
        else return false;
    }
    public static Card getTopCardFromDeck(ArrayList<Card> deck) {
        if (deck.size() == 0) {
            return null;
        }else{
            Card card = deck.get(deck.size() - 1);
            deck.remove(deck.size() - 1);
            if (card.getContent() == Card.Content.DRAWFOUR || card.getContent() == Card.Content.WILD) {
                card.setColor(Card.Color.NONE);
            }
            return card;
        }
    }
    public static Tuple generateTuple(ArrayList<ArrayList<Card>> players, Card movement){
        ArrayList<MaxNElement> elements = new ArrayList<>();
        for (int j = 0; j < players.size(); j++) {
            elements.add(new MaxNElement(j, players.get(j).size()));
        }
        return new Tuple(elements, movement);        
    }
    public static int nextPlayerTurn(int currentTurn, boolean turnDirectionInc, int numberOfPlayer, int next) {
        int retval = currentTurn;
        for (int i = 0; i < next; i++) {
            if (turnDirectionInc) {
                if (retval < numberOfPlayer - 1) {
                    retval += 1;
                } else {
                    retval = 0;
                }
            } else {
                if (retval > 0) {
                    retval -= 1;
                } else {
                    retval = numberOfPlayer - 1;
                }
            }
        }
        return retval;
    }
    public static ArrayList<Card> getAllPossibilities(ArrayList<Card> cards, Card lastCard){
        ArrayList<Card> possibilities = new ArrayList<Card>();
        if (lastCard.getColor() == Card.Color.NONE)
            possibilities = cards;
        else{
            for(int i=0;i<cards.size();i++){
                if(cards.get(i).getContent() == Card.Content.DRAWFOUR){
                    if(noColorMatch(cards, lastCard)){
                        possibilities.add(new Card(Card.Content.DRAWFOUR, Card.Color.RED, Card.Effect.ACT));
                        possibilities.add(new Card(Card.Content.DRAWFOUR, Card.Color.BLUE, Card.Effect.ACT));
                        possibilities.add(new Card(Card.Content.DRAWFOUR, Card.Color.GREEN, Card.Effect.ACT));
                        possibilities.add(new Card(Card.Content.DRAWFOUR, Card.Color.YELLOW, Card.Effect.ACT));
                    }
                }
                else if(cards.get(i).getContent() == Card.Content.WILD){
                        possibilities.add(new Card(Card.Content.WILD, Card.Color.RED, Card.Effect.ACT));
                        possibilities.add(new Card(Card.Content.WILD, Card.Color.BLUE, Card.Effect.ACT));
                        possibilities.add(new Card(Card.Content.WILD, Card.Color.GREEN, Card.Effect.ACT));
                        possibilities.add(new Card(Card.Content.WILD, Card.Color.YELLOW, Card.Effect.ACT));
                }
                else if(cards.get(i).getContent() == lastCard.getContent() || cards.get(i).getColor() == lastCard.getColor())
                    possibilities.add(cards.get(i));
            }
        }
//        if(Setting.DEBUG) System.out.println("all possible cards: " +possibilities.size());
        return possibilities;
    }
}
