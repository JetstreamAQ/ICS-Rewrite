package umbral;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

class Menu {
    //selector movement
    boolean moveUp,
            moveDown,
            volUp,
            volDown,
            selection;

    private int selectedOption = 0;

    //text
    private String[] options = {"PLAY", "INSTRUCTIONS", "CREDITS", "COMPENDIUM", "QUIT"};

    /*coordinates*/
    private int selectorY = 300;

    void draw(Graphics2D g) {
        //text
        g.setFont(new Font("Liberation Mono", Font.BOLD, 64));
        g.setColor(Color.LIGHT_GRAY);
        g.drawString("[CULMINATING]", 210, 65);

        g.setFont(new Font("Liberation Mono", Font.PLAIN, 56));
        for (int i = 0; i < options.length; i++) {
            g.drawString(options[i], 45, 300 + (i * 50));
        }

        g.drawString(">", 8, selectorY);
    }

    void math() {
        //moving the selector
        if (moveDown && selectedOption < options.length - 1) {
            selectorY += 50;
            selectedOption++;
            moveDown = false;
        } else if (moveUp && selectedOption > 0){
            selectorY -= 50;
            selectedOption--;
            moveUp = false;
        }

        //upon selecting an option
        if (selection) {
            selection = false;

            switch(selectedOption) {
                case 0:
                    Main.gameState = 4;
                    Main.game = new Game();
                    Main.charSelect = new CharSelect();
                    Main.plr = Main.characters.getCharacters();
                    break;

                case 1:
                    Main.gameState = 1;
                    break;

                case 2:
                    Main.gameState = 2;
                    break;

                case 3:
                    Main.gameState = 3;
                    break;

                case 4:
                    Main.isRunning = false;
                    break;
            }
        }
    }
}
