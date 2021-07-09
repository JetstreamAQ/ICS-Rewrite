package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class Eviction extends Effect {
    public Eviction() {
        cdVariant = 7;
        damage = 500;
    }

    @Override
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> temp = new ArrayList<>();

        ap.play("missileLaunch");
        ap.play("eviction");
        temp.add(new Bullet(x, y, 2, random.nextInt(360), damage, images.get("person"), true));

        return temp;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        return new double[]{1, -10, -100, 50, -40, 250};
    }
}
