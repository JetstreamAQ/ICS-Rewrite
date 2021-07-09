package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class Blunderbuss extends Effect {
    public Blunderbuss() {
        cdVariant = 2;
        damage = 1;
    }

    @Override
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> temp = new ArrayList<>();

        ap.play("heavyShot");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 24 - i; j++) {
                double angleC = ((j - (12 - i)) * 2) - i;
                temp.add(new Bullet(x, y, 20 - (i * 2.0), 270 + angleC, damage, images.get("pinkLong"), false));
            }
        }

        for (int i = 0; i < 64; i++) {
            double angleC = (i % 2 == 0) ? random.nextDouble() * 30 : -1 * random.nextDouble() * 30;
            int xOffset = (i % 2 == 0) ? random.nextInt(2) : -1 * random.nextInt(2);
            temp.add(new Bullet(x + xOffset, y, random.nextInt(10) + 5, 0.01, 40, 270 + angleC, 0, damage, images.get("pinkLong"), false));
        }

        return temp;
    }
}
