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

/**
 *
 * Log class saves information in the game for each player
 */
public class PlayerLog {
    private int playerId;
    private int memoryLevel;
    private int allPlayersUno;
    private int successUno;
    private int failedUno;
    private int successAttackUno;
    private int possibilities;
    private int successTurn;
    private int failedTurn;
    private int getSkipped;
    private int getDrawtwoAttack;
    private int getDrawfourAttack;
    private int attackDrawtwo;
    private int attackDrawfour;
    private int pickedCards;
    private int wastedCards;
    private int ranking;
    private int numPlayer;
    private String level;
    private int turnUntilLeave;
    private int possibilitiesOfficial;

    public PlayerLog(int playerId, String level, int memoryLevel) {
        this.playerId = playerId;
        this.allPlayersUno = 0;
        this.successUno = 0;
        this.failedUno = 0;
        this.successAttackUno = 0;
        this.possibilities = 0;
        this.successTurn = 0;
        this.failedTurn = 0;
        this.getSkipped = 0;
        this.getDrawtwoAttack = 0;
        this.getDrawfourAttack = 0;
        this.attackDrawtwo = 0;
        this.attackDrawfour = 0;
        this.pickedCards = 0;
        this.wastedCards = 0;
        this.ranking = 0;
        this.numPlayer = 0;
        this.level = level;
        this.possibilitiesOfficial = 0;
        this.turnUntilLeave = 0;
        this.memoryLevel = memoryLevel;
    }

    public int getMemoryLevel() {
        return memoryLevel;
    }

    public void setMemoryLevel(int memoryLevel) {
        this.memoryLevel = memoryLevel;
    }

    public int getTurnUntilLeave() {
        return turnUntilLeave;
    }

    public void setTurnUntilLeave(int turnUntilLeave) {
        this.turnUntilLeave = turnUntilLeave;
    }

    public int getPossibilitiesOfficial() {
        return possibilitiesOfficial;
    }

    public void setPossibilitiesOfficial(int possibilitiesOfficial) {
        this.possibilitiesOfficial = possibilitiesOfficial;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void addPossibilitiesOfficial(int possibilities){
        setPossibilitiesOfficial(possibilitiesOfficial+possibilities);
    }
    
    public void addTurnUntilLeave(){
        setTurnUntilLeave(turnUntilLeave+1);
    }
    
    public void addAllPlayersUno(){
        setAllPlayersUno(allPlayersUno+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" allPlayersUno:"+allPlayersUno);
    }
    public void addSuccessUno(){
        setSuccessUno(successUno+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" successUno:"+successUno);
    }
    public void addFailedUno(){
        setFailedUno(failedUno+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" failedUno:"+failedUno);
    }
    public void addSuccessAttackUno(){
        setSuccessAttackUno(successAttackUno+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" successAttackUno:"+successAttackUno);
    }
    public void addPossibilities(int possibilities){
        setPossibilities(this.possibilities+possibilities);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" possibilities:"+this.possibilities);
    }
    public void addSuccessTurn(){
        setSuccessTurn(successTurn+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" successTurn:"+this.successTurn);
    }
    public void addFailedTurn(){
        setFailedTurn(failedTurn+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" failedTurn:"+this.failedTurn);
    }
    public void addGetSkipped(){
        setGetSkipped(getSkipped+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" getSkipped:"+this.getSkipped);
    }
    public void addGetDrawtwoAttack(){
        setGetDrawtwoAttack(getDrawtwoAttack+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" getDrawTwoAttack:"+this.getDrawtwoAttack);
    }
    public void addGetDrawfourAttack(){
        setGetDrawfourAttack(getDrawfourAttack+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" getDrawFourAttack:"+this.getDrawfourAttack);
    }
    public void addAttackDrawtwo(){
        setAttackDrawtwo(attackDrawtwo+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" attackDrawTwo:"+this.attackDrawtwo);
    }
    public void addAttackDrawfour(){
        setAttackDrawfour(attackDrawfour+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" attackDrawFour:"+this.attackDrawfour);
    }
    public void addPickedCards(int card){
        setPickedCards(pickedCards+card);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" pickedCards:"+this.pickedCards);
    }
    public void addWastedCards(){
        setWastedCards(wastedCards+1);
        if(Setting.DEBUG) System.out.println("Player "+playerId+" wastedCards:"+this.wastedCards);
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }
    
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getAllPlayersUno() {
        return allPlayersUno;
    }

    public void setAllPlayersUno(int allPlayersUno) {
        this.allPlayersUno = allPlayersUno;
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

    public int getSuccessAttackUno() {
        return successAttackUno;
    }

    public void setSuccessAttackUno(int successAttackUno) {
        this.successAttackUno = successAttackUno;
    }

    public int getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(int possibilities) {
        this.possibilities = possibilities;
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

    public int getGetSkipped() {
        return getSkipped;
    }

    public void setGetSkipped(int getSkipped) {
        this.getSkipped = getSkipped;
    }

    public int getGetDrawtwoAttack() {
        return getDrawtwoAttack;
    }

    public void setGetDrawtwoAttack(int getDrawtwoAttack) {
        this.getDrawtwoAttack = getDrawtwoAttack;
    }

    public int getGetDrawfourAttack() {
        return getDrawfourAttack;
    }

    public void setGetDrawfourAttack(int getDrawfourAttack) {
        this.getDrawfourAttack = getDrawfourAttack;
    }

    public int getAttackDrawtwo() {
        return attackDrawtwo;
    }

    public void setAttackDrawtwo(int attackDrawtwo) {
        this.attackDrawtwo = attackDrawtwo;
    }

    public int getAttackDrawfour() {
        return attackDrawfour;
    }

    public void setAttackDrawfour(int attackDrawfour) {
        this.attackDrawfour = attackDrawfour;
    }

    public int getPickedCards() {
        return pickedCards;
    }

    public void setPickedCards(int pickedCards) {
        this.pickedCards = pickedCards;
    }

    public int getWastedCards() {
        return wastedCards;
    }

    public void setWastedCards(int wastedCards) {
        this.wastedCards = wastedCards;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
        if(Setting.DEBUG) System.out.println("Player "+playerId+" ranking:"+this.ranking);
    }
    
}
