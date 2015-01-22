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

import connector.MySqlConnector;
import java.util.ArrayList;

/**
 *
 * DataSaver save information to database
 */
public class DataSaver {
    public static void saveData(GameLog gameLog, ArrayList<PlayerLog> playerLog){
        MySqlConnector mySqlConnector = new MySqlConnector();
        int gameId = mySqlConnector.getNewGameId();
        ArrayList<String> sql = new ArrayList<>();
        sql.add("insert into game (id, player, rule, success_uno, failed_uno, drawtwo, drawfour, success_turn, failed_turn, wasted_cards, picked_cards, score, date) values ("+gameId+", "+gameLog.getPlayer()+", "+gameLog.getRule()+", "+gameLog.getSuccessUno()+", "+gameLog.getFailedUno()+", "+gameLog.getDrawTwo()+", "+gameLog.getDrawFour()+", "+gameLog.getSuccessTurn()+", "+gameLog.getFailedTurn()+", "+gameLog.getWastedCards()+", "+gameLog.getPickedCards()+", "+gameLog.getScore()+", '"+gameLog.getDate()+"')");
        for(int i=0;i<playerLog.size();i++){
//            if(playerLog.get(i).getLevel().equals("T"))
//                playerLog.get(i).setLevel("T"+playerLog.get(i).getMemoryLevel());
            sql.add("insert into player (player_id, game_id, level, num_player, all_players_uno, success_uno, failed_uno, success_attack_uno, possibilities, possibilities_official, turn_until_leave, success_turn, failed_turn, get_skipped, get_drawtwo_attack, get_drawfour_attack, attack_drawtwo, attack_drawfour, picked_cards, wasted_cards, ranking) values ("+playerLog.get(i).getPlayerId()+", "+gameId+", '"+playerLog.get(i).getLevel()+"', "+playerLog.get(i).getNumPlayer()+", "+playerLog.get(i).getAllPlayersUno()+", "+playerLog.get(i).getSuccessUno()+", "+playerLog.get(i).getFailedUno()+", "+playerLog.get(i).getSuccessAttackUno()+", "+playerLog.get(i).getPossibilities()+", "+playerLog.get(i).getPossibilitiesOfficial()+", "+playerLog.get(i).getTurnUntilLeave()+", "+playerLog.get(i).getSuccessTurn()+", "+playerLog.get(i).getFailedTurn()+", "+playerLog.get(i).getGetSkipped()+", "+playerLog.get(i).getGetDrawtwoAttack()+", "+playerLog.get(i).getGetDrawfourAttack()+", "+playerLog.get(i).getAttackDrawtwo()+", "+playerLog.get(i).getAttackDrawfour()+", "+playerLog.get(i).getPickedCards()+", "+playerLog.get(i).getWastedCards()+", "+playerLog.get(i).getRanking()+")");
        }
        mySqlConnector.insert(sql);
    }
}
