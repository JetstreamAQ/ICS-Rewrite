package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.items.Effects.Effect;

public class Autoloader extends Effect {
    public Autoloader() {
        cdVariant = 4;
        damage = 0;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("autoloader");
        return new double[]{invuln, damage, 20, 20, movementSpeed, blockChance};
    }
}
