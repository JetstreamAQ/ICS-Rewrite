package umbral.characters.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpriteDraw {
    private float offsetX,
                  offsetY;

    private int width,
                height,
                xMax,
                yMax;

    //used for looped simple animations; tells us when to start counting down
    private boolean animStart = false,
                    animMax = false;

    private double rotation = 0.0;

    public SpriteDraw() {}

    public void drawSprite(BufferedImage img, int x, int y, Graphics2D g, int spriteSize) {
        this.width = img.getWidth();
        this.height = img.getHeight();
        xMax = (width / spriteSize) - 1;
        yMax = (height / spriteSize) - 1;

        BufferedImage temp = img.getSubimage((int)offsetX * spriteSize, (int)offsetY * spriteSize, spriteSize, spriteSize);

        g.drawImage(temp, x - spriteSize/4, y - spriteSize/4, null);
    }

    public void simpleAnim() {
        new Thread(() -> {
            int shit = (int)offsetX;
            offsetX += (!animMax) ? 0.25:-0.25;

            if (shit == 2 && !animMax) {
                animMax = true;
            } else if (shit == 0 && animMax) {
                animMax = false;
            }
        }).start();
    }

    public void simpleIterate() {
        new Thread(() -> {
            int shit = (int) offsetX,
                shit2 = (int) offsetY;
            offsetX += (offsetX < xMax) ? 1 : 0;

            if (shit == xMax && shit2 < yMax) {
                offsetY++;
                offsetX = 0;
            } else if (shit2 == yMax && shit == xMax) {
                animMax = true;
            }
        }).start();
    }

    public void anim(int startX, int startY, int endX, int endY) {
        new Thread(() -> {
            if (!animStart) {
                offsetX = startX;
                offsetY = startY;
                animStart = true;
            }

            if (offsetX < endX)
                offsetX += 0.1;

            if (offsetY < endY)
                offsetY += 0.1;

            animMax = (offsetX >= endX) && (offsetY >= endY);
        }).start();
    }

    public void changeFrame(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void reset() {
        changeFrame(0, 0);
        animMax = false;
        animStart = false;
    }

    public void reset(int offsetX, int offsetY) {
        changeFrame(offsetX, offsetY);
        animMax = false;
        animStart = false;
    }

    public boolean complete() {return (offsetY == yMax && offsetX == xMax);}
    public boolean isAnimMax() {return animMax;}
}
