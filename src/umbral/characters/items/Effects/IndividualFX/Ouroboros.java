package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class Ouroboros extends Effect {
    public Ouroboros() {
        cdVariant = 0;
        damage = 10;
    }

    @Override
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> temp = new ArrayList<>();

        int randNum = random.nextInt(4) - 2;

        temp.add(new Bullet(x - 8, y - 8, 1, 0.25, 16, -45 + randNum, -1.5, damage, images.get("missile"), 32, true, sd));
        temp.add(new Bullet(x - 8, y - 8, 1, 0.25, 16, randNum, -2.5, damage, images.get("missile"), 32, true, sd));
        temp.add(new Bullet(x - 8, y - 8, 8, 270 + randNum, damage, images.get("missile"), 32, true, sd));
        temp.add(new Bullet(x - 8, y - 8, 1, 0.25, 16, 225 + randNum, 1.5, damage, images.get("missile"), 32, true, sd));
        temp.add(new Bullet(x - 8, y - 8, 1, 0.25, 16, 180 + randNum, 2.5, damage, images.get("missile"), 32, true, sd));

        ap.play("missileLaunch");

        return temp;
    }
}
