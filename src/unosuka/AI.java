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
import java.util.Arrays;
import java.util.Random;
import unosuka.Card.Color;

/**
 *
 * AI for computer logic
 */
public class AI {
    public static enum Level {
        AMATEUR, OFFENSIVE, DEFENSIVE, THINKER
    }
    public static Card randomAction(ArrayList<Card> possibleCard, Card lastCard){
        Random random = new Random();
        return possibleCard.get(random.nextInt(possibleCard.size()));
    }
    public static Card offensiveAction(ArrayList<Card> possibleCard, Card lastCard){
        return mostOffensiveCard(possibleCard, lastCard);
    }
    public static Card defensiveAction(ArrayList<Card> possibleCard, Card lastCard){
        return mostNormalCard(possibleCard, lastCard);
    }
    public static Card thinkerAction(ArrayList<Card> cards, Card lastCard, ArrayList<PlayerActivity> playerActivity){
        // draw card which others's don't have
        // if it is safe, draw only normal cards
        // need: number of others' cards and their last three blank cards
        ArrayList<Card> possibleCard = Card.possibleCards(cards, lastCard);
        //MonteCarloSet set = MonteCarlo.generateGame(cards);
        
//        System.out.print("MaxN result:");
        //Card.showCard(MaxN.search(3, set.getPlayers(), set.getDeck(), lastCard));
        
        if(othersHaveMoreCards(playerActivity, 3)){
            return mostOffensiveCard(possibleCard, lastCard);
        }else{
            int[] colorList = new int[4];
            for (int i = 0; i < colorList.length; i++) {
                colorList[i] = 0;
            }
            for (int i = 0; i < playerActivity.size(); i++) {
                for (int j = 0; j < playerActivity.get(i).getBlankCards().size(); j++) {
                    if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.RED)
                        colorList[0] += 1;
                    if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.GREEN)
                        colorList[1] += 1;
                    if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.BLUE)
                        colorList[2] += 1;
                    if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.YELLOW)
                        colorList[3] += 1;
                }
            }
            int[] cardChoices = new int[possibleCard.size()];
            for (int i = 0; i < cardChoices.length; i++) {
                cardChoices[i] = 0;
            }
            for (int i = 0; i < possibleCard.size(); i++) {
                for (int j = 0; j < playerActivity.size(); j++) {
                    for (int k = 0; k < playerActivity.get(j).getBlankCards().size(); k++) {
                        if(possibleCard.get(i).getColor() == playerActivity.get(j).getBlankCards().get(k).getColor() && possibleCard.get(i).getContent()== playerActivity.get(j).getBlankCards().get(k).getContent())
                            cardChoices[i] +=2;
                        else if(possibleCard.get(i).getColor() == playerActivity.get(j).getBlankCards().get(k).getColor() || possibleCard.get(i).getContent()== playerActivity.get(j).getBlankCards().get(k).getContent())
                            cardChoices[i] +=1;                        
                    }
                }
            }
            for (int i = 0; i < possibleCard.size(); i++) {
                if(possibleCard.get(i).getColor() == Card.Color.RED)
                    cardChoices[i] +=colorList[0];
                if(possibleCard.get(i).getColor() == Card.Color.GREEN)
                    cardChoices[i] +=colorList[1];
                if(possibleCard.get(i).getColor() == Card.Color.BLUE)
                    cardChoices[i] +=colorList[2];
                if(possibleCard.get(i).getColor() == Card.Color.YELLOW)
                    cardChoices[i] +=colorList[3];
            }
            int idMax = 0;
            for (int i = 0; i < cardChoices.length; i++) {
                if (cardChoices[idMax] < cardChoices[i])
                    idMax = i;                    
            }
            return possibleCard.get(idMax);
        }
    }
    public static boolean drawAction(Level level, ArrayList<Card> cardCollections, Card lastCard, ArrayList<PlayerActivity> playerActivity){
        Random random = new Random();
        if (level == Level.OFFENSIVE)
            return false;
        else if (level == Level.DEFENSIVE)
            return defensiveDrawAction(cardCollections, lastCard);
        else if (level == Level.THINKER)
            return thinkerDrawAction(cardCollections, lastCard, playerActivity);
        else if (level == Level.AMATEUR)
            return random.nextInt(10)>8; // 10% draw
        else
            return random.nextBoolean();
    }
    public static boolean drawnCardAction(Level level, ArrayList<Card> cardCollections, Card drawnCard, Card lastCard, ArrayList<PlayerActivity> playerActivity){
        Random random = new Random();
        if (level == Level.OFFENSIVE)
            return true;
        if (level == Level.DEFENSIVE)
            return defensiveDrawnCardAction(cardCollections, drawnCard);
        if (level == Level.THINKER)
            return thinkerDrawnCardAction(cardCollections, drawnCard, lastCard, playerActivity);
        else if (level == Level.AMATEUR)
            return random.nextInt(10)<8; // 90% play
        else
            return random.nextBoolean();
    }
    public static boolean defensiveDrawnCardAction(ArrayList<Card> cardCollections, Card drawnCard){
        //return drawnCard.getEffect()==Card.Effect.NORMAl || cardCollections.size() > 3;
        return true;
    }
    
    public static boolean thinkerDrawAction(ArrayList<Card> cardCollections, Card lastCard, ArrayList<PlayerActivity> playerActivity){
        // if don't have normal cards and safe to draw card
        // need: number of others' cards and their last three blank cards
        if(othersHaveMoreCards(playerActivity, 7) && !hasNormalCards(cardCollections, lastCard))
            return true;
        else return false;
    }
    public static boolean hasNormalCards(ArrayList<Card> cardCollections, Card lastCard){
        ArrayList<Card> possibilities = Card.possibleCards(cardCollections, lastCard);
        boolean hasNormalCards = false;
        for (int i = 0; i < possibilities.size(); i++) {
            if (possibilities.get(i).getEffect() == Card.Effect.NORMAl){
                hasNormalCards = true;
                break;
            }
        }
        return hasNormalCards;        
    }
    public static boolean othersHaveMoreCards(ArrayList<PlayerActivity> playerActivity, int cards){
        for (int i = 0; i < playerActivity.size(); i++) {
            if(playerActivity.get(i).getNumberOfCards()<=cards)
                return false;
        }
        return true;
    }
    public static boolean defensiveDrawAction(ArrayList<Card> cardCollections, Card lastCard){
        return false;
    }
    
    public static boolean thinkerDrawnCardAction(ArrayList<Card> cardCollections, Card drawnCard, Card lastCard, ArrayList<PlayerActivity> playerActivity){
        if(!othersHaveMoreCards(playerActivity, 5) || drawnCard.getEffect()==Card.Effect.NORMAl)
            return true;
        else return false;
    }
    
    public static boolean randomUnoShout(){
        Random random = new Random();
        return random.nextBoolean(); // 50%
    }
    
    public static boolean offensiveUnoShout(){
        Random random = new Random();
        return random.nextInt(10) < 9; // 90%
    }
    
    public static boolean defensiveUnoShout(){
        Random random = new Random();
        return random.nextInt(10) < 9; // 90%
    }
    
    public static boolean thinkerUnoShout(){
        Random random = new Random();
        return random.nextInt(20) < 19; // 95%
    }
    
    public static boolean unoShout(Level level){
        if (level == Level.OFFENSIVE){
            return offensiveUnoShout();
        }else if(level == Level.DEFENSIVE){
            return defensiveUnoShout();
        }else if (level == Level.THINKER){
            return thinkerUnoShout();
        }else{
            return randomUnoShout();
        }
    }
    public static Card action(Level level, ArrayList<Card> cards, Card lastCard, ArrayList<PlayerActivity> playerActivity){
        ArrayList<Card> possibleCard = Card.possibleCards(cards, lastCard);
        if (level == Level.OFFENSIVE){
            return offensiveAction(possibleCard, lastCard);
        }else if(level == Level.DEFENSIVE){
            return defensiveAction(possibleCard, lastCard);
        }else if (level == Level.THINKER){
            return thinkerAction(cards, lastCard, playerActivity);
        }else{
            return randomAction(possibleCard, lastCard);
        }
    }
    public static Color pickColor(Level level, ArrayList<Card> cardCollections, ArrayList<PlayerActivity> playerActivity){
        Color color = null;
        if (level == Level.OFFENSIVE){
            color = offensivePickColor(cardCollections);
        }else if(level == Level.DEFENSIVE){
            color = defensivePickColor(cardCollections);
        }else if (level == Level.THINKER){
            color = thinkerPickColor(cardCollections, playerActivity);
        }else{
            color = randomPickColor(cardCollections);
        }
        if(Setting.DEBUG) System.out.println("Pick Color " + level + " : "+color);        
        return color;
    }
    
    public static Color thinkerPickColor(ArrayList<Card> cardCollections, ArrayList<PlayerActivity> playerActivity){
        // what others mostly don't have and I have
        // need: number of others' cards and their last three blank cards
        int[] colorList = new int[4];
        for (int i = 0; i < colorList.length; i++) {
            colorList[i] = 0;
        }
        for (int i = 0; i < playerActivity.size(); i++) {
            for (int j = 0; j < playerActivity.get(i).getBlankCards().size(); j++) {
                if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.RED)
                    colorList[0] += 1;
                if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.GREEN)
                    colorList[1] += 1;
                if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.BLUE)
                    colorList[2] += 1;
                if(playerActivity.get(i).getBlankCards().get(j).getColor() == Card.Color.YELLOW)
                    colorList[3] += 1;                    
            }                    
        }
        for (int i = 0; i < cardCollections.size(); i++) {
            if(cardCollections.get(i).getColor() == Card.Color.RED)
                colorList[0] +=1;
            if(cardCollections.get(i).getColor() == Card.Color.GREEN)
                colorList[1] +=1;
            if(cardCollections.get(i).getColor() == Card.Color.BLUE)
                colorList[2] +=1;
            if(cardCollections.get(i).getColor() == Card.Color.YELLOW)
                colorList[3] +=1;
        }
        int idMax = -1;
        for (int i = 0; i < colorList.length; i++) {
            if (colorList[i] > 0){
                if (idMax != -1){
                    if (colorList[idMax] < colorList[i])
                        idMax = i;                    
                }else{
                    idMax = i;
                }
            }
        }
        if (idMax == 0)
            return Color.RED;
        else if (idMax == 1)
            return Color.GREEN;
        else if (idMax == 2)
            return Color.BLUE;
        else if (idMax == 3)
            return Color.YELLOW;
        else return randomPickColor(cardCollections);
    }
    
    public static Color randomPickColor(ArrayList<Card> cardCollections){
        Random random = new Random();
        Color color = Card.Color.NONE;
        if(cardCollections.size()>0) 
            color = cardCollections.get(random.nextInt(cardCollections.size())).getColor();
        else{
            if (random.nextBoolean())
                if(random.nextBoolean()) color = Card.Color.BLUE;
                else color = Card.Color.GREEN;
            else {
                if(random.nextBoolean()) color = Card.Color.RED;
                else color = Card.Color.YELLOW;
            }
        }
        return color;
    }
    
    public static Color getTheMostColor(ArrayList<Card> cardCollections){
        Color color = Card.Color.NONE;
        int[] colorList = new int[4];
        for (int i = 0; i < colorList.length; i++) {
            colorList[i] = 0;
        }
        // find the most color
        for (int i = 0; i < cardCollections.size(); i++) {
            int add = 1;
            if (cardCollections.get(i).getEffect()== Card.Effect.ACT)
                add = 3; // an action card is stronger than 2 number cards
            if(cardCollections.get(i).getColor() == Card.Color.RED)
                colorList[0] +=add;
            if(cardCollections.get(i).getColor() == Card.Color.GREEN)
                colorList[1] +=add;
            if(cardCollections.get(i).getColor() == Card.Color.BLUE)
                colorList[2] +=add;
            if(cardCollections.get(i).getColor() == Card.Color.YELLOW)
                colorList[3] +=add;
        }
        int idMax = -1;
        for (int i = 0; i < colorList.length; i++) {
            if (colorList[i] > 0){
                if (idMax != -1){
                    if (colorList[idMax] < colorList[i])
                        idMax = i;                    
                }else{
                    idMax = i;
                }
            }
        }
        if (idMax == 0)
            return Color.RED;
        else if (idMax == 1)
            return Color.GREEN;
        else if (idMax == 2)
            return Color.BLUE;
        else if (idMax == 3)
            return Color.YELLOW;
        else 
            return randomPickColor(cardCollections);
    }
    
    public static Color offensivePickColor(ArrayList<Card> cardCollections){
        return getTheMostColor(cardCollections);
    }
    
    public static Color defensivePickColor(ArrayList<Card> cardCollections){
        return getTheMostColor(cardCollections);
    }
    
    public static Card mostOffensiveCard(ArrayList<Card> possibleCard, Card lastCard){
        Card card = null;
        for (int i = 0; i < possibleCard.size(); i++) {
            if (card==null)
                card = possibleCard.get(i);
            else{
                if (card.getContent() == Card.Content.WILD){
                    if (possibleCard.get(i).getContent() == Card.Content.DRAWFOUR || possibleCard.get(i).getContent() == Card.Content.DRAWTWO || possibleCard.get(i).getContent() == Card.Content.SKIP || possibleCard.get(i).getContent() == Card.Content.REVERSE)
                        card = possibleCard.get(i);
                }
                else if (card.getContent() == Card.Content.SKIP || card.getContent() == Card.Content.REVERSE){
                    if (possibleCard.get(i).getContent() == Card.Content.DRAWTWO || possibleCard.get(i).getContent() == Card.Content.DRAWFOUR)
                        card = possibleCard.get(i);
                }
                else if (card.getContent() == Card.Content.DRAWTWO){
                    if (possibleCard.get(i).getContent() == Card.Content.DRAWFOUR)
                        card = possibleCard.get(i);
                }
                else if (card.getContent() == Card.Content.DRAWFOUR){
                    card = possibleCard.get(i);
                }else{ // not necessary
                    //if(possibleCard.get(i).getColor()!=lastCard.getColor())
                    //    card = possibleCard.get(i);
                }
            }
        }
        return card;
    }
    
    public static Card mostNormalCard(ArrayList<Card> possibleCard, Card lastCard){
        Card card = null;
        for (int i = 0; i < possibleCard.size(); i++) {
            if (card==null)
                card = possibleCard.get(i);
            else{
                if (possibleCard.get(i).getEffect() == Card.Effect.NORMAl)
                    card = possibleCard.get(i);
            }
        }
        return card;
    }
}
