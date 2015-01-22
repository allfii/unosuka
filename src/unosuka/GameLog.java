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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Log class saves information in the game
 */
public class GameLog {
    private int player;
    private int rule;
    private int successUno;
    private int failedUno;
    private int drawTwo;
    private int drawFour;
    private int successTurn;
    private int failedTurn;
    private int wastedCards;
    private int pickedCards;
    private String date;
    private int score;

    public GameLog(int player, int rule) {
        this.player = player;
        this.rule = rule;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        this.date = dateFormat.format(currentDate);
        this.successUno = 0;
        this.failedUno = 0;
        this.drawTwo = 0;
        this.drawFour = 0;
        this.successTurn = 0;
        this.failedTurn = 0;
        this.wastedCards = 0;
        this.pickedCards = 0;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        if(Setting.DEBUG) System.out.println("Gamelog score:"+this.score);
    }

    public void addSuccessUno(){
        setSuccessUno(successUno+1);
        if(Setting.DEBUG) System.out.println("Gamelog successUno:"+this.successUno);
    }
    
    public void addFailedUno(){
        setFailedUno(failedUno+1);
        if(Setting.DEBUG) System.out.println("Gamelog failedUno:"+this.failedUno);
    }
    public void addDrawTwo(){
        setDrawTwo(drawTwo+1);
        if(Setting.DEBUG) System.out.println("Gamelog drawTwo:"+this.drawTwo);
    }
    public void addDrawFour(){
        setDrawFour(drawFour+1);
        if(Setting.DEBUG) System.out.println("Gamelog drawFour:"+this.drawFour);
    }
    public void addSuccessTurn(){
        setSuccessTurn(successTurn+1);
        if(Setting.DEBUG) System.out.println("Gamelog successTurn:"+this.successTurn);
    }
    public void addFailedTurn(){
        setFailedTurn(failedTurn+1);
        if(Setting.DEBUG) System.out.println("Gamelog failedTurn:"+this.failedTurn);
    }
    public void addWastedCards(){
        setWastedCards(wastedCards+1);
        if(Setting.DEBUG) System.out.println("Gamelog wastedCards:"+this.wastedCards);
    }
    public void addPickedCards(int card){
        setPickedCards(pickedCards+card);
        if(Setting.DEBUG) System.out.println("Gamelog pickedCards:"+this.pickedCards);
    }
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public int getSuccessUno() {
        return successUno;
    }

    public void setSuccessUno(int successUno) {
        this.successUno = successUno;
    }

    public int getFailedUno() {
        return failedUno;
    }

    public void setFailedUno(int failedUno) {
        this.failedUno = failedUno;
    }

    public int getDrawTwo() {
        return drawTwo;
    }

    public void setDrawTwo(int drawTwo) {
        this.drawTwo = drawTwo;
    }

    public int getDrawFour() {
        return drawFour;
    }

    public void setDrawFour(int drawFour) {
        this.drawFour = drawFour;
    }

    public int getSuccessTurn() {
        return successTurn;
    }

    public void setSuccessTurn(int successTurn) {
        this.successTurn = successTurn;
    }

    public int getFailedTurn() {
        return failedTurn;
    }

    public void setFailedTurn(int failedTurn) {
        this.failedTurn = failedTurn;
    }

    public int getWastedCards() {
        return wastedCards;
    }

    public void setWastedCards(int wastedCards) {
        this.wastedCards = wastedCards;
    }

    public int getPickedCards() {
        return pickedCards;
    }

    public void setPickedCards(int pickedCards) {
        this.pickedCards = pickedCards;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
