package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.items.Effects.Effect;

public class ShipManual extends Effect {
    public ShipManual() {
        cdVariant = 7;
        damage = 0;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("klaxon");
        ap.play("overdrive");
        return new double[]{0.2, damage, 50, 50, 10, blockChance};
    }
}
