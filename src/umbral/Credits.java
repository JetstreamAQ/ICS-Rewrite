package umbral;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

class Credits {
    boolean back;

    private String[] creds = {
			"placeholder",
			"placeholder2"
    };

    void draw (Graphics2D g) {
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(new Font("Liberation Mono", Font.BOLD, 64));
        g.drawString("Credits", 10, 55);

        g.drawLine(10, 70, 700, 70);

        g.setFont(new Font("Liberation Mono", Font.PLAIN, 15));
        for (int i = 0; i < creds.length; i++) {
            g.drawString(creds[i], 10, 100 + (i * 20));
        }

    }

    void math() {
        if (back) {
            back = false;
            Main.gameState = 0;
        }
    }
}
