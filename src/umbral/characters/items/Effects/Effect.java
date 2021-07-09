package umbral.characters.items.Effects;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public abstract class Effect {
    protected int cdVariant,
                  damage;

    protected Random random = new Random();

    public Effect() {}

    public int getCdVariant() {return cdVariant;}
    public ArrayList<Bullet> effect(Map<String, BufferedImage> images, AudioPlayer ap, SpriteDraw sd, int x, int y) {return new ArrayList<>();}

    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        return new double[]{invuln, damage, attackSpeed, spreadMod, movementSpeed, blockChance};
    }
}
