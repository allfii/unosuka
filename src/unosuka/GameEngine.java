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
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import unosuka.AI.Level;
import unosuka.Card.Color;

/**
 *
 * Game Engine maintains and controls flow of the game
 */
public class GameEngine {

    private ArrayList<Player> players;
    private int turn;
    private boolean turnDirectionInc;
    private GameLog log;
    private boolean gameFinish;
    private ArrayList<Card> deck;
    private ArrayList<Card> wastedCards;
    private int numberOfPlayer;
    private Random random;
    private Card lastCard;
    private int unoShouted;
    private GameLog gameLog;
    private ArrayList<PlayerLog> playerLog;
    private ArrayList<Player> ranking;
    public ArrayList<PlayerActivity> playerActivity;

    public GameEngine(int rule, int numPlayers) {
        random = new Random();
        gameFinish = false;
        turnDirectionInc = true;
        numberOfPlayer = numPlayers;
        unoShouted = -1;
        initializePlayer();
        turn = random.nextInt(players.size());
        deck = Card.InitializeDeck();
        wastedCards = new ArrayList<Card>();
        gameLog = new GameLog(numberOfPlayer, rule);
        ranking = new ArrayList<>();
    }

    public void initializeGame() {
        do {
            lastCard = getTopCardFromDeck(-1);
            if (Setting.DEBUG) {
                System.out.println("First Card: ");
            }
            Card.showCard(lastCard);
            if (lastCard.getContent() == Card.Content.DRAWFOUR) {
                putCardToBottomOfDeck(lastCard);
            }
        } while (lastCard.getContent() == Card.Content.DRAWFOUR);

    }

    public void initializePlayer() {
        players = new ArrayList<Player>();
        playerActivity = new ArrayList<>();
        for (int i = 0; i < numberOfPlayer; i++) {
            final int j = i;
            Level level;
            switch(random.nextInt(4)){
                case 1:
                    players.add(new Player(i, new ArrayList<Card>(), AI.Level.OFFENSIVE));
                    break;
                case 2:
                    players.add(new Player(i, new ArrayList<Card>(), AI.Level.DEFENSIVE));
                    break;
                case 3:
                    players.add(new Player(i, new ArrayList<Card>(), AI.Level.THINKER));
                    players.get(i).setMemoryLevel(random.nextInt(10)+1); // level of prunning
//                    players.get(i).setMemoryLevel(3); // 3 level is enough
                    break;
                default:
                    players.add(new Player(i, new ArrayList<Card>(), AI.Level.AMATEUR));
                    break;
            }
            playerActivity.add(new PlayerActivity(i, Setting.PLAYER_INIT_CARDS));
            players.get(i).setIUno(new IUno() {

                @Override
                public ArrayList<unosuka.PlayerActivity> unoMind() {
                    final int z = j;
                    ArrayList<PlayerActivity> retval = new ArrayList<>();
                    if (!players.get(z).hasCards()) {
                        return retval;
                    }
                    for (int k = 1; k < numberOfPlayer; k++) {
                        int nextTurn = nextPlayerTurn(z, k);
                        if (nextTurn == players.get(z).getId()) {
                            return retval;
                        }
                        boolean found = false;
                        for (int l = 0; l < retval.size(); l++) {
                            if (retval.get(l).getId() == nextTurn) {
                                found = true;
                            }
                        }
                        if (!found) {
//                            PlayerActivity pA = new PlayerActivity(playerActivity.get(nextTurn).getId(), playerActivity.get(nextTurn).getNumberOfCards());
//                            pA.setLastPlayed(playerActivity.get(nextTurn).getLastPlayed());
//                            pA.setFinish(playerActivity.get(nextTurn).isFinish());
//                            if (Setting.DEBUG) {
//                                System.out.println("Memory level: " + players.get(z).getMemoryLevel());
//                            }
//                            for (int l = 0; l < players.get(z).getMemoryLevel(); l++) {
//                                if(playerActivity.get(nextTurn).getBlankCards().size()<=l)
//                                    break;
//                                pA.getBlankCards().add(playerActivity.get(nextTurn).getBlankCards().get(l));
//                                if (Setting.DEBUG) {
//                                    System.out.println("Add blank cards from " + nextTurn);
//                                }
//                            }
                            retval.add(playerActivity.get(nextTurn));
                        }
                    }
                    return retval;
                }
            }
            );
        }

        playerLog = new ArrayList<>();
        for (int i = 0; i < numberOfPlayer; i++) {
            playerLog.add(new PlayerLog(i, players.get(i).getAiLevel().toString().substring(0,1), players.get(i).getMemoryLevel()));
            playerLog.get(i).setNumPlayer(numberOfPlayer);
        }
    }

    public void play() {
        randomizeCards(); // each player gets 7 cards
        showPlayersCards();
        initializeGame();
        do {
            playerTurn();
            if (!endGameCondition()) {
                nextTurn();
            } else {
                gameFinish = true;
                DataSaver.saveData(gameLog, playerLog);

            }
            showPlayersCards();
        } while (!gameFinish);
    }

    public void refreshDeck() {
        for (int i = 0; i < wastedCards.size(); i++) {
            deck.add(wastedCards.get(i));
        }
        wastedCards.clear();
        shuffleDeck();
    }

    public void randomizeCards() {
        shuffleDeck();
        for (int j = 0; j < Setting.PLAYER_INIT_CARDS; j++) {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).addCard(getTopCardFromDeck(i));
            }
        }
    }

    public Card getTopCardFromDeck(int picker) {
        if (deck.size() == 0 && wastedCards.size() == 0) {
            return null;
        }
        if (picker != -1) {
            playerLog.get(picker).addPickedCards(1);
        }
        if (deck.size() == 0) {
            refreshDeck();
        }
        if (deck.size() > 0) {
            Card card = deck.get(deck.size() - 1);
            deck.remove(deck.size() - 1);
            gameLog.addPickedCards(1);
            if (card.getContent() == Card.Content.DRAWFOUR || card.getContent() == Card.Content.WILD) {
                card.setColor(Card.Color.NONE);
            }
            return card;
        } else {
            return null;
        }
    }

    public void putCardToBottomOfDeck(Card card) {
        deck.add(0, card);
        if (Setting.DEBUG) {
            System.out.println("Put card in bottom of the deck: " + card.getContent() + " size:" + deck.size());
        }
    }

    public void shuffleDeck() {
        showDeck();
        random = new Random();
        int shuffleTimes = 1000 + random.nextInt(500);
        for (int i = 0; i < shuffleTimes; i++) {
            if (random.nextBoolean()) {
                Collections.swap(deck, random.nextInt(deck.size()), random.nextInt(deck.size()));
            }
        }
        showDeck();
    }

    public void showDeck() {
        if (Setting.DEBUG) {
            System.out.println("Deck:");
        }
        for (int i = 0; i < deck.size(); i++) {
            if (Setting.DEBUG) {
                System.out.println(deck.get(i).getContent() + " " + deck.get(i).getColor());
            }
        }
        if (Setting.DEBUG) {
            System.out.println("");
        }
    }

    public void showPlayersCards() {
        if (Setting.DEBUG) {
            System.out.println("Players Deck:");
        }
        for (int i = 0; i < players.size(); i++) {
            if (Setting.DEBUG) {
                System.out.print("Player " + players.get(i).getId() + "(" + players.get(i).getAiLevel() + ")" + ":");
            }
            for (int j = 0; j < players.get(i).getCards().size(); j++) {
                if (Setting.DEBUG) {
                    System.out.print("(" + players.get(i).getCards().get(j).getContent() + "-" + players.get(i).getCards().get(j).getColor() + ") ");
                }
            }
            if (Setting.DEBUG) {
                System.out.println("");
            }
        }
        if (Setting.DEBUG) {
            System.out.println("");
        }

    }

    public void debuggingPlayerActivity() {
        for (int i = 0; i < numberOfPlayer; i++) {
            if (Setting.DEBUG) {
                System.out.println("players " + players.get(i).getId());
            }
            for (int j = 0; j < players.get(i).getIUno().unoMind().size(); j++) {
                if (Setting.DEBUG) {
                    System.out.println("players iuno " + players.get(i).getIUno().unoMind().get(j).getId());
                }
                for (int k = 0; k < players.get(i).getIUno().unoMind().get(j).getBlankCards().size(); k++) {
                    Card.showCard(players.get(i).getIUno().unoMind().get(j).getBlankCards().get(k));
                }
            }
        }
    }

    public boolean endGameCondition() {
        int playersInGame = 0;
        int indexLastPlayer = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getCards().size() > 0) {
                playersInGame += 1;
                indexLastPlayer = i;
            }
            if (playersInGame > 1) {
                break;
            }
        }
        if (playersInGame == 1) {
            playerLog.get(indexLastPlayer).setRanking(numberOfPlayer);
            return true;
        } else {
            return false;
        }
    }

    public void playerTurn() {
        if (Setting.DEBUG) {
            System.out.println("Now Turn: " + turn);
        }
        playerLog.get(turn).addPossibilities(Card.possibleCards(players.get(turn).getCards(), lastCard).size());
        if (ranking.size() < 1) // calculate while there is no winner
        {
            playerLog.get(turn).addPossibilitiesOfficial(Card.possibleCards(players.get(turn).getCards(), lastCard).size());
        }        
        Card card = null;
        if (players.get(turn).drawCard(lastCard)) {
            Card drawnCard = getTopCardFromDeck(turn);
            if (Setting.DEBUG) {
                System.out.println("Draw Card");
            }
            Card.showCard(drawnCard);
            if (drawnCard != null) { // deck is not empty
                if(Card.possibleToPlay(players.get(turn).getCards(), drawnCard, lastCard)){
                    if (ranking.size() < 1) // calculate while there is no winner
                    {
                        playerLog.get(turn).addPossibilities(1);
                        playerLog.get(turn).addPossibilitiesOfficial(1);
                    }                            
                }
                if (players.get(turn).playDrawnCard(drawnCard, lastCard)) {
                    if (Setting.DEBUG) {
                        System.out.println("Play the card");
                    }
                    card = drawnCard;
                } else {
                    if (Setting.DEBUG) {
                        System.out.println("Add card to hand");
                    }
                    players.get(turn).addCard(drawnCard);
                    updateBlankCardActivity();
                    // card = null;
                }
            }
        } else {
            card = players.get(turn).play(lastCard);
            // card must be not null
        }
        if (Setting.DEBUG) {
            System.out.print("Play: ");
        }
        Card.showCard(card);

        if (card != null) {
            unoChecking();
            lastCard = card;
            wastedCards.add(lastCard);
            playerActivity.get(turn).setLastPlayed(new Card(lastCard.getContent(), lastCard.getColor(), lastCard.getEffect()));
            playerLog.get(turn).addWastedCards();
            gameLog.addWastedCards();
            gameLog.addSuccessTurn();
            playerLog.get(turn).addSuccessTurn();
            if (players.get(turn).getCards().size() == 0) {
                playerActivity.get(turn).setFinish(false);
                ranking.add(players.get(turn));
                playerLog.get(turn).setRanking(ranking.size());
                if (ranking.size() == 1) // one kicked out
                {
                    calculateScore();
                }
            }
            if (lastCard.getEffect() == Card.Effect.ACT) {
                if (Setting.DEBUG) {
                    System.out.println("Effect!");
                }
                if (lastCard.getContent() == Card.Content.DRAWTWO) {
                    int target = nextPlayerTurn();
                    players.get(target).addCard(getTopCardFromDeck(target));
                    players.get(target).addCard(getTopCardFromDeck(target));
                    playerLog.get(nextPlayerTurn()).addGetDrawtwoAttack();
                    playerLog.get(turn).addAttackDrawtwo();
                    gameLog.addDrawTwo();
                    nextTurn();
                } else if (lastCard.getContent() == Card.Content.DRAWFOUR) {
                    int target = nextPlayerTurn();
                    players.get(target).addCard(getTopCardFromDeck(target));
                    players.get(target).addCard(getTopCardFromDeck(target));
                    players.get(target).addCard(getTopCardFromDeck(target));
                    players.get(target).addCard(getTopCardFromDeck(target));
                    pickColor();
                    playerLog.get(nextPlayerTurn()).addGetDrawfourAttack();
                    playerLog.get(turn).addAttackDrawfour();
                    gameLog.addDrawFour();
                    nextTurn();
                } else if (lastCard.getContent() == Card.Content.SKIP) {
                    playerLog.get(nextPlayerTurn()).addGetSkipped();
                    nextTurn();
                } else if (lastCard.getContent() == Card.Content.REVERSE) {
                    turnDirectionInc = !turnDirectionInc;
                } else if (lastCard.getContent() == Card.Content.WILD) {
                    pickColor();
                }
            }
        } else {
            if (Setting.DEBUG) {
                System.out.println("Pass");
            }
            //players.get(turn).addCard(getTopCardFromDeck(turn));
            gameLog.addFailedTurn();
            playerLog.get(turn).addFailedTurn();
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).hasCards()) {
                playerLog.get(i).addTurnUntilLeave();
            }
        }        
        updatePlayerActivity();
    }

    public void updatePlayerActivity() {
        for (int i = 0; i < players.size(); i++) {
            playerActivity.get(i).setNumberOfCards(players.get(i).getCards().size());
        }

    }

    public void updateBlankCardActivity() {
        playerActivity.get(turn).getBlankCards().add(0, new Card(lastCard.getContent(), lastCard.getColor(), lastCard.getEffect()));
        if (playerActivity.get(turn).getBlankCards().size() > 3) { // 3 cards is enough
            playerActivity.get(turn).getBlankCards().remove(playerActivity.get(turn).getBlankCards().size() - 1);
        }
    }

    public void updatePreferredColorActivity(int idPlayer, Color color) {
        playerActivity.get(idPlayer).getPreferredColor().add(0, color);
        if (playerActivity.get(idPlayer).getPreferredColor().size() > 3) { // 3 cards is enough
            playerActivity.get(idPlayer).getPreferredColor().remove(playerActivity.get(idPlayer).getPreferredColor().size() - 1);
        }
    }
    
    public void calculateScore() {
        int score = 0;
        for (int i = 0; i < players.size(); i++) {
            for (int j = 0; j < players.get(i).getCards().size(); j++) {
                if (players.get(i).getCards().get(j).getContent() == Card.Content.ONE) {
                    score += 1;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.TWO) {
                    score += 2;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.THREE) {
                    score += 3;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.FOUR) {
                    score += 4;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.FIVE) {
                    score += 5;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.SIX) {
                    score += 6;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.SEVEN) {
                    score += 7;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.EIGHT) {
                    score += 8;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.NINE) {
                    score += 9;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.DRAWTWO) {
                    score += 20;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.SKIP) {
                    score += 20;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.REVERSE) {
                    score += 20;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.DRAWFOUR) {
                    score += 50;
                }
                if (players.get(i).getCards().get(j).getContent() == Card.Content.WILD) {
                    score += 50;
                }
            }
        }
        gameLog.setScore(score);
    }

    public void unoChecking() {
        if (players.get(turn).unoSituation()) {
            int currentPlayer = turn;
            Random random = new Random();
            while (unoShouted == -1) {
                if (players.get(currentPlayer).unoShout()) {
                    unoShouted = players.get(currentPlayer).getId();
                    if (turn != currentPlayer) {
                        if (Setting.DEBUG) {
                            System.out.println("Uno Attack! Player " + players.get(turn).getId() + " pick 2 cards!");
                        }
                        players.get(turn).addCard(getTopCardFromDeck(turn));
                        players.get(turn).addCard(getTopCardFromDeck(turn));
                        gameLog.addFailedUno();
                        playerLog.get(turn).addFailedUno();
                        playerLog.get(currentPlayer).addSuccessAttackUno();
                    } else {
                        gameLog.addSuccessUno();
                        playerLog.get(turn).addSuccessUno();
                    }
                } else {
                    currentPlayer = nextPlayerTurn(currentPlayer);
                }
            }
            unoShouted = -1;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).hasCards()) {
                    playerLog.get(i).addAllPlayersUno();
                }
            }
        }
    }

    public void pickColor() {
        Color color = players.get(turn).pickColor();
        lastCard.setColor(color);
        updatePreferredColorActivity(turn, color);
    }

    public int nextPlayerTurn(int currentTurn) {
        int retval = currentTurn;
        do {
            if (turnDirectionInc) {
                if (retval < players.size() - 1) {
                    retval += 1;
                } else {
                    retval = 0;
                }
            } else {
                if (retval > 0) {
                    retval -= 1;
                } else {
                    retval = players.size() - 1;
                }
            }
        } while (!players.get(retval).hasCards());
        return retval;
    }

    public int nextPlayerTurn(int currentTurn, int next) {
        int retval = currentTurn;
        for (int i = 0; i < next; i++) {
            do {
                if (turnDirectionInc) {
                    if (retval < players.size() - 1) {
                        retval += 1;
                    } else {
                        retval = 0;
                    }
                } else {
                    if (retval > 0) {
                        retval -= 1;
                    } else {
                        retval = players.size() - 1;
                    }
                }
            } while (!players.get(retval).hasCards());
        }
        return retval;
    }

    public int nextPlayerTurn() {
        int retval = turn;
        do {
            if (turnDirectionInc) {
                if (retval < players.size() - 1) {
                    retval += 1;
                } else {
                    retval = 0;
                }
            } else {
                if (retval > 0) {
                    retval -= 1;
                } else {
                    retval = players.size() - 1;
                }
            }
        } while (!players.get(retval).hasCards());
        return retval;
    }

    public void nextTurn() {
        do {
            if (turnDirectionInc) {
                if (turn < players.size() - 1) {
                    turn += 1;
                } else {
                    turn = 0;
                }
            } else {
                if (turn > 0) {
                    turn -= 1;
                } else {
                    turn = players.size() - 1;
                }
            }
        } while (!players.get(turn).hasCards());
        if (Setting.DEBUG) {
            System.out.println("Last cards: ");
        }
        Card.showCard(lastCard);
        if (Setting.DEBUG) {
            System.out.println("Deck cards: " + deck.size());
        }
        if (Setting.DEBUG) {
            System.out.println("");
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

}
