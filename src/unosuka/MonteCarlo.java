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

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author masphei <masphei@gmail.com>
 */
public class MonteCarlo {
    public static MonteCarloSet generateGame(ArrayList<Card> cards){
        ArrayList<Card> deck = Card.InitializeDeck();
        ArrayList<ArrayList<Card>> players = new ArrayList<ArrayList<Card>>();
        shuffleDeck(deck);
        for (int i = 0; i < Setting.PLAYER_INIT_CARDS; i++) {
            players.add(new ArrayList<Card>());
        }
        ArrayList<Card> c = new ArrayList<Card>();
        for (int i = 0; i < cards.size(); i++) {
            c.add(cards.get(i));
        }
        players.add(c);
        for (int j = 0; j < Setting.PLAYER_INIT_CARDS; j++) {
            for (int i = 1; i < players.size(); i++) {
                players.get(i).add(getTopCardFromDeck(deck));
            }
        }
        return new MonteCarloSet(players, deck);
    }
    public static void shuffleDeck(ArrayList<Card> deck) {
        Random random = new Random();
        int shuffleTimes = 1000 + random.nextInt(500);
        for (int i = 0; i < shuffleTimes; i++) {
            if (random.nextBoolean()) {
                Collections.swap(deck, random.nextInt(deck.size()), random.nextInt(deck.size()));
            }
        }
    }
    public static Card getTopCardFromDeck(ArrayList<Card> deck) {
        if (deck.size() == 0) {
            return null;
        }
        else{
            Card card = deck.get(deck.size() - 1);
            deck.remove(deck.size() - 1);
            if (card.getContent() == Card.Content.DRAWFOUR || card.getContent() == Card.Content.WILD) {
                card.setColor(Card.Color.NONE);
            }
            return card;
        }
    }
}
