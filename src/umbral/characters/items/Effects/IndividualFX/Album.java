package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.items.Effects.Effect;

public class Album extends Effect {
    public Album() {
        cdVariant = 7;
        damage = 50;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("album");
        return new double[]{1, this.damage, -150, -20, -5, 200};
    }
}
