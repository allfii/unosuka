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
package connector;

/**
 *
 * MySqlConnector maintains database flow
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import unosuka.Setting;

public class MySqlConnector {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/unosuka";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    public void select() {
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            if(Setting.DEBUG) System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            if(Setting.DEBUG) System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM setting";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                int player = rs.getInt("player");
                int rule = rs.getInt("rule");

                //Display values
                if(Setting.DEBUG) System.out.print("ID: " + id);
                if(Setting.DEBUG) System.out.print(", Player: " + player);
                if(Setting.DEBUG) System.out.print(", Rule: " + rule);
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        if(Setting.DEBUG) System.out.println("Goodbye!");
    }//end mainft

    public void insert(ArrayList<String> statement) {
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            if(Setting.DEBUG) System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            if(Setting.DEBUG) System.out.println("Connected database successfully...");

            //STEP 4: Execute a query
            if(Setting.DEBUG) System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();

            for(int i=0;i<statement.size();i++){
                stmt.executeUpdate(statement.get(i));
            }
            if(Setting.DEBUG) System.out.println("Inserted records into the table...");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }//end main

    public int getNewGameId() {
        Connection conn = null;
        Statement stmt = null;
        int gameId = -1;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            if(Setting.DEBUG) System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            if(Setting.DEBUG) System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT MAX(id) as max FROM game";
            ResultSet rs = stmt.executeQuery(sql);
            
            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                gameId = rs.getInt("max") + 1;
            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        return gameId;
    }//end mainft
    
    public static void main(String[] args) {
        MySqlConnector mySqlConnector = new MySqlConnector();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        int gameId = mySqlConnector.getNewGameId();
        ArrayList<String> sql = new ArrayList<String>();
        sql.add("insert into game (id, player, rule, success_uno, failed_uno, drawtwo, drawfour, success_turn, failed_turn, wasted_cards, picked_cards, date) values ("+gameId+", 2, 3, 3, 4, 5, 6, 7, 8, 9, 10, '"+dateFormat.format(date)+"')");
        sql.add("insert into player (player_id, game_id, all_players_uno, success_uno, failed_uno, success_attack_uno, possibilities, success_turn, failed_turn, get_skipped, get_drawtwo_attack, get_drawfour_attack, attack_drawtwo, attack_drawfour, picked_cards, wasted_cards, ranking) values (1, "+gameId+", 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)");
        sql.add("insert into player (player_id, game_id, all_players_uno, success_uno, failed_uno, success_attack_uno, possibilities, success_turn, failed_turn, get_skipped, get_drawtwo_attack, get_drawfour_attack, attack_drawtwo, attack_drawfour, picked_cards, wasted_cards, ranking) values (1, "+gameId+", 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)");
        sql.add("insert into player (player_id, game_id, all_players_uno, success_uno, failed_uno, success_attack_uno, possibilities, success_turn, failed_turn, get_skipped, get_drawtwo_attack, get_drawfour_attack, attack_drawtwo, attack_drawfour, picked_cards, wasted_cards, ranking) values (1, "+gameId+", 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)");
        sql.add("insert into player (player_id, game_id, all_players_uno, success_uno, failed_uno, success_attack_uno, possibilities, success_turn, failed_turn, get_skipped, get_drawtwo_attack, get_drawfour_attack, attack_drawtwo, attack_drawfour, picked_cards, wasted_cards, ranking) values (1, "+gameId+", 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)");
        mySqlConnector.insert(sql);
    }

}
