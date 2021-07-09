package umbral.characters.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class VFX {
    private BufferedImage img;

    private int x, y, spriteSize;

    private SpriteDraw sd = new SpriteDraw();

    public VFX(BufferedImage img, int x, int y, int spriteSize) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.spriteSize = spriteSize;
    }

    public void drawEffect(Graphics2D g) {
        sd.drawSprite(img, x, y, g, spriteSize);
    }

    public void animatedEffect() {
        sd.simpleIterate();
    }

    public boolean fxComplete() {return sd.complete() && sd.isAnimMax();}
}
