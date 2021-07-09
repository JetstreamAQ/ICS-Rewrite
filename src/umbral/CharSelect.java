package umbral;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

class CharSelect {
    boolean moveLeft,
            moveRight,
            select,
            back;

    private int charNum = Main.plr.size(),
                charIndex = 0;

    private String[] statTag = {
            "Health: ",
            "Spread Multiplier: ",
            "Credit Multiplier: ",
            "Damage: ",
            "Attack Speed: ",
            "Movement Speed: ",
            "Block Chance: "
    };

    private double[] statVal = {
            Main.plr.get(charIndex).getDeathType(),
            Main.plr.get(charIndex).getHealth(),
            Main.plr.get(charIndex).getSpreadMod(),
            Main.plr.get(charIndex).getCreditMod(),
            Main.plr.get(charIndex).getDamage(),
            Main.plr.get(charIndex).getAttackSpeed(),
            Main.plr.get(charIndex).getMovementSpeed(),
            Main.plr.get(charIndex).getBlockChance()
    };

    void draw(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.BOLD, 64));
        g.drawString("Select Character", 10, 55);

        g.drawLine(10, 70, 700, 70);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Liberation Mono", Font.BOLD, 32));
        g.drawString(Main.plr.get(charIndex).getName(), 10, 110);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 16));
        g.drawString(Main.plr.get(charIndex).getFlavor(), 10, 140);

        g.drawImage(Main.loadedImages.get(Main.plr.get(charIndex).getName() + "Avatar"), 10, 160, null);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Liberation Mono", Font.ITALIC, 32));
        g.drawString("Base Stats", 160, 175);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 24));
        for (int i = 0; i < statTag.length; i++) {
            String statText = (i == statTag.length - 1 ) ? statTag[i] + statVal[i + 1] + "%":statTag[i] + statVal[i + 1];
            g.drawString(statText, 160, 210 + (i * 30));
        }

        g.drawRect(10, 400, 700, 230);
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 15));
        for (int i = 0; i < Main.plr.get(charIndex).getDesc().split("\n").length; i++) {
            g.drawString(Main.plr.get(charIndex).getDesc().split("\n")[i], 20, 420 + (20 * i));
        }

        g.setColor(Color.RED);
        g.setFont(new Font("Liberation Mono", Font.PLAIN, 24));
        g.drawString("Press [Z] to select character.", 10, 660);
        g.drawString("Press [X] to go back.", 10, 685);
    }

    void math() {
        if (moveRight && charIndex < charNum - 1) {
            charIndex++;
            moveRight = false;
        } else if (moveLeft && charIndex > 0) {
            charIndex--;
            moveLeft = false;
        } else if (select) {
            Main.game.deathType = (int)statVal[0];
            Main.game.playerHealth = (int)statVal[1];
            Main.game.playerMaxHealth = Main.game.playerHealth;
            Main.game.baseSpreadMod = (int)statVal[2];
            Main.game.baseCreditMod = statVal[3];
            Main.game.baseDmg = (int)statVal[4];
            Main.game.baseAS = (int)statVal[5];
            Main.game.baseMS = (int)statVal[6];
            Main.game.baseBlockChance = (int)statVal[7];

            Main.game.charID = Main.plr.get(charIndex).getName();
            Main.game.charNum = charIndex;

            Main.gameState = 5;
        }

        /*
         * Updating statVal values...
         *
         * I absolutely hate how I do this...
         */
        statVal[0] = Main.plr.get(charIndex).getDeathType();
        statVal[1] = Main.plr.get(charIndex).getHealth();
        statVal[2] = Main.plr.get(charIndex).getSpreadMod();
        statVal[3] = Main.plr.get(charIndex).getCreditMod();
        statVal[4] = Main.plr.get(charIndex).getDamage();
        statVal[5] = Main.plr.get(charIndex).getAttackSpeed();
        statVal[6] = Main.plr.get(charIndex).getMovementSpeed();
        statVal[7] = Main.plr.get(charIndex).getBlockChance();

        if (back) {
            back = false;
            select = false;
            moveLeft = false;
            moveRight = false;

            charIndex = 0;

            Main.gameState = 0;
        }
    }
}
