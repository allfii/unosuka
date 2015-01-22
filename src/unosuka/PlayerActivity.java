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
import unosuka.Card.Color;

/**
 *
 * @author masphei <masphei@gmail.com>
 */
public class PlayerActivity {
    private ArrayList<Card> blankCards;
    private ArrayList<Color> preferredColor;
    private Card lastPlayed;
    private int numberOfCards;
    private int id;
    private boolean finish;

    public ArrayList<Color> getPreferredColor() {
        return preferredColor;
    }

    public void setPreferredColor(ArrayList<Color> preferredColor) {
        this.preferredColor = preferredColor;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public Card getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Card lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public ArrayList<Card> getBlankCards() {
        return blankCards;
    }

    public void setBlankCards(ArrayList<Card> blankCards) {
        this.blankCards = blankCards;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerActivity(int id, int _numberOfCards) {
        this.id = id;
        this.blankCards = new ArrayList<Card>();
        this.preferredColor = new ArrayList<Color>();
        this.numberOfCards = _numberOfCards;
        this.lastPlayed = null;
        this.finish = false;
    }
}
