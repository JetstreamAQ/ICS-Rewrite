package umbral;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

class Instructions {
    boolean back;

    private String[] controls = {
            "Basic Controls & Functions",
            "Arrow keys -- Movement",
            "Z key -- Shoot (GAME) | Select (MENU)",
            "[Z is also used to purchase items]",
            "X key -- Activate (ACTIVE) Item",
            "WASD keys -- Select Item Slot",
            "Accumulate credits by killing enemies",
            "Spend credits on items",
            "Some items must be crafted",
            "Bought items go to the currently selected key",
            "Press [X] to return to the main menu."
    };

    void draw(Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.BOLD, 64));
        g.drawString("Instructions", 10, 55);

        g.drawLine(10, 70, 700, 70);

        g.setFont(new Font("Liberation Mono", Font.PLAIN, 25));
        for (int i = 0; i < controls.length; i++) {
            int spacing = (i == controls.length - 1) ? 62:40;
            g.drawString(controls[i], 10, 120 + (i * spacing));
        }

        String sample = "WASD";
        int rectNum = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (i != 0 || j == 1) {
                    g.drawRect(260 + (j * 64), 500 + (i * 64), 64, 64);
                    g.drawString(sample.substring(rectNum, rectNum + 1), 265 + (j * 64), 560 + (i * 64));
                    rectNum++;
                }
            }
        }
    }

    void math() {
        if (back) {
            back = false;
            Main.gameState = 0;
        }
    }
}
