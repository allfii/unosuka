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
 * Command Prompt UnoSuka Version
 * Use the original rules from "http://unotips.org/pdf/official_rules.pdf" with modification
 * Modification:
 * 1. The game ends when only there is one player left.
 * 2. The game starts not from turning over a card from the deck, yet from one of the players chosen by hom-pim-pa and he/she is free to put any cards on his hand 
 */
public class UnoSuka {

    public static void main(String[] args) {
        GameEngine gameEngine;
//        gameEngine  =new GameEngine(4, 4);
//        gameEngine.play();
        while(true) {
        gameEngine  =new GameEngine(2, 2);
        gameEngine.play();
//            for (int i = 3; i < 13; i++) {
//                gameEngine = new GameEngine(i, i);
//                gameEngine.play();                
//            }
        }
    }
    
}
