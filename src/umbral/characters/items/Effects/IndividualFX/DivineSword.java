package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class DivineSword extends Effect {
    private Random rand = new Random();
    public DivineSword() {
        cdVariant = 2;
        damage = 8;
    }

    @Override
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> temp = new ArrayList<>();

        //temp.add(new Bullet(x - 16, y - 16, 25, 270, damage, images.get("spear"), 64, false));

        for (int n = 0; n < 6; n++) {
            int m = rand.nextInt(4);
            int xIntercept = (n % 2 == 0) ? rand.nextInt(720) : 0;
            int yIntercept = (n % 2 != 0) ? rand.nextInt(720) : 0;
            m = (yIntercept > 360 || xIntercept > 360) ? -m : m;
            for (int bx = 0; bx < 50; bx++) {
                temp.add(new Bullet((bx * 20) + xIntercept, m * (bx * 20) + yIntercept, 0, 0.01, 30, rand.nextInt(360), 0, damage, images.get("cut"), false));
            }
        }

        ap.play("sword");

        return temp;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("shieldActivate");
        return new double[]{0.7, damage, attackSpeed, spreadMod, -10, 25};
    }
}
