package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class ScuffedSpeakers extends Effect {
    public ScuffedSpeakers() {
        cdVariant = 7;
        damage = 1;
    }

    @Override
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> temp = new ArrayList<>();

        ap.play("speakers");
        for (int i = 0; i < 4000; i++) {
            temp.add(new Bullet(x, y, 0.5, random.nextDouble() + 0.25, 1000, random.nextInt(360), random.nextDouble() * random.nextInt(100), damage, images.get("bruh" + (i % 3)), true));
        }

        return temp;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        //4 TIMES, LETS GOOOOOO (pendbestdeck)
        new Thread(() -> {
            for (int i = 0; i < 4; i++) {
                ap.play("pendulum");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return new double[]{invuln, -10, 125, 125, -65, 300};
    }
}
