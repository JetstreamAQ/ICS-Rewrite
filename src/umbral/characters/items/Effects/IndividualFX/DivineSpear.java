package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class DivineSpear extends Effect {
    public DivineSpear() {
        cdVariant = 4;
        damage = 1000;
    }

    @Override
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> temp = new ArrayList<>();
        temp.add(new Bullet(x, y, 40, 270, damage, images.get("spear"), false));
        ap.play("spear");

        return temp;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("shieldActivate");
        return new double[]{invuln, 100, 10, -50, 10, 100};
    }
}
