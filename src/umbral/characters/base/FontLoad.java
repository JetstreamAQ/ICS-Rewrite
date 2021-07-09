package umbral.characters.base;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.net.URL;

public class FontLoad {
    public FontLoad(){}

    public void loadFont() {
        URL url = getClass().getClassLoader().getResource("font/LiberationMono.ttf");
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        try {
            assert url != null;
            Font f = Font.createFont(Font.TRUETYPE_FONT, url.openStream());

            ge.registerFont(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
