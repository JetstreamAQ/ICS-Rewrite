package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class TachyonDrive extends Effect {
    public TachyonDrive() {
        cdVariant = 7;
        damage = 10;
    }

    @Override
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> temp = new ArrayList<>();

        for (int i = 0; i < 360; i++) {
            double angleC = (i % 2 == 0) ? random.nextDouble() * random.nextInt(4) : -1 * random.nextDouble() * random.nextInt(4);
            temp.add(new Bullet(x, y, 1, 0.05, 100, random.nextInt(360), angleC, damage, images.get("AnomalyBullet"), false));
        }

        return temp;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("tachyonDrive1");
        ap.play("tachyonDrive2");
        return new double[]{0.5, -5, 100, 100, 50, 75};
    }
}
